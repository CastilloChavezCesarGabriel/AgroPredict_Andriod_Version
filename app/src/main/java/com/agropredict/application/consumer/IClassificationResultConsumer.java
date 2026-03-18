package com.agropredict.application.consumer;

public interface IClassificationResultConsumer {
    void visit(String predictedCrop, double confidence);
}