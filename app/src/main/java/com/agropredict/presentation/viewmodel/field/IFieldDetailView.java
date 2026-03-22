package com.agropredict.presentation.viewmodel.field;

import com.agropredict.domain.entity.Diagnostic;

public interface IFieldDetailView {
    void display(Diagnostic diagnostic);
    void warn();
    void navigate(String cropIdentifier);
}