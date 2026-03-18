package com.agropredict.domain.visitor;

import com.agropredict.domain.value.diagnostic.DiagnosticOwnership;
import com.agropredict.domain.value.diagnostic.DiagnosticSummary;

public interface IDiagnosticAssessmentVisitor {
    void visit(DiagnosticSummary summary, DiagnosticOwnership ownership);
}