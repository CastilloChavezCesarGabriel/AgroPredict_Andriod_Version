package com.agropredict.presentation.viewmodel.prediction;

import com.agropredict.domain.entity.Diagnostic;

public interface IPredictionResultView {
    void display(Diagnostic diagnostic);
    void warn();
}