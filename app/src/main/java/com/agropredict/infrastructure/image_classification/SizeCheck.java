package com.agropredict.infrastructure.image_classification;

import java.io.File;

public final class SizeCheck implements IImageCheck {
    private static final long MIN_BYTES = 10L * 1024;
    private static final long MAX_BYTES = 10L * 1024 * 1024;

    @Override
    public String inspect(String imagePath, File file) {
        long size = file.length();
        if (size < MIN_BYTES) return "Image is too small (minimum 10KB)";
        if (size > MAX_BYTES) return "Image is too large (maximum 10MB)";
        return null;
    }
}