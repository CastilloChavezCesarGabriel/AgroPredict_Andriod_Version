package com.agropredict.presentation.viewmodel.prediction_diagnosis;

import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.domain.photograph.Photograph;

public interface IPredictionResultView {
    void display(Diagnostic diagnostic);
    void display(Photograph photograph);
    void warn();
}