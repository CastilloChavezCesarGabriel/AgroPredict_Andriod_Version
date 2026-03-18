package com.agropredict.application.service;

import com.agropredict.domain.entity.Diagnostic;

public interface IDiagnosticApiService {
    Diagnostic submit(Diagnostic diagnostic);
}