package com.agropredict.domain.visitor.diagnostic;

import com.agropredict.domain.component.diagnostic.DiagnosticData;

public interface IDiagnosticVisitor {
    void visit(String identifier, DiagnosticData data);
}