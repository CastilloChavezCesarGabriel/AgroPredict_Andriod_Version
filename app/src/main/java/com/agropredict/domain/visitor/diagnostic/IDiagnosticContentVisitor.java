package com.agropredict.domain.visitor.diagnostic;

import com.agropredict.domain.component.diagnostic.DiagnosticAssessment;
import com.agropredict.domain.component.diagnostic.DiagnosticConditions;

public interface IDiagnosticContentVisitor {
    void visit(DiagnosticConditions conditions, DiagnosticAssessment assessment);
}