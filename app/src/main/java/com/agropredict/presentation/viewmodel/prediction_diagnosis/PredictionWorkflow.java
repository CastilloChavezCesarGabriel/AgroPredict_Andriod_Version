package com.agropredict.presentation.viewmodel.prediction_diagnosis;

import com.agropredict.application.operation_result.OperationResult;
import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;
import com.agropredict.application.usecase.diagnostic.ClassifyImageUseCase;
import com.agropredict.application.usecase.diagnostic.SubmitDiagnosticUseCase;
import com.agropredict.application.visitor.IClassificationResultVisitor;

public final class PredictionWorkflow {
    private final ClassifyImageUseCase classifyUseCase;
    private final SubmitDiagnosticUseCase submitUseCase;

    public PredictionWorkflow(ClassifyImageUseCase classifyUseCase, SubmitDiagnosticUseCase submitUseCase) {
        this.classifyUseCase = classifyUseCase;
        this.submitUseCase = submitUseCase;
    }

    public void classify(String imagePath, IClassificationResultVisitor consumer) {
        classifyUseCase.classify(imagePath, consumer);
    }

    public OperationResult submit(SubmissionRequest request) {
        return submitUseCase.submit(request);
    }
}
