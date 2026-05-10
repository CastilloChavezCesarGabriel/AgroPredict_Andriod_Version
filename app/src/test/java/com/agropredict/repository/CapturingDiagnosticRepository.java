package com.agropredict.repository;

import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.application.repository.IRecordEraser;
import com.agropredict.domain.diagnostic.Diagnostic;
import java.util.Collections;
import java.util.List;

public final class CapturingDiagnosticRepository implements IDiagnosticRepository, IRecordEraser {
    @Override public void store(Diagnostic diagnostic) {}
    @Override public List<Diagnostic> list(String userIdentifier) { return Collections.emptyList(); }
    @Override public List<Diagnostic> filter(String userIdentifier, String cropIdentifier) { return Collections.emptyList(); }
    @Override public Diagnostic find(String diagnosticIdentifier) { return null; }
    @Override public Diagnostic resolve(String userIdentifier, String cropIdentifier) { return null; }
    @Override public void erase(String diagnosticIdentifier) {}
}