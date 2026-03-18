package com.agropredict.domain.visitor;

import com.agropredict.domain.value.diagnostic.DiagnosticData;

public interface IDiagnosticVisitor {
    void visit(String identifier, DiagnosticData data);
}