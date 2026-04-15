package com.agropredict.application.service;

import com.agropredict.application.visitor.IClassificationResultVisitor;

public interface IImageClassifier {
    void classify(String imagePath, IClassificationResultVisitor consumer);
}