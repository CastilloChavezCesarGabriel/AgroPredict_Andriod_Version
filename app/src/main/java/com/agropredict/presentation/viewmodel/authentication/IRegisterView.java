package com.agropredict.presentation.viewmodel.authentication;

import java.util.List;

public interface IRegisterView {
    void notify(String message);
    void navigateToLogin();
    void populateOccupations(List<String> occupations);
}