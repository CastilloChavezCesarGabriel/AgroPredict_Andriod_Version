package com.agropredict.presentation.viewmodel.prediction_diagnosis;

import com.agropredict.presentation.user_interface.catalog_input.SoilTypeOption;
import com.agropredict.presentation.user_interface.catalog_input.StageOption;

public interface IPredictionView {
    void notify(String message);
    void onLoading();
    void onIdle();
    void onClassified(String cropName, double confidence);
    void onDiagnosed(String diagnosticIdentifier);
    void onFailed();
    void populate(SoilTypeOption soilTypeOption);
    void populate(StageOption stageOption);
}