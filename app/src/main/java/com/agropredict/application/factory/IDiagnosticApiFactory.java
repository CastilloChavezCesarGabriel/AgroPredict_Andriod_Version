package com.agropredict.application.factory;

import com.agropredict.application.service.IDiagnosticApiService;

public interface IDiagnosticApiFactory {
    IDiagnosticApiService createApiService();
}