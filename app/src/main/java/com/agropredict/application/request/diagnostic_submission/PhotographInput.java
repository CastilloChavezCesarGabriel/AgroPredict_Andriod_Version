package com.agropredict.application.request.diagnostic_submission;

import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.photograph.Photograph;

public final class PhotographInput {
    private final String imagePath;

    public PhotographInput(String imagePath) {
        this.imagePath = ArgumentPrecondition.validate(imagePath, "photograph input image path");
    }

    public Photograph produce(String identifier) {
        return new Photograph(identifier, imagePath);
    }
}