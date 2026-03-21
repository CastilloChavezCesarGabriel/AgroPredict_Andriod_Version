package com.agropredict.application.usecase.diagnostic;

import com.agropredict.application.repository.IDiagnosticWorkflow;
import com.agropredict.application.request.SubmissionRequest;
import com.agropredict.application.result.OperationResult;
import com.agropredict.application.service.IDiagnosticApiService;
import com.agropredict.domain.entity.Diagnostic;

public final class SubmitDiagnosticUseCase {
    private final IDiagnosticApiService apiService;
    private final IDiagnosticWorkflow workflow;

    public SubmitDiagnosticUseCase(IDiagnosticApiService apiService, IDiagnosticWorkflow workflow) {
        this.apiService = apiService;
        this.workflow = workflow;
    }

    public OperationResult submit(SubmissionRequest request) {
        try {
            Diagnostic diagnostic = request.diagnose();
            Diagnostic enriched = apiService.submit(diagnostic, request);
            String identifier = workflow.persist(request, enriched);
            return OperationResult.succeed(identifier);
        } catch (RuntimeException exception) {
            return OperationResult.fail();
        }
    }
}