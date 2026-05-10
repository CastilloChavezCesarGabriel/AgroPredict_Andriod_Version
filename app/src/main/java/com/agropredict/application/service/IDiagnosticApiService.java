package com.agropredict.application.service;

import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;
import com.agropredict.domain.diagnostic.Diagnostic;

public interface IDiagnosticApiService {
    Diagnostic submit(Diagnostic diagnostic, SubmissionRequest request);
}