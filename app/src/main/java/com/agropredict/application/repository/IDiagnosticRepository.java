package com.agropredict.application.repository;

import com.agropredict.domain.entity.Diagnostic;
import java.util.List;

public interface IDiagnosticRepository {
    void store(Diagnostic diagnostic);
    void delete(String diagnosticIdentifier);
    List<Diagnostic> list(String userIdentifier);
    List<Diagnostic> filter(String userIdentifier, String cropIdentifier);
    Diagnostic find(String diagnosticIdentifier);
    Diagnostic resolve(String userIdentifier, String cropIdentifier);
}