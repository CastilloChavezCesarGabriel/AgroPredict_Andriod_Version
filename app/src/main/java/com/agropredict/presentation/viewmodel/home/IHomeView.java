package com.agropredict.presentation.viewmodel.home;

public interface IHomeView {
    void notify(String message);
    void navigateToPrediction();
    void navigateToHistory();
    void navigateToReport();
    void navigateToLogin();
}