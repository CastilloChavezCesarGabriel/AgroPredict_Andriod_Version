package com.agropredict.application.operation_result;

public interface IOperationResult {
    void onSucceed(String value);
    void onFail();
    void onReject(String reason);
}