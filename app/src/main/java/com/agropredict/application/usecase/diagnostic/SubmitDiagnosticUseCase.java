package com.agropredict.application.usecase.diagnostic;

import com.agropredict.application.repository.IDiagnosticWorkflow;
import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;
import com.agropredict.application.operation_result.OperationResult;
import com.agropredict.application.service.IDiagnosticApiService;

public final class SubmitDiagnosticUseCase {
    private final IDiagnosticApiService apiService;
    private final IDiagnosticWorkflow workflow;

    public SubmitDiagnosticUseCase(IDiagnosticApiService apiService, IDiagnosticWorkflow workflow) {
        this.apiService = apiService;
        this.workflow = workflow;
    }

    public OperationResult submit(SubmissionRequest request) {
        try {
            String identifier = request.submit(apiService, workflow);
            return OperationResult.succeed(identifier);
        } catch (RuntimeException exception) {
            return OperationResult.fail();
        }
    }
}