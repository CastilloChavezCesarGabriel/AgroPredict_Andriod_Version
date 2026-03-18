package com.agropredict.domain.visitor;

import com.agropredict.domain.value.diagnostic.DiagnosticContent;
import com.agropredict.domain.value.diagnostic.Prediction;

public interface IDiagnosticDataVisitor {

    void visit(Prediction prediction, DiagnosticContent content);
}
