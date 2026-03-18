package com.agropredict.infrastructure.classifier;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.agropredict.application.result.ClassificationResult;
import com.agropredict.application.service.IImageClassifierService;
import org.json.JSONArray;
import org.tensorflow.lite.Interpreter;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public final class ImageClassifierService implements IImageClassifierService {
    private static final int IMAGE_SIZE = 224;
    private static final int PIXEL_CHANNELS = 3;
    private static final int BYTES_PER_FLOAT = 4;
    private static final float CONFIDENCE_THRESHOLD = 0.6f;
    private static final String MODEL_PATH = "models/cultivo_model.tflite";
    private static final String CLASSES_PATH = "models/classes.json";
    private static final String NO_CROP_LABEL = "no_cultivo";

    private final AssetManager assetManager;
    private Interpreter interpreter;
    private String[] classLabels;

    public ImageClassifierService(AssetManager assetManager) {
        this.assetManager = assetManager;
        initialize();
    }

    @Override
    public ClassificationResult classify(String imagePath) {
        if (interpreter == null) return createDefaultResult();
        ByteBuffer inputBuffer = preprocess(imagePath);
        if (inputBuffer == null) return createDefaultResult();
        float[][] output = new float[1][classLabels.length];
        interpreter.run(inputBuffer, output);
        return select(output[0]);
    }

    private void initialize() {
        try {
            MappedByteBuffer modelBuffer = loadModel();
            interpreter = new Interpreter(modelBuffer);
            classLabels = loadClasses();
        } catch (IOException exception) {
            interpreter = null;
            classLabels = new String[0];
        }
    }

    private ClassificationResult createDefaultResult() {
        return new ClassificationResult(NO_CROP_LABEL, 0.0);
    }

    private ByteBuffer preprocess(String imagePath) {
        Bitmap originalBitmap = BitmapFactory.decodeFile(imagePath);
        if (originalBitmap == null) return null;
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, IMAGE_SIZE, IMAGE_SIZE, true);
        ByteBuffer buffer = allocateBuffer();
        fillBuffer(buffer, resizedBitmap);
        recycleBitmaps(originalBitmap, resizedBitmap);
        return buffer;
    }

    private ByteBuffer allocateBuffer() {
        int bufferSize = BYTES_PER_FLOAT * IMAGE_SIZE * IMAGE_SIZE * PIXEL_CHANNELS;
        ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);
        buffer.order(ByteOrder.nativeOrder());
        return buffer;
    }

    private void fillBuffer(ByteBuffer buffer, Bitmap bitmap) {
        int[] pixels = new int[IMAGE_SIZE * IMAGE_SIZE];
        bitmap.getPixels(pixels, 0, IMAGE_SIZE, 0, 0, IMAGE_SIZE, IMAGE_SIZE);
        for (int pixelValue : pixels) {
            buffer.putFloat(((pixelValue >> 16) & 0xFF) / 255.0f);
            buffer.putFloat(((pixelValue >> 8) & 0xFF) / 255.0f);
            buffer.putFloat((pixelValue & 0xFF) / 255.0f);
        }
    }

    private void recycleBitmaps(Bitmap original, Bitmap resized) {
        if (original != resized) {
            original.recycle();
        }
        resized.recycle();
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
            return createDefaultResult();
        }
        return new ClassificationResult(classLabels[bestIndex], bestConfidence);
    }

    private MappedByteBuffer loadModel() throws IOException {
        FileInputStream inputStream = new FileInputStream(
                assetManager.openFd(MODEL_PATH).getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = assetManager.openFd(MODEL_PATH).getStartOffset();
        long declaredLength = assetManager.openFd(MODEL_PATH).getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private String[] loadClasses() throws IOException {
        String jsonContent = readAssetFile();
        return parseClassLabels(jsonContent);
    }

    private String readAssetFile() throws IOException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(assetManager.open(CLASSES_PATH)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        }
        return builder.toString();
    }

    private String[] parseClassLabels(String jsonContent) {
        try {
            JSONArray jsonArray = new JSONArray(jsonContent);
            String[] labels = new String[jsonArray.length()];
            for (int index = 0; index < jsonArray.length(); index++) {
                labels[index] = jsonArray.getString(index);
            }
            return labels;
        } catch (Exception exception) {
            return new String[0];
        }
    }
}