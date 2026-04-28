package com.agropredict.presentation.viewmodel.authentication;

public interface IRecoveryView {
    void dismiss();
    void notify(String message);
    void confirm();
    void warn();
}