package com.agropredict.presentation.user_interface.screen;

import com.agropredict.R;
import com.agropredict.application.diagnostic_submission.request.ImagePrediction;
import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.presentation.viewmodel.prediction_diagnosis.IDiagnosisSubmitter;
import com.agropredict.presentation.viewmodel.prediction_diagnosis.IPendingDiagnosisOutcome;

public final class CapturedPendingDiagnosis implements IPendingDiagnosis {
    private final String path;

    public CapturedPendingDiagnosis(String path) {
        this.path = ArgumentPrecondition.validate(path, "captured pending diagnosis path");
    }

    @Override
    public IPendingDiagnosis capture(String newPath) {
        return new CapturedPendingDiagnosis(newPath);
    }

    @Override
    public IPendingDiagnosis classify(ImagePrediction classification) {
        return new ClassifiedPendingDiagnosis(path, classification);
    }

    @Override
    public void submit(IDiagnosisSubmitter submitter, IPendingDiagnosisOutcome outcome) {
        outcome.warn(R.string.classification_low_confidence);
    }
}
