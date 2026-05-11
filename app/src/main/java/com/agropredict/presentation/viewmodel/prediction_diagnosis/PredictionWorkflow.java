package com.agropredict.presentation.viewmodel.prediction_diagnosis;

import com.agropredict.application.operation_result.IUseCaseResult;
import com.agropredict.application.diagnostic_submission.request.SubmissionRequest;
import com.agropredict.application.diagnostic_submission.usecase.ClassifyImageUseCase;
import com.agropredict.application.diagnostic_submission.usecase.SubmitDiagnosticUseCase;
import com.agropredict.domain.diagnostic.visitor.IClassificationResult;

public final class PredictionWorkflow {
    private final ClassifyImageUseCase classifyUseCase;
    private final SubmitDiagnosticUseCase submitUseCase;

    public PredictionWorkflow(ClassifyImageUseCase classifyUseCase, SubmitDiagnosticUseCase submitUseCase) {
        this.classifyUseCase = classifyUseCase;
        this.submitUseCase = submitUseCase;
    }

    public void classify(String imagePath, IClassificationResult visitor) {
        classifyUseCase.classify(imagePath, visitor);
    }

    public IUseCaseResult submit(SubmissionRequest request) {
        return submitUseCase.submit(request);
    }
}