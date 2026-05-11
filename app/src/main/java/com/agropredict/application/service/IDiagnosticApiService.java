package com.agropredict.application.service;

import com.agropredict.application.diagnostic_submission.request.SubmissionRequest;
import com.agropredict.domain.diagnostic.Diagnostic;

public interface IDiagnosticApiService {
    Diagnostic submit(Diagnostic diagnostic, SubmissionRequest request);
}