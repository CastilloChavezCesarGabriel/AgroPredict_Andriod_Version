package com.agropredict.application.factory.failure;

import com.agropredict.domain.authentication.failure.IAuthenticationFailure;

public interface IAuthenticationFailureFactory {
    IAuthenticationFailure createIncorrectCredential();
    IAuthenticationFailure createLockedAccount();
    IAuthenticationFailure createExhaustedAttempt();
}
