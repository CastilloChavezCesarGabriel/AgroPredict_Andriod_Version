package com.agropredict.presentation.viewmodel.prediction;

import java.util.List;

public interface IPredictionView {
    void notify(String message);
    void load();
    void idle();
    void classify(String cropName, String confidence);
    void reveal(String diagnosticIdentifier);
    void populateSoilTypes(List<String> soilTypes);
    void populateStages(List<String> stages);
}