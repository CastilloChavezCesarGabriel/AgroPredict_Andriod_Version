package com.agropredict.presentation.viewmodel.crop_management;

import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.domain.photograph.Photograph;

public interface IFieldDetailView {
    void display(Diagnostic diagnostic);
    void display(Photograph photograph);
    void warn();
    void navigate(String cropIdentifier);
}