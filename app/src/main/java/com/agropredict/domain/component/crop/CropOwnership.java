package com.agropredict.domain.component.crop;

import com.agropredict.domain.visitor.crop.ICropOwnershipVisitor;

public final class CropOwnership {
    private final String userIdentifier;
    private final String stageIdentifier;

    public CropOwnership(String userIdentifier, String stageIdentifier) {
        this.userIdentifier = userIdentifier;
        this.stageIdentifier = stageIdentifier;
    }

    public void accept(ICropOwnershipVisitor visitor) {
        visitor.visitOwnership(userIdentifier, stageIdentifier);
    }
}