package com.agropredict.presentation.viewmodel.prediction;

import com.agropredict.application.usecase.diagnostic.LoadDiagnosticDetailUseCase;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.presentation.mapping.DiagnosticMapping;
import java.util.Map;

public final class PredictionResultViewModel {
    private final LoadDiagnosticDetailUseCase loadDetailUseCase;
    private IPredictionResultView view;

    public PredictionResultViewModel(LoadDiagnosticDetailUseCase loadDetailUseCase) {
        this.loadDetailUseCase = loadDetailUseCase;
    }

    public void bind(IPredictionResultView view) {
        this.view = view;
    }

    public void load(String diagnosticIdentifier) {
        Diagnostic diagnostic = loadDetailUseCase.load(diagnosticIdentifier);
        if (view == null || diagnostic == null) return;
        view.display(new DiagnosticMapping().map(diagnostic));
        if (!diagnostic.isConfident()) {
            view.warnLowConfidence();
        }
    }
}
