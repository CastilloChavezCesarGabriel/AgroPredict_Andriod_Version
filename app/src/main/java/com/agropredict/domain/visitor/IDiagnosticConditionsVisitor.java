package com.agropredict.domain.visitor;

import com.agropredict.domain.value.diagnostic.DiagnosticContext;
import com.agropredict.domain.value.diagnostic.DiagnosticEnvironment;

public interface IDiagnosticConditionsVisitor {

    void visit(DiagnosticContext context, DiagnosticEnvironment environment);
}
