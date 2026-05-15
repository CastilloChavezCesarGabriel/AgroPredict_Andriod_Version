package com.agropredict.infrastructure.image_classification;

import com.agropredict.application.diagnostic_submission.rejection.IImageRejection;
import com.agropredict.domain.diagnostic.classification.ConfidentClassification;
import com.agropredict.domain.diagnostic.classification.Prediction;
import com.agropredict.domain.diagnostic.visitor.IClassificationResult;
import org.tensorflow.lite.Interpreter;
import java.nio.ByteBuffer;
import java.util.Objects;

public final class TFLiteModel {
    private final Interpreter interpreter;
    private final String[] labels;
    private final IImageRejection lowConfidenceRejection;

    public TFLiteModel(Interpreter interpreter, String[] labels, IImageRejection lowConfidenceRejection) {
        this.interpreter = Objects.requireNonNull(interpreter, "tflite model requires an interpreter");
        this.labels = Objects.requireNonNull(labels, "tflite model requires labels");
        this.lowConfidenceRejection = Objects.requireNonNull(lowConfidenceRejection,
                "tflite model requires a low-confidence rejection");
    }

    public void infer(ByteBuffer input, IClassificationResult visitor) {
        float[][] output = new float[1][labels.length];
        interpreter.run(input, output);
        decide(output[0], visitor);
    }

    private void decide(float[] probabilities, IClassificationResult visitor) {
        int bestIndex = 0;
        float bestConfidence = probabilities[0];
        for (int index = 1; index < probabilities.length; index++) {
            if (probabilities[index] > bestConfidence) {
                bestConfidence = probabilities[index];
                bestIndex = index;
            }
        }
        Prediction prediction = new Prediction(labels[bestIndex], bestConfidence);
        if (prediction.isConfident()) {
            new ConfidentClassification(labels[bestIndex], bestConfidence).accept(visitor);
        } else {
            lowConfidenceRejection.encode(visitor);
        }
    }
}
