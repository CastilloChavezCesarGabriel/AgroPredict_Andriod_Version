package com.agropredict.application.authentication.usecase;

import com.agropredict.application.operation_result.IUseCaseResult;

public interface IAuthenticationResult {
    IUseCaseResult onAuthenticate();
    IUseCaseResult onReject(long currentTime);
}
