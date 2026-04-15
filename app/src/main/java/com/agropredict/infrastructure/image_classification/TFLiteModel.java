package com.agropredict.infrastructure.image_classification;

import com.agropredict.application.operation_result.ClassificationResult;
import org.tensorflow.lite.Interpreter;
import java.nio.ByteBuffer;

public final class TFLiteModel {
    private final Interpreter interpreter;
    private final String[] labels;

    public TFLiteModel(Interpreter interpreter, String[] labels) {
        this.interpreter = interpreter;
        this.labels = labels;
    }

    public ClassificationResult infer(ByteBuffer input) {
        if (interpreter == null) return null;
        float[][] output = new float[1][labels.length];
        interpreter.run(input, output);
        return select(output[0]);
    }

    public boolean isAvailable() {
        return interpreter != null;
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
        return new ClassificationResult(labels[bestIndex], bestConfidence);
    }
}