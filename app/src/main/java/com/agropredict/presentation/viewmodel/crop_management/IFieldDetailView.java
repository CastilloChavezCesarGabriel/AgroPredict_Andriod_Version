package com.agropredict.presentation.viewmodel.crop_management;

import com.agropredict.domain.entity.Diagnostic;

public interface IFieldDetailView {
    void display(Diagnostic diagnostic);
    void warn();
    void navigate(String cropIdentifier);
}