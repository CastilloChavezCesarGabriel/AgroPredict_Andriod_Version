package com.agropredict.application.diagnostic_submission.rejection;

import com.agropredict.domain.diagnostic.visitor.IClassificationResult;

public interface IImageRejection {
    void encode(IClassificationResult visitor);
}
