package com.agropredict.infrastructure.image_classification;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.util.Log;
import org.tensorflow.lite.Interpreter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public final class InterpreterLoader {
    private static final String TAG = "InterpreterLoader";
    private final AssetManager assetManager;

    public InterpreterLoader(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public Interpreter load(String modelAsset) {
        MappedByteBuffer buffer = map(modelAsset);
        if (buffer == null) return null;
        return build(modelAsset, buffer);
    }

    private MappedByteBuffer map(String modelAsset) {
        try (AssetFileDescriptor descriptor = assetManager.openFd(modelAsset);
             FileInputStream stream = new FileInputStream(descriptor.getFileDescriptor())) {
            return stream.getChannel().map(
                    FileChannel.MapMode.READ_ONLY,
                    descriptor.getStartOffset(),
                    descriptor.getDeclaredLength());
        } catch (IOException exception) {
            Log.e(TAG, "Failed to read model asset '" + modelAsset
                    + "'. Confirm noCompress 'tflite' is set in build.gradle.", exception);
            return null;
        }
    }

    private Interpreter build(String modelAsset, MappedByteBuffer buffer) {
        try {
            return new Interpreter(buffer, configure());
        } catch (RuntimeException exception) {
            Log.e(TAG, "TFLite Interpreter rejected model asset '" + modelAsset
                    + "'. Likely an op-version mismatch with litert.", exception);
            return null;
        }
    }

    private Interpreter.Options configure() {
        Interpreter.Options options = new Interpreter.Options();
        options.setUseXNNPACK(false);
        options.setNumThreads(2);
        return options;
    }
}