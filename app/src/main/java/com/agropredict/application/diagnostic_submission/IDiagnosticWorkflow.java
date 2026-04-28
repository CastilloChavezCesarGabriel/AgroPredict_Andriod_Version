package com.agropredict.application.diagnostic_submission;

import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;
import com.agropredict.domain.entity.Diagnostic;

public interface IDiagnosticWorkflow {
    void persist(SubmissionRequest request, Diagnostic diagnostic, String identifier);
}