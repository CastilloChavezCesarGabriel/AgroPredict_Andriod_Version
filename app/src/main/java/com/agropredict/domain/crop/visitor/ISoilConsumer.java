package com.agropredict.domain.crop.visitor;

public interface ISoilConsumer {
    void analyze(String typeIdentifier, String area);
}