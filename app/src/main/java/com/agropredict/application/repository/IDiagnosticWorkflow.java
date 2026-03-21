package com.agropredict.application.repository;

import com.agropredict.application.request.SubmissionRequest;
import com.agropredict.domain.entity.Diagnostic;

public interface IDiagnosticWorkflow {
    String persist(SubmissionRequest request, Diagnostic diagnostic);
}
