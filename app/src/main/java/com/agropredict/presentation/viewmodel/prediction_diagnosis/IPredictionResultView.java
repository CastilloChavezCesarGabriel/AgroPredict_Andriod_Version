package com.agropredict.presentation.viewmodel.prediction_diagnosis;

import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.domain.photograph.Photograph;

public interface IPredictionResultView {
    void present(Diagnostic diagnostic);
    void show(Photograph photograph);
    void warn();
}