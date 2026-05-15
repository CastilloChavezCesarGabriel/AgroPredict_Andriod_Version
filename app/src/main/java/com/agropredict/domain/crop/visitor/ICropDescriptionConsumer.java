package com.agropredict.domain.crop.visitor;

public interface ICropDescriptionConsumer {
    void describe(String identifier, String type, String plantingDate);
}
