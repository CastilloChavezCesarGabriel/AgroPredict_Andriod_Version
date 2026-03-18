package com.agropredict.presentation.viewmodel.field;

import java.util.Map;

public interface IFieldDetailView {
    void display(Map<String, Object> fieldDetail);
    void navigateToEdit(String cropIdentifier);
}