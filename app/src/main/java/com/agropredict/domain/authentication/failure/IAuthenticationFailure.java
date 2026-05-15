package com.agropredict.domain.authentication.failure;

import com.agropredict.domain.authentication.gate.IAuthenticationFailureCallback;

public interface IAuthenticationFailure {
    void encode(IAuthenticationFailureCallback callback);
}
