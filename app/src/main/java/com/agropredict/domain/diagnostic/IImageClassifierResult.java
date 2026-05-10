package com.agropredict.domain.diagnostic;

import com.agropredict.domain.diagnostic.visitor.IClassificationResult;

public interface IImageClassifierResult {
    void accept(IClassificationResult visitor);
}
