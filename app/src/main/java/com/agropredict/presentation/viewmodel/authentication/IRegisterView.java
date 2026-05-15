package com.agropredict.presentation.viewmodel.authentication;

import java.util.List;

public interface IRegisterView {
    void notify(String message);
    void dismiss();
    void confirm(String username);
    void populate(List<String> occupations);
}