package com.agropredict.presentation.user_interface;

import androidx.activity.OnBackPressedCallback;

public final class BackNavigationGuard extends OnBackPressedCallback {
    public BackNavigationGuard() {
        super(true);
    }

    @Override
    public void handleOnBackPressed() {}
}