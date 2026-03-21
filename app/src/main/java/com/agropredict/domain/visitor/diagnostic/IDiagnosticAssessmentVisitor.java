package com.agropredict.domain.visitor.diagnostic;

import com.agropredict.domain.component.diagnostic.DiagnosticOwnership;
import com.agropredict.domain.component.diagnostic.DiagnosticSummary;

public interface IDiagnosticAssessmentVisitor {
    void visit(DiagnosticSummary summary, DiagnosticOwnership ownership);
}