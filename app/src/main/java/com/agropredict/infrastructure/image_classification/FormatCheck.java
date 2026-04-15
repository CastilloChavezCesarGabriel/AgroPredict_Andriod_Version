package com.agropredict.infrastructure.image_classification;

import java.io.File;

public final class FormatCheck implements IImageCheck {
    @Override
    public String inspect(String imagePath, File file) {
        String lowercasePath = imagePath.toLowerCase();
        boolean supported = lowercasePath.endsWith(".jpg")
                || lowercasePath.endsWith(".jpeg")
                || lowercasePath.endsWith(".png");
        return supported ? null : "Unsupported format. Use JPG or PNG";
    }
}