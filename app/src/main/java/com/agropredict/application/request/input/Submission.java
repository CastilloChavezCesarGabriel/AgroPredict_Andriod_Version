package com.agropredict.application.request.input;

import com.agropredict.application.visitor.ISubmissionVisitor;
import com.agropredict.application.request.data.Field;
import com.agropredict.application.request.data.Classification;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.CropImage;
import com.agropredict.domain.entity.Diagnostic;

public final class Submission {
    private final Classification prediction;
    private final Field field;

    public Submission(Classification prediction, Field field) {
        this.prediction = prediction;
        this.field = field;
    }

    public Diagnostic diagnose() {
        return prediction.derive();
    }

    public Crop cultivate() {
        return field.cultivate();
    }

    public CropImage capture() {
        return field.capture();
    }

    public void accept(ISubmissionVisitor visitor) {
        prediction.accept(visitor);
    }
}