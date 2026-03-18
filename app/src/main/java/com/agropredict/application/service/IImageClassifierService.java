package com.agropredict.application.service;

import com.agropredict.application.result.ClassificationResult;

public interface IImageClassifierService {
    ClassificationResult classify(String imagePath);
}