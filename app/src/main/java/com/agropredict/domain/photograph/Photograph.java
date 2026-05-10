package com.agropredict.domain.photograph;

import com.agropredict.domain.identifier.IIdentifierConsumer;
import com.agropredict.domain.guard.ArgumentPrecondition;

public final class Photograph {
    private final String identifier;
    private final String filePath;

    public Photograph(String identifier, String filePath) {
        this.identifier = ArgumentPrecondition.validate(identifier, "photograph identifier");
        this.filePath = ArgumentPrecondition.validate(filePath, "photograph file path");
    }

    public void expose(IPhotographConsumer consumer) {
        consumer.expose(identifier, filePath);
    }

    public void identify(IIdentifierConsumer consumer) {
        consumer.identify(identifier);
    }
}
