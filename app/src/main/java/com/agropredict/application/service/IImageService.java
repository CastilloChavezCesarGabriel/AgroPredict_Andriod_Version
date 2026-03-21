package com.agropredict.application.service;

import com.agropredict.application.result.ClassificationResult;

public interface IImageService {
    ClassificationResult classify(String imagePath);
    String validate(String imagePath);
    String compress(String imageUriString);
}