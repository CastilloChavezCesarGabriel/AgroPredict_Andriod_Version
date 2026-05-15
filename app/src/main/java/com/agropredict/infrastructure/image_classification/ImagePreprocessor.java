package com.agropredict.infrastructure.image_classification;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class ImagePreprocessor {
    private static final float CHANNEL_MAX = 255.0f;
    private final int imageSize;

    public ImagePreprocessor(int imageSize) {
        validate(imageSize);
        this.imageSize = imageSize;
    }

    private static void validate(int imageSize) {
        if (imageSize <= 0) {
            throw new IllegalArgumentException("image preprocessor requires a positive image size");
        }
    }

    public ByteBuffer prepare(String imagePath) {
        Bitmap scaled = resize(imagePath);
        if (scaled == null) return null;
        return digitize(scaled);
    }

    private Bitmap resize(String imagePath) {
        Bitmap original = BitmapFactory.decodeFile(imagePath);
        if (original == null) return null;
        Bitmap scaled = Bitmap.createScaledBitmap(original, imageSize, imageSize, true);
        if (original != scaled) original.recycle();
        return scaled;
    }

    private ByteBuffer digitize(Bitmap bitmap) {
        int channels = 3;
        int bytesPerFloat = 4;
        int bufferSize = bytesPerFloat * imageSize * imageSize * channels;
        ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);
        buffer.order(ByteOrder.nativeOrder());
        int[] pixels = new int[imageSize * imageSize];
        bitmap.getPixels(pixels, 0, imageSize, 0, 0, imageSize, imageSize);

        int channelMask = 0xFF;
        for (int pixelValue : pixels) {
            buffer.putFloat(((pixelValue >> 16) & channelMask) / CHANNEL_MAX);
            buffer.putFloat(((pixelValue >> 8) & channelMask) / CHANNEL_MAX);
            buffer.putFloat((pixelValue & channelMask) / CHANNEL_MAX);
        }
        bitmap.recycle();
        return buffer;
    }
}