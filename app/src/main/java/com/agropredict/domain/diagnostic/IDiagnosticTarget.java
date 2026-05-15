package com.agropredict.domain.diagnostic;

import com.agropredict.domain.diagnostic.visitor.IDiagnosticTargetConsumer;

public interface IDiagnosticTarget {
    void bind(IDiagnosticTargetConsumer consumer);
}
