package com.agropredict.application.diagnostic_submission.usecase;

import com.agropredict.application.diagnostic_submission.workflow.DiagnosticWorkflow;
import com.agropredict.application.diagnostic_submission.request.SubmissionRequest;
import com.agropredict.application.operation_result.IUseCaseResult;
import com.agropredict.application.operation_result.SuccessfulOperation;
import com.agropredict.application.operation_result.FailedOperation;
import com.agropredict.application.service.IDiagnosticApiService;
import java.util.Objects;

public final class SubmitDiagnosticUseCase {
    private final IDiagnosticApiService apiService;
    private final DiagnosticWorkflow workflow;

    public SubmitDiagnosticUseCase(IDiagnosticApiService apiService, DiagnosticWorkflow workflow) {
        this.apiService = Objects.requireNonNull(apiService, "submit diagnostic use case requires an api service");
        this.workflow = Objects.requireNonNull(workflow, "submit diagnostic use case requires a workflow");
    }

    public IUseCaseResult submit(SubmissionRequest request) {
        try {
            String identifier = request.submit(apiService, workflow);
            return new SuccessfulOperation(identifier);
        } catch (RuntimeException exception) {
            return new FailedOperation();
        }
    }
}