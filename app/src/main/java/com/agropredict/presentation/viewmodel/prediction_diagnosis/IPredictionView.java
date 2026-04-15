package com.agropredict.presentation.viewmodel.prediction_diagnosis;

import com.agropredict.presentation.user_interface.catalog_input.SoilTypeCatalog;
import com.agropredict.presentation.user_interface.catalog_input.StageCatalog;

public interface IPredictionView {
    void notify(String message);
    void onLoading();
    void onIdle();
    void onClassified(String cropName, double confidence);
    void onDiagnosed(String diagnosticIdentifier);
    void populate(SoilTypeCatalog soilTypeOption);
    void populate(StageCatalog stageOption);
}