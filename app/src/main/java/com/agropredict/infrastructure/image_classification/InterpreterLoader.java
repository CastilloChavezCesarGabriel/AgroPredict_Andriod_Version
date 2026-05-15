package com.agropredict.infrastructure.image_classification;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import org.tensorflow.lite.Interpreter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Objects;

public final class InterpreterLoader {
    private final AssetManager assetManager;

    public InterpreterLoader(AssetManager assetManager) {
        this.assetManager = Objects.requireNonNull(assetManager, "interpreter loader requires an asset manager");
    }

    public Interpreter load(String modelAsset) {
        return build(modelAsset, map(modelAsset));
    }

    private MappedByteBuffer map(String modelAsset) {
        try (AssetFileDescriptor descriptor = assetManager.openFd(modelAsset);
             FileInputStream stream = new FileInputStream(descriptor.getFileDescriptor())) {
            return stream.getChannel().map(
                    FileChannel.MapMode.READ_ONLY,
                    descriptor.getStartOffset(),
                    descriptor.getDeclaredLength());
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Failed to read model asset '" + modelAsset
                            + "'. Confirm noCompress 'tflite' is set in build.gradle.", exception);
        }
    }

    private Interpreter build(String modelAsset, MappedByteBuffer buffer) {
        try {
            return new Interpreter(buffer, configure());
        } catch (RuntimeException exception) {
            throw new IllegalStateException(
                    "TFLite Interpreter rejected model asset '" + modelAsset
                            + "'. Likely an op-version mismatch with litert.", exception);
        }
    }

    private Interpreter.Options configure() {
        int inferenceThreads = 2;
        boolean usesCpuAccelerator = false;
        Interpreter.Options options = new Interpreter.Options();
        options.setUseXNNPACK(usesCpuAccelerator);
        options.setNumThreads(inferenceThreads);
        return options;
    }
}
