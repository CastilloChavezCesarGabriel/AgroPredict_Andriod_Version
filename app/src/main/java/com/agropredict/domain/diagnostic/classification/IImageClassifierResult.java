package com.agropredict.domain.diagnostic.classification;

import com.agropredict.domain.diagnostic.visitor.IClassificationResult;

public interface IImageClassifierResult {
    void accept(IClassificationResult visitor);
}
