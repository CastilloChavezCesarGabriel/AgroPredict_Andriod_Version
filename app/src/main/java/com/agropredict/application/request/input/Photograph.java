package com.agropredict.application.request.input;

import com.agropredict.domain.entity.CropImage;

public final class Photograph {
    private final String imagePath;

    public Photograph(String imagePath) {
        this.imagePath = imagePath;
    }

    public CropImage archive() {
        String identifier = "img_" + System.currentTimeMillis();
        return CropImage.create(identifier, imagePath);
    }
}