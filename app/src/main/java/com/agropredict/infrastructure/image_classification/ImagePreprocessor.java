package com.agropredict.infrastructure.image_classification;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class ImagePreprocessor {
    private static final int IMAGE_SIZE = 224;

    public ByteBuffer prepare(String imagePath) {
        Bitmap scaled = resize(imagePath);
        if (scaled == null) return null;
        return digitize(scaled);
    }

    private Bitmap resize(String imagePath) {
        Bitmap original = BitmapFactory.decodeFile(imagePath);
        if (original == null) return null;
        Bitmap scaled = Bitmap.createScaledBitmap(original, IMAGE_SIZE, IMAGE_SIZE, true);
        if (original != scaled) original.recycle();
        return scaled;
    }

    private ByteBuffer digitize(Bitmap bitmap) {
        int bufferSize = 4 * IMAGE_SIZE * IMAGE_SIZE * 3;
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
}