package com.agropredict.domain.crop;

import com.agropredict.domain.crop.visitor.IFieldConsumer;
import com.agropredict.domain.guard.ArgumentPrecondition;

public final class Field {
    private final String name;
    private final String location;

    public Field(String name, String location) {
        this.name = name;
        this.location = ArgumentPrecondition.validate(location, "field location");
    }

    public void locate(IFieldConsumer consumer) {
        consumer.locate(name, location);
    }
}