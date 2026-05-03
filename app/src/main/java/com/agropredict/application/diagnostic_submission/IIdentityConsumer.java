package com.agropredict.application.diagnostic_submission;

public interface IIdentityConsumer {
    void accept(String cropIdentifier, String imageIdentifier);
}
