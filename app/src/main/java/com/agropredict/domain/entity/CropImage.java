package com.agropredict.domain.entity;

import com.agropredict.domain.visitor.crop.ICropImageVisitor;

public final class CropImage {
    private static final long MAXIMUM_SIZE_BYTES = 10 * 1024 * 1024;
    private final String identifier;
    private final String filePath;

    private CropImage(String identifier, String filePath) {
        this.identifier = identifier;
        this.filePath = filePath;
    }

    public static CropImage create(String identifier, String filePath) {
        return new CropImage(identifier, filePath);
    }

    public boolean isValidFormat() {
        String lower = filePath.toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png");
    }

    public boolean isWithinSizeLimit(long fileSize) {
        return fileSize <= MAXIMUM_SIZE_BYTES;
    }

    public void accept(ICropImageVisitor visitor) {
        visitor.visit(identifier, filePath);
    }
}