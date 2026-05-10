package com.agropredict.application.repository;

import com.agropredict.domain.diagnostic.Diagnostic;
import java.util.List;

public interface IDiagnosticRepository {
    void store(Diagnostic diagnostic);
    List<Diagnostic> list(String userIdentifier);
    List<Diagnostic> filter(String userIdentifier, String cropIdentifier);
    Diagnostic find(String diagnosticIdentifier);
    Diagnostic resolve(String userIdentifier, String cropIdentifier);
}
