package com.agropredict.domain.visitor.diagnostic;

import com.agropredict.domain.component.diagnostic.DiagnosticContext;
import com.agropredict.domain.component.diagnostic.DiagnosticEnvironment;

public interface IDiagnosticConditionsVisitor {
    void visit(DiagnosticContext context, DiagnosticEnvironment environment);
}
