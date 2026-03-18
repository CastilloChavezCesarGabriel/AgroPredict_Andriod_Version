package com.agropredict.presentation.viewmodel.prediction;

import java.util.Map;

public interface IPredictionResultView {
    void display(Map<String, Object> diagnosticDetail);
    void warnLowConfidence();
    void navigateToHome();
}