package com.agropredict.domain.entity;

import com.agropredict.domain.IIdentifierConsumer;
import com.agropredict.domain.component.crop.CropProfile;
import com.agropredict.domain.visitor.crop.ICropVisitor;

public final class Crop {
    private final String identifier;
    private final String cropType;
    private final CropProfile profile;

    public Crop(String identifier, String cropType, CropProfile profile) {
        this.identifier = identifier;
        this.cropType = cropType;
        this.profile = profile;
    }

    public void accept(ICropVisitor visitor) {
        visitor.visitIdentity(identifier, cropType);
        profile.accept(visitor);
    }

    public void identify(IIdentifierConsumer consumer) {
        consumer.accept(identifier);
    }
}
