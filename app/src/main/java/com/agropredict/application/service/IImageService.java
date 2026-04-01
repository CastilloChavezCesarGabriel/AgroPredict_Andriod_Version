package com.agropredict.application.service;

import com.agropredict.application.visitor.IClassificationResultVisitor;

public interface IImageService {
    void classify(String imagePath, IClassificationResultVisitor consumer);
    String compress(String imageUriString);
}