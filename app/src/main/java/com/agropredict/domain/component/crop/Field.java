package com.agropredict.domain.component.crop;

import com.agropredict.domain.visitor.crop.ICropVisitor;

public final class Field {
    private final String name;
    private final String location;

    public Field(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public void accept(ICropVisitor visitor) {
        visitor.visitField(name, location);
    }
}