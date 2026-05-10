package com.agropredict.application.visitor;

public interface IOperationResult {
    void onSucceed(String value);
    void onFail();
    void onReject(String reason);
}