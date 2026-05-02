package com.agropredict.presentation.viewmodel.prediction_diagnosis;

import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.domain.entity.Photograph;

public interface IPredictionResultView {
    void display(Diagnostic diagnostic);
    void display(Photograph photograph);
    void warn();
}