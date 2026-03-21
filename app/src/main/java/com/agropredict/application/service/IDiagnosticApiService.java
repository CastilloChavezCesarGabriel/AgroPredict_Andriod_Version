package com.agropredict.application.service;

import com.agropredict.application.request.SubmissionRequest;
import com.agropredict.domain.entity.Diagnostic;

public interface IDiagnosticApiService {
    Diagnostic submit(Diagnostic diagnostic, SubmissionRequest request);
}