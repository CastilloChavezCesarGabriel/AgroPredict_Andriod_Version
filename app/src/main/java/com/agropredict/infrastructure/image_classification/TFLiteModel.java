package com.agropredict.infrastructure.image_classification;

import com.agropredict.domain.diagnostic.ConfidentClassification;
import com.agropredict.domain.diagnostic.IImageClassifierResult;
import com.agropredict.domain.diagnostic.Prediction;
import com.agropredict.domain.diagnostic.UnconfidentClassification;
import com.agropredict.domain.diagnostic.visitor.IClassificationResult;
import org.tensorflow.lite.Interpreter;
import java.nio.ByteBuffer;

public final class TFLiteModel {
    private final Interpreter interpreter;
    private final String[] labels;

    public TFLiteModel(Interpreter interpreter, String[] labels) {
        this.interpreter = interpreter;
        this.labels = labels;
    }

    public void infer(ByteBuffer input, IClassificationResult visitor) {
        if (interpreter == null) {
            new UnconfidentClassification("Model not available").accept(visitor);
            return;
        }
        float[][] output = new float[1][labels.length];
        interpreter.run(input, output);
        select(output[0]).accept(visitor);
    }

    private IImageClassifierResult select(float[] probabilities) {
        int bestIndex = 0;
        float bestConfidence = probabilities[0];
        for (int index = 1; index < probabilities.length; index++) {
            if (probabilities[index] > bestConfidence) {
                bestConfidence = probabilities[index];
                bestIndex = index;
            }
        }
        if (new Prediction(labels[bestIndex], bestConfidence).isConfident()) {
            return new ConfidentClassification(labels[bestIndex], bestConfidence);
        }
        return new UnconfidentClassification("Could not identify the crop with certainty");
    }
}