package com.agropredict.infrastructure.image_classification;

import java.io.File;
import java.util.List;

public final class ImageValidator {
    private final List<IImageCheck> checks;

    public ImageValidator() {
        this.checks = List.of(new FormatCheck(), new SizeCheck(), new DimensionCheck());
    }

    public String validate(String imagePath) {
        File file = new File(imagePath);
        if (!file.exists()) return "File does not exist";
        for (IImageCheck check : checks) {
            String error = check.inspect(imagePath, file);
            if (error != null) return error;
        }
        return null;
    }
}