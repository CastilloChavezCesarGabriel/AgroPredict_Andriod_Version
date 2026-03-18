package com.agropredict.presentation.viewmodel.field;

import com.agropredict.application.usecase.diagnostic.LoadDiagnosticDetailUseCase;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.presentation.mapping.DiagnosticMapping;
import java.util.Map;

public final class FieldDetailViewModel {

    private final LoadDiagnosticDetailUseCase loadDetailUseCase;
    private IFieldDetailView view;

    public FieldDetailViewModel(LoadDiagnosticDetailUseCase loadDetailUseCase) {
        this.loadDetailUseCase = loadDetailUseCase;
    }

    public void bind(IFieldDetailView view) {
        this.view = view;
    }

    public void load(String diagnosticIdentifier) {
        Diagnostic diagnostic = loadDetailUseCase.load(diagnosticIdentifier);
        if (view != null && diagnostic != null) {
            view.display(new DiagnosticMapping().map(diagnostic));
        }
    }
}
