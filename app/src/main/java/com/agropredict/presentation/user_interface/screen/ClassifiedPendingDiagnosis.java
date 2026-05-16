package com.agropredict.presentation.user_interface.screen;

import com.agropredict.application.diagnostic_submission.request.ImagePrediction;
import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.presentation.viewmodel.prediction_diagnosis.IDiagnosisSubmitter;
import com.agropredict.presentation.viewmodel.prediction_diagnosis.IPendingDiagnosisOutcome;
import java.util.Objects;

public final class ClassifiedPendingDiagnosis implements IPendingDiagnosis {
    private final String path;
    private final ImagePrediction classification;

    public ClassifiedPendingDiagnosis(String path, ImagePrediction classification) {
        this.path = ArgumentPrecondition.validate(path, "classified pending diagnosis path");
        this.classification = Objects.requireNonNull(classification, "classified pending diagnosis requires a classification");
    }

    @Override
    public IPendingDiagnosis capture(String newPath) {
        return new CapturedPendingDiagnosis(newPath);
    }

    @Override
    public IPendingDiagnosis classify(ImagePrediction newClassification) {
        return new ClassifiedPendingDiagnosis(path, newClassification);
    }

    @Override
    public void submit(IDiagnosisSubmitter submitter, IPendingDiagnosisOutcome outcome) {
        submitter.submit(path, classification);
    }
}
