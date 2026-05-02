package com.agropredict.presentation.viewmodel.crop_management;

import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.domain.entity.Photograph;

public interface IFieldDetailView {
    void display(Diagnostic diagnostic);
    void display(Photograph photograph);
    void warn();
    void navigate(String cropIdentifier);
}