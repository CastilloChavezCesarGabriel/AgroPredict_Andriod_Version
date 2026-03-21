package com.agropredict.infrastructure.image;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import com.agropredict.application.result.ClassificationResult;
import com.agropredict.application.service.IImageService;
import org.json.JSONArray;
import org.json.JSONException;
import org.tensorflow.lite.Interpreter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public final class ImageService implements IImageService {
    private static final int IMAGE_SIZE = 224;
    private static final int PIXEL_CHANNELS = 3;
    private static final int BYTES_PER_FLOAT = 4;
    private static final float CONFIDENCE_THRESHOLD = 0.6f;
    private static final String MODEL_PATH = "models/cultivo_model.tflite";
    private static final String CLASSES_PATH = "models/classes.json";
    private static final String NO_CROP_LABEL = "no_cultivo";
    private static final int COMPRESSION_QUALITY = 80;
    private static final long MINIMUM_FILE_SIZE = 10 * 1024;
    private static final long MAXIMUM_FILE_SIZE = 10 * 1024 * 1024;
    private static final int MINIMUM_DIMENSION = 100;
    private static final int MAXIMUM_DIMENSION = 8000;

    private final Context context;
    private final AssetManager assetManager;
    private Interpreter interpreter;
    private String[] classLabels;

    public ImageService(Context context) {
        this.context = context;
        this.assetManager = context.getAssets();
        initialize();
    }

    @Override
    public ClassificationResult classify(String imagePath) {
        if (interpreter == null) return new ClassificationResult(NO_CROP_LABEL, 0.0);
        Bitmap scaled = resize(imagePath);
        if (scaled == null) return new ClassificationResult(NO_CROP_LABEL, 0.0);
        ByteBuffer inputBuffer = digitize(scaled);
        float[][] output = new float[1][classLabels.length];
        interpreter.run(inputBuffer, output);
        return select(output[0]);
    }

    @Override
    public String validate(String imagePath) {
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) return "El archivo no existe";
        String formatError = checkFormat(imagePath);
        if (formatError != null) return formatError;
        String sizeError = checkSize(imageFile);
        if (sizeError != null) return sizeError;
        return checkDimensions(imagePath);
    }

    @Override
    public String compress(String imageUriString) {
        Uri imageUri = Uri.parse(imageUriString);
        try {
            Bitmap bitmap = decode(imageUri);
            if (bitmap == null) return imageUriString;
            File compressed = save(bitmap);
            bitmap.recycle();
            return compressed.getAbsolutePath();
        } catch (IOException exception) {
            return imageUriString;
        }
    }

    private void initialize() {
        try {
            interpreter = load();
            classLabels = read();
        } catch (IOException | JSONException exception) {
            interpreter = null;
            classLabels = new String[0];
        }
    }

    private Interpreter load() throws IOException {
        AssetFileDescriptor descriptor = assetManager.openFd(MODEL_PATH);
        FileInputStream stream = new FileInputStream(descriptor.getFileDescriptor());
        FileChannel channel = stream.getChannel();
        MappedByteBuffer modelBuffer = channel.map(
                FileChannel.MapMode.READ_ONLY, descriptor.getStartOffset(), descriptor.getDeclaredLength());
        channel.close();
        stream.close();
        descriptor.close();
        return new Interpreter(modelBuffer);
    }

    private String[] read() throws IOException, JSONException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(assetManager.open(CLASSES_PATH)))) {
            String line;
            while ((line = reader.readLine()) != null) builder.append(line);
        }
        JSONArray jsonArray = new JSONArray(builder.toString());
        String[] labels = new String[jsonArray.length()];
        for (int index = 0; index < jsonArray.length(); index++) {
            labels[index] = jsonArray.getString(index);
        }
        return labels;
    }

    private Bitmap resize(String imagePath) {
        Bitmap original = BitmapFactory.decodeFile(imagePath);
        if (original == null) return null;
        Bitmap scaled = Bitmap.createScaledBitmap(original, IMAGE_SIZE, IMAGE_SIZE, true);
        if (original != scaled) original.recycle();
        return scaled;
    }

    private ByteBuffer digitize(Bitmap bitmap) {
        int bufferSize = BYTES_PER_FLOAT * IMAGE_SIZE * IMAGE_SIZE * PIXEL_CHANNELS;
        ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);
        buffer.order(ByteOrder.nativeOrder());
        int[] pixels = new int[IMAGE_SIZE * IMAGE_SIZE];
        bitmap.getPixels(pixels, 0, IMAGE_SIZE, 0, 0, IMAGE_SIZE, IMAGE_SIZE);
        for (int pixelValue : pixels) {
            buffer.putFloat(((pixelValue >> 16) & 0xFF) / 255.0f);
            buffer.putFloat(((pixelValue >> 8) & 0xFF) / 255.0f);
            buffer.putFloat((pixelValue & 0xFF) / 255.0f);
        }
        bitmap.recycle();
        return buffer;
    }

    private ClassificationResult select(float[] probabilities) {
        int bestIndex = 0;
        float bestConfidence = probabilities[0];
        for (int index = 1; index < probabilities.length; index++) {
            if (probabilities[index] > bestConfidence) {
                bestConfidence = probabilities[index];
                bestIndex = index;
            }
        }
        if (bestConfidence < CONFIDENCE_THRESHOLD) {
            return new ClassificationResult(NO_CROP_LABEL, 0.0);
        }
        return new ClassificationResult(classLabels[bestIndex], bestConfidence);
    }

    private String checkFormat(String path) {
        String lowercasePath = path.toLowerCase();
        boolean isJpg = lowercasePath.endsWith(".jpg");
        boolean isJpeg = lowercasePath.endsWith(".jpeg");
        boolean isPng = lowercasePath.endsWith(".png");
        if (!isJpg && !isJpeg && !isPng) return "Formato no soportado. Use JPG o PNG";
        return null;
    }

    private String checkSize(File imageFile) {
        long fileSize = imageFile.length();
        if (fileSize < MINIMUM_FILE_SIZE) return "La imagen es demasiado pequena (minimo 10KB)";
        if (fileSize > MAXIMUM_FILE_SIZE) return "La imagen es demasiado grande (maximo 10MB)";
        return null;
    }

    private String checkDimensions(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        if (options.outWidth < MINIMUM_DIMENSION || options.outHeight < MINIMUM_DIMENSION)
            return "La imagen es demasiado pequena (minimo 100x100)";
        if (options.outWidth > MAXIMUM_DIMENSION || options.outHeight > MAXIMUM_DIMENSION)
            return "La imagen es demasiado grande (maximo 8000x8000)";
        return null;
    }

    private Bitmap decode(Uri imageUri) throws IOException {
        try (InputStream stream = context.getContentResolver().openInputStream(imageUri)) {
            return BitmapFactory.decodeStream(stream);
        }
    }

    private File save(Bitmap bitmap) throws IOException {
        File outputFile = new File(context.getCacheDir(), "compressed_" + System.currentTimeMillis() + ".jpg");
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_QUALITY, outputStream);
            outputStream.flush();
        }
        return outputFile;
    }
}
