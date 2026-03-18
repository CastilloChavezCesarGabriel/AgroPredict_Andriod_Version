package com.agropredict.presentation.user_interface;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.agropredict.AgroPredictApplication;
import com.agropredict.R;
import com.agropredict.application.usecase.authentication.CheckSessionUseCase;
import com.agropredict.presentation.viewmodel.authentication.SplashViewModel;

public final class SplashActivity extends BaseActivity {

    private static final int SPLASH_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        compose();
    }

    private void compose() {
        AgroPredictApplication application = (AgroPredictApplication) getApplication();
        application.provide(factory -> {
            CheckSessionUseCase checkSession = new CheckSessionUseCase(factory.createSessionRepository());
            SplashViewModel viewModel = new SplashViewModel(checkSession);
            schedule(viewModel);
        });
    }

    private void schedule(SplashViewModel viewModel) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> viewModel.check(this::visit), SPLASH_DELAY);
    }

    private void visit(boolean hasSession, String userIdentifier) {
        if (hasSession) {
            redirect(HomeActivity.class);
        } else {
            redirect(LoginActivity.class);
        }
    }
}
