package com.agropredict.domain.entity;

import com.agropredict.domain.IIdentifierConsumer;
import com.agropredict.domain.visitor.photograph.IPhotographVisitor;

public final class Photograph {
    private final String identifier;
    private final String filePath;

    public Photograph(String identifier, String filePath) {
        this.identifier = identifier;
        this.filePath = filePath;
    }

    public void accept(IPhotographVisitor visitor) {
        visitor.visitPhotograph(identifier, filePath);
    }

    public void identify(IIdentifierConsumer consumer) {
        consumer.accept(identifier);
    }
}
