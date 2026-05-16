package com.agropredict.presentation.user_interface.screen;

import com.agropredict.application.diagnostic_submission.request.ImagePrediction;
import com.agropredict.presentation.viewmodel.prediction_diagnosis.IDiagnosisSubmitter;
import com.agropredict.presentation.viewmodel.prediction_diagnosis.IPendingDiagnosisOutcome;

public interface IPendingDiagnosis {
    IPendingDiagnosis capture(String path);
    IPendingDiagnosis classify(ImagePrediction classification);
    void submit(IDiagnosisSubmitter submitter, IPendingDiagnosisOutcome outcome);
}
