package com.agropredict.domain.user;

public interface IOccupationListener {
    void onElevate(String label);
    void onLimit(String label);
}