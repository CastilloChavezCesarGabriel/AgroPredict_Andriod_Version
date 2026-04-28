package com.agropredict.presentation.viewmodel.crop_management;

import com.agropredict.application.usecase.diagnostic.FindDiagnosticUseCase;
import com.agropredict.domain.component.diagnostic.ISeverityVisitor;
import com.agropredict.domain.entity.Diagnostic;

public final class FieldDetailViewModel implements ISeverityVisitor {
    private static final int SEVERE_URGENCY = 2;

    private final FindDiagnosticUseCase loadDetailUseCase;
    private final IFieldDetailView view;

    public FieldDetailViewModel(FindDiagnosticUseCase loadDetailUseCase, IFieldDetailView view) {
        this.loadDetailUseCase = loadDetailUseCase;
        this.view = view;
    }

    public void load(String diagnosticIdentifier) {
        Diagnostic diagnostic = loadDetailUseCase.find(diagnosticIdentifier);
        if (diagnostic != null) {
            view.display(diagnostic);
            diagnostic.inspect(this);
        }
    }

    @Override
    public void visit(String name, int urgency) {
        if (urgency >= SEVERE_URGENCY) view.warn();
    }
}