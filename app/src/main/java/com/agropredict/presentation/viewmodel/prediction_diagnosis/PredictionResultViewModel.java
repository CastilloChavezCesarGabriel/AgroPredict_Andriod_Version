package com.agropredict.presentation.viewmodel.prediction_diagnosis;

import com.agropredict.application.usecase.diagnostic.FindDiagnosticUseCase;
import com.agropredict.domain.entity.Diagnostic;

public final class PredictionResultViewModel {
    private final FindDiagnosticUseCase loadDetailUseCase;
    private final IPredictionResultView view;

    public PredictionResultViewModel(FindDiagnosticUseCase loadDetailUseCase, IPredictionResultView view) {
        this.loadDetailUseCase = loadDetailUseCase;
        this.view = view;
    }

    public void load(String diagnosticIdentifier) {
        Diagnostic diagnostic = loadDetailUseCase.find(diagnosticIdentifier);
        if (diagnostic == null) return;
        view.display(diagnostic);
        if (!diagnostic.isConfident()) {
            view.warn();
        }
    }
}