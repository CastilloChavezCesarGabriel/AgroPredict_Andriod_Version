package com.agropredict.application.service;

import com.agropredict.domain.diagnostic.visitor.IClassificationResult;

public interface IImageClassifier {
    void classify(String imagePath, IClassificationResult visitor);
}