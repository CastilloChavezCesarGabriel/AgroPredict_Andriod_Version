package com.agropredict.domain.visitor;

import com.agropredict.domain.value.diagnostic.DiagnosticAssessment;
import com.agropredict.domain.value.diagnostic.DiagnosticConditions;

public interface IDiagnosticContentVisitor {

    void visit(DiagnosticConditions conditions, DiagnosticAssessment assessment);
}
