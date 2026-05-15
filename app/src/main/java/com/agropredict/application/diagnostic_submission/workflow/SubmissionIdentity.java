package com.agropredict.application.diagnostic_submission.workflow;

import com.agropredict.application.diagnostic_submission.request.PhotographInput;
import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.identifier.IdentifierFactory;
import com.agropredict.domain.photograph.Photograph;

public final class SubmissionIdentity {
    private final String cropIdentifier;
    private final String imageIdentifier;

    public SubmissionIdentity(String cropIdentifier, String imageIdentifier) {
        this.cropIdentifier = ArgumentPrecondition.validate(cropIdentifier, "submission crop identifier");
        this.imageIdentifier = ArgumentPrecondition.validate(imageIdentifier, "submission image identifier");
    }

    public static SubmissionIdentity bind(String cropIdentifier) {
        return new SubmissionIdentity(cropIdentifier, IdentifierFactory.generate("image"));
    }

    public Diagnostic link(Diagnostic diagnostic) {
        return diagnostic.link(cropIdentifier, imageIdentifier);
    }

    public void enroll(PhotographInput input, IPhotographRepository repository) {
        Photograph photograph = input.produce(imageIdentifier);
        repository.store(photograph, cropIdentifier);
    }
}
