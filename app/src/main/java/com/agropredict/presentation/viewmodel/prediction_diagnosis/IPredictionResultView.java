package com.agropredict.presentation.viewmodel.prediction_diagnosis;

import com.agropredict.domain.entity.Diagnostic;

public interface IPredictionResultView {
    void display(Diagnostic diagnostic);
    void warn();
}