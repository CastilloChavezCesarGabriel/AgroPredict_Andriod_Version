package com.agropredict.application.service;

import com.agropredict.application.diagnostic_submission.rejection.IImageRejection;

public interface IImageValidator {
    IImageRejection validate(String uriString);
}
