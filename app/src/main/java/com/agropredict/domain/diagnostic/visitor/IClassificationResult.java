package com.agropredict.domain.diagnostic.visitor;

public interface IClassificationResult {
    void onClassify(String predictedCrop, double confidence);
    void onReject(String reason);
}
