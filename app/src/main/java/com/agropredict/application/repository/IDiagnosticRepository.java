package com.agropredict.application.repository;

import com.agropredict.domain.entity.Diagnostic;
import java.util.List;

public interface IDiagnosticRepository {
    void store(Diagnostic diagnostic);
    void delete(String diagnosticIdentifier);
    List<Diagnostic> list(String userIdentifier);
    Diagnostic load(String diagnosticIdentifier);
}