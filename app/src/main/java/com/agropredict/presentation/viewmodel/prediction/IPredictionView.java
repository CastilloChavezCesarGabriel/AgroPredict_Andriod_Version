package com.agropredict.presentation.viewmodel.prediction;

import java.util.List;

public interface IPredictionView {
    void notify(String message);
    void showLoading();
    void hideLoading();
    void displayClassification(String cropName, String confidence);
    void navigateToResult(String diagnosticIdentifier);
    void populateSoilTypes(List<String> soilTypes);
    void populateStages(List<String> stages);
}