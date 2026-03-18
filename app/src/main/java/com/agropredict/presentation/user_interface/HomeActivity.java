package com.agropredict.presentation.user_interface;

import android.os.Bundle;
import com.agropredict.AgroPredictApplication;
import com.agropredict.R;
import com.agropredict.application.usecase.authentication.LogoutUseCase;
import com.agropredict.presentation.viewmodel.home.HomeViewModel;
import com.agropredict.presentation.viewmodel.home.IHomeView;

public final class HomeActivity extends BaseActivity implements IHomeView {

    private HomeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        compose();
        bind();
    }

    private void compose() {
        AgroPredictApplication application = (AgroPredictApplication) getApplication();
        application.provide(factory -> {
            LogoutUseCase logoutUseCase = new LogoutUseCase(factory.createSessionRepository());
            viewModel = new HomeViewModel(logoutUseCase);
            viewModel.bind(this);
        });
    }

    private void bind() {
        findViewById(R.id.cardNewPrediction).setOnClickListener(clickedView -> navigateToPrediction());
        findViewById(R.id.cardHistory).setOnClickListener(clickedView -> navigateToHistory());
        findViewById(R.id.cardReport).setOnClickListener(clickedView -> navigateToReport());
        findViewById(R.id.btnLogout).setOnClickListener(clickedView -> viewModel.logout());
        findViewById(R.id.fabAddField).setOnClickListener(clickedView -> navigateToPrediction());
    }

    @Override
    public void navigateToPrediction() {
        navigate(PredictionActivity.class);
    }

    @Override
    public void navigateToHistory() {
        navigate(HistoryActivity.class);
    }

    @Override
    public void navigateToReport() {
        navigate(ReportActivity.class);
    }

    @Override
    public void navigateToLogin() {
        redirect(LoginActivity.class);
    }
}
