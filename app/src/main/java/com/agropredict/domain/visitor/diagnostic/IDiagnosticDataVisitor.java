package com.agropredict.domain.visitor.diagnostic;

import com.agropredict.domain.component.diagnostic.DiagnosticContent;
import com.agropredict.domain.component.diagnostic.Prediction;

public interface IDiagnosticDataVisitor {
    void visit(Prediction prediction, DiagnosticContent content);
}
