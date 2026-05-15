package com.agropredict.domain.user.occupation;

public interface IOccupationListener {
    void onElevate(String label);
    void onLimit(String label);
}