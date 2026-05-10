package com.agropredict.domain.diagnostic.visitor;

public interface IPredictionConsumer {
    void classify(String predictedCrop, double confidence);
}