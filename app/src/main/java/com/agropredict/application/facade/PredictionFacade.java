package com.agropredict.application.facade;

import com.agropredict.application.visitor.IClassificationResultVisitor;
import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;
import com.agropredict.application.operation_result.OperationResult;
import com.agropredict.application.usecase.diagnostic.ClassifyImageUseCase;
import com.agropredict.application.usecase.diagnostic.SubmitDiagnosticUseCase;

public final class PredictionFacade {
    private final ClassifyImageUseCase classifyUseCase;
    private final SubmitDiagnosticUseCase submitUseCase;

    public PredictionFacade(ClassifyImageUseCase classifyUseCase, SubmitDiagnosticUseCase submitUseCase) {
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