package com.agropredict.application.repository;

import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;
import com.agropredict.domain.entity.Diagnostic;

public interface IDiagnosticWorkflow {
    void persist(SubmissionRequest request, Diagnostic diagnostic, String identifier);
}