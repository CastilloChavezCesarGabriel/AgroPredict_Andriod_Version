package com.agropredict.presentation.viewmodel.prediction_diagnosis;

import com.agropredict.domain.crop.Crop;
import com.agropredict.presentation.user_interface.catalog_input.SoilTypeOption;
import com.agropredict.presentation.user_interface.catalog_input.StageOption;
import java.util.List;

public interface IPredictionView {
    void notify(String message);
    void onLoading();
    void onIdle();
    void onClassified(String cropName, double confidence);
    void onDiagnosed(String diagnosticIdentifier);
    void onFailed();
    void furnish(SoilTypeOption soilTypeOption);
    void arrange(StageOption stageOption);
    void offer(List<Crop> crops);
}
