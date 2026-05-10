package com.agropredict.domain.crop;

import com.agropredict.domain.crop.visitor.ISoilConsumer;

public final class Soil {
    private final String typeIdentifier;
    private final String area;

    public Soil(String typeIdentifier, String area) {
        this.typeIdentifier = typeIdentifier;
        this.area = area;
    }

    public void analyze(ISoilConsumer consumer) {
        consumer.analyze(typeIdentifier, area);
    }
}