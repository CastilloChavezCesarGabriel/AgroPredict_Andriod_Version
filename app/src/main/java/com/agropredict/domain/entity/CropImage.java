package com.agropredict.domain.entity;

import com.agropredict.domain.visitor.ICropImageVisitor;

public final class CropImage {
    private final String identifier;
    private final String filePath;

    private CropImage(String identifier, String filePath) {
        this.identifier = identifier;
        this.filePath = filePath;
    }

    public static CropImage create(String identifier, String filePath) {
        return new CropImage(identifier, filePath);
    }

    public void accept(ICropImageVisitor visitor) {
        visitor.visit(identifier, filePath);
    }
}