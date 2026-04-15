package com.agropredict.infrastructure.image_classification;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import org.tensorflow.lite.Interpreter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public final class InterpreterLoader {
    private final AssetManager assetManager;

    public InterpreterLoader(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public Interpreter load(String modelAsset) {
        try (AssetFileDescriptor descriptor = assetManager.openFd(modelAsset);
             FileInputStream stream = new FileInputStream(descriptor.getFileDescriptor())) {
            FileChannel channel = stream.getChannel();
            MappedByteBuffer buffer = channel.map(
                    FileChannel.MapMode.READ_ONLY,
                    descriptor.getStartOffset(),
                    descriptor.getDeclaredLength());
            return new Interpreter(buffer);
        } catch (IOException exception) {
            return null;
        }
    }
}