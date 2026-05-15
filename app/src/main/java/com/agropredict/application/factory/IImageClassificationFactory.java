package com.agropredict.application.factory;

import com.agropredict.application.service.IImageClassifier;
import com.agropredict.application.service.IImageCompressor;
import com.agropredict.application.service.IImageValidator;

public interface IImageClassificationFactory {
    IImageClassifier createImageClassifier();
    IImageCompressor createImageCompressor();
    IImageValidator createImageValidator();
}