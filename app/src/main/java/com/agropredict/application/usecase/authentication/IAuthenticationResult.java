package com.agropredict.application.usecase.authentication;

import com.agropredict.application.operation_result.IUseCaseResult;

public interface IAuthenticationResult {
    IUseCaseResult onAuthenticate();
    IUseCaseResult onReject(long currentTime);
}
