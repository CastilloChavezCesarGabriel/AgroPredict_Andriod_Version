package com.agropredict.presentation.viewmodel.prediction_diagnosis;

import com.agropredict.presentation.user_interface.catalog_input.SoilTypeCatalog;
import com.agropredict.presentation.user_interface.catalog_input.StageCatalog;

public interface IPredictionView {
    void notify(String message);
    void load();
    void idle();
    void classify(String cropName, String confidence);
    void reveal(String diagnosticIdentifier);
    void populate(SoilTypeCatalog soilTypeOption);
    void populate(StageCatalog stageOption);
}