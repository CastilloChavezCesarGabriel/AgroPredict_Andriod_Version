package com.agropredict.domain.component.diagnostic;

import com.agropredict.domain.visitor.diagnostic.IDiagnosticVisitor;

public final class CropImage {
    private final String cropIdentifier;
    private final String imageIdentifier;

    public CropImage(String cropIdentifier, String imageIdentifier) {
        this.cropIdentifier = cropIdentifier;
        this.imageIdentifier = imageIdentifier;
    }

    public void accept(IDiagnosticVisitor visitor) {
        if (cropIdentifier != null) visitor.visitCrop(cropIdentifier);
        if (imageIdentifier != null) visitor.visitImage(imageIdentifier);
    }
}
