package com.agropredict.presentation.user_interface.screen;

import com.agropredict.R;
import com.agropredict.application.diagnostic_submission.request.ImagePrediction;
import com.agropredict.presentation.viewmodel.prediction_diagnosis.IDiagnosisSubmitter;
import com.agropredict.presentation.viewmodel.prediction_diagnosis.IPendingDiagnosisOutcome;

public final class EmptyPendingDiagnosis implements IPendingDiagnosis {
    @Override
    public IPendingDiagnosis capture(String path) {
        return new CapturedPendingDiagnosis(path);
    }

    @Override
    public IPendingDiagnosis classify(ImagePrediction classification) {
        return this;
    }

    @Override
    public void submit(IDiagnosisSubmitter submitter, IPendingDiagnosisOutcome outcome) {
        outcome.warn(R.string.image_invalid);
    }
}
