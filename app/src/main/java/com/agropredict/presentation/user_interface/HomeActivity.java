package com.agropredict.presentation.user_interface;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
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
        ((AgroPredictApplication) getApplication()).provide(factory -> {
            LogoutUseCase useCase = new LogoutUseCase(factory.createSessionRepository());
            viewModel = new HomeViewModel(useCase, this);
        });
        findViewById(R.id.cardNewPrediction).setOnClickListener(view -> predict());
        findViewById(R.id.cardHistory).setOnClickListener(view -> review());
        findViewById(R.id.cardReport).setOnClickListener(view -> report());
        findViewById(R.id.btnLogout).setOnClickListener(view -> confirm());
        findViewById(R.id.fabAddField).setOnClickListener(view -> predict());
        findViewById(R.id.cardResources).setOnClickListener(view -> browse("https://www.fao.org/agriculture/es"));
        findViewById(R.id.cardManual).setOnClickListener(view -> browse("https://www.inia.gob.pe"));
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }
        });
    }

    private void confirm() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.logout)
                .setMessage(R.string.logout_confirmation)
                .setPositiveButton(R.string.confirm, (dialog, which) -> viewModel.logout())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void browse(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    @Override
    public void predict() {
        navigate(PredictionActivity.class);
    }

    @Override
    public void review() {
        navigate(HistoryActivity.class);
    }

    @Override
    public void report() {
        navigate(ReportActivity.class);
    }

    @Override
    public void logout() {
        redirect(LoginActivity.class);
    }
}