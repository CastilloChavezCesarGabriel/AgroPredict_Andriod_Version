package com.agropredict.infrastructure.image_classification;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import com.agropredict.application.service.IImageCompressor;
import com.agropredict.domain.Identifier;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class BitmapCompressor implements IImageCompressor {
    private static final int JPEG_QUALITY = 80;

    private final Context context;

    public BitmapCompressor(Context context) {
        this.context = context;
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

    private Bitmap decode(Uri imageUri) throws IOException {
        try (InputStream stream = context.getContentResolver().openInputStream(imageUri)) {
            return BitmapFactory.decodeStream(stream);
        }
    }

    private File save(Bitmap bitmap) throws IOException {
        File outputFile = new File(context.getCacheDir(), Identifier.generate("compressed") + ".jpg");
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, outputStream);
            outputStream.flush();
        }
        return outputFile;
    }
}