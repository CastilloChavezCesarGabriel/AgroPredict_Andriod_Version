package com.agropredict.presentation.viewmodel.prediction_diagnosis;

import com.agropredict.application.diagnostic_submission.request.ImagePrediction;

public interface IDiagnosisSubmitter {
    void submit(String path, ImagePrediction classification);
}
