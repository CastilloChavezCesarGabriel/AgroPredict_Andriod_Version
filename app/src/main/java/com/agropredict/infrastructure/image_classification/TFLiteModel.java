package com.agropredict.infrastructure.image_classification;

import com.agropredict.application.operation_result.ClassificationResult;
import com.agropredict.application.visitor.IClassificationResultVisitor;
import org.tensorflow.lite.Interpreter;
import java.nio.ByteBuffer;

public final class TFLiteModel {
    private final Interpreter interpreter;
    private final String[] labels;

    public TFLiteModel(Interpreter interpreter, String[] labels) {
        this.interpreter = interpreter;
        this.labels = labels;
    }

    public void infer(ByteBuffer input, IClassificationResultVisitor consumer) {
        if (interpreter == null) {
            consumer.reject("Model not available");
            return;
        }
        float[][] output = new float[1][labels.length];
        interpreter.run(input, output);
        select(output[0]).accept(consumer);
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