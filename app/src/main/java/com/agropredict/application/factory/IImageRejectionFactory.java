package com.agropredict.application.factory;

import com.agropredict.application.diagnostic_submission.rejection.IImageRejection;

public interface IImageRejectionFactory {
    IImageRejection createFormatUnsupported();
    IImageRejection createDimensionTooSmall();
    IImageRejection createDimensionTooLarge();
    IImageRejection createFileSizeTooSmall();
    IImageRejection createFileSizeTooLarge();
    IImageRejection createProcessingFailed();
    IImageRejection createConfidenceLow();
}