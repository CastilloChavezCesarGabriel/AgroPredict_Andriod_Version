package com.agropredict.domain.diagnostic;

import com.agropredict.domain.diagnostic.visitor.IDiagnosticTargetConsumer;

public final class NoDiagnosticTarget implements IDiagnosticTarget {
    @Override
    public void bind(IDiagnosticTargetConsumer consumer) {}
}
