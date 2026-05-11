package com.agropredict.presentation.user_interface.screen;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.agropredict.R;
import com.agropredict.application.factory.IAccessFactory;
import com.agropredict.application.factory.IDashboardFactory;
import com.agropredict.application.service.IAssetService;
import com.agropredict.application.authentication.usecase.CheckSessionUseCase;
import com.agropredict.application.authentication.usecase.LogoutUseCase;
import com.agropredict.application.profile.GreetUserUseCase;
import com.agropredict.domain.authentication.Session;
import com.agropredict.domain.user.visitor.IUserIdentityConsumer;
import com.agropredict.presentation.user_interface.navigation.BackNavigationGuard;
import com.agropredict.presentation.user_interface.navigation.PdfLauncher;
import com.agropredict.presentation.viewmodel.home_dashboard.HomeViewModel;
import com.agropredict.presentation.viewmodel.home_dashboard.IHomeView;
import java.io.IOException;

public final class HomeActivity extends BaseActivity implements IHomeView, IUserIdentityConsumer {
    private static final String PEST_GUIDE_PATH = "pdf/manual_sostenible.pdf";
    private HomeViewModel viewModel;
    private IAssetService assetService;
    private GreetUserUseCase greetUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initialize();
        listen();
    }

    private void initialize() {
        IDashboardFactory factory = (IDashboardFactory) getApplication();
        IAccessFactory accessFactory = (IAccessFactory) getApplication();
        LogoutUseCase logoutUseCase = new LogoutUseCase(factory.createSessionRepository());
        viewModel = new HomeViewModel(logoutUseCase, this);
        assetService = factory.createAssetService();
        greetUseCase = new GreetUserUseCase(accessFactory.createUserRepository());
        new CheckSessionUseCase(factory.createSessionRepository()).check(this::restrict);
    }

    private void listen() {
        findViewById(R.id.cardNewPrediction).setOnClickListener(view -> predict());
        findViewById(R.id.cardHistory).setOnClickListener(view -> review());
        findViewById(R.id.cardReport).setOnClickListener(view -> report());
        findViewById(R.id.cardPestGuide).setOnClickListener(view -> guide());
        findViewById(R.id.btnLogout).setOnClickListener(view -> confirm());
        findViewById(R.id.btnProfile).setOnClickListener(view -> openProfile());
        findViewById(R.id.fabAddField).setOnClickListener(view -> predict());
        findViewById(R.id.cardResources).setOnClickListener(view -> browse("https://www.fao.org/agriculture/es"));
        findViewById(R.id.cardManual).setOnClickListener(view -> browse("https://www.inia.gob.pe"));
        getOnBackPressedDispatcher().addCallback(this, new BackNavigationGuard());
    }

    private void openProfile() {
        navigate(ProfileActivity.class);
    }

    private void restrict(String identifier, String occupation) {
        if (identifier == null || identifier.isEmpty()) {
            redirect(LoginActivity.class);
            return;
        }
        Session session = new Session(identifier, occupation);
        View reportCard = findViewById(R.id.cardReport);
        reportCard.setVisibility(View.GONE);
        session.observe(new AdvancedReportCardReveal(reportCard));
        greetUseCase.greet(identifier, this);
    }

    private void guide() {
        try {
            String filePath = assetService.extract(PEST_GUIDE_PATH);
            PdfLauncher.open(this, filePath);
        } catch (IOException exception) {
            Toast.makeText(this, R.string.error_general, Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void describe(String identifier, String fullName) {
        TextView welcome = findViewById(R.id.tvWelcome);
        welcome.setText(getString(R.string.welcome_user, fullName));
        welcome.setVisibility(View.VISIBLE);
    }
}