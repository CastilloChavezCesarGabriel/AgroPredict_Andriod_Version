package com.agropredict.presentation.viewmodel.field;

import com.agropredict.application.usecase.diagnostic.FindDiagnosticUseCase;
import com.agropredict.domain.entity.Diagnostic;

public final class FieldDetailViewModel {

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
            if (diagnostic.isSevere()) {
                view.warn();
            }
        }
    }
}