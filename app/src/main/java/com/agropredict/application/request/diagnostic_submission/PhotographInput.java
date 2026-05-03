package com.agropredict.application.request.diagnostic_submission;

import com.agropredict.domain.entity.Photograph;

public final class PhotographInput {
    private final String imagePath;

    public PhotographInput(String imagePath) {
        this.imagePath = imagePath;
    }

    public Photograph capture(String identifier) {
        return new Photograph(identifier, imagePath);
    }
}
