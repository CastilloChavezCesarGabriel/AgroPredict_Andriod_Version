package com.agropredict.application.diagnostic_submission;

import com.agropredict.application.request.diagnostic_submission.DiagnosticSubject;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.domain.photograph.Photograph;
import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.identifier.IdentifierFactory;

public final class SubmissionIdentity {
    private final String cropIdentifier;
    private final String imageIdentifier;

    public SubmissionIdentity(String cropIdentifier, String imageIdentifier) {
        this.cropIdentifier = ArgumentPrecondition.validate(cropIdentifier, "submission crop identifier");
        this.imageIdentifier = ArgumentPrecondition.validate(imageIdentifier, "submission image identifier");
    }

    public static SubmissionIdentity generate() {
        String cropIdentifier = IdentifierFactory.generate("crop");
        String imageIdentifier = IdentifierFactory.generate("image");
        return new SubmissionIdentity(cropIdentifier, imageIdentifier);
    }

    public void link(Diagnostic diagnostic) {
        diagnostic.link(cropIdentifier, imageIdentifier);
    }

    public void enroll(DiagnosticSubject subject, CropRegistry registry) {
        Crop crop = subject.produce(cropIdentifier, registry);
        Photograph photograph = subject.capture(imageIdentifier);
        registry.register(crop, photograph);
    }
}