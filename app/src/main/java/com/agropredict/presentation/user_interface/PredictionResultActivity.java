package com.agropredict.presentation.user_interface;

import android.os.Bundle;
import android.widget.TextView;
import com.agropredict.AgroPredictApplication;
import com.agropredict.R;
import com.agropredict.application.usecase.diagnostic.LoadDiagnosticDetailUseCase;
import com.agropredict.presentation.viewmodel.prediction.IPredictionResultView;
import com.agropredict.presentation.viewmodel.prediction.PredictionResultViewModel;
import java.util.Map;

public final class PredictionResultActivity extends BaseActivity implements IPredictionResultView {
    private PredictionResultViewModel viewModel;
    private TextView cropNameLabel;
    private TextView confidenceLabel;
    private TextView severityLabel;
    private TextView summaryLabel;
    private TextView recommendationsLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction_result);
        compose();
        bind();
        load();
    }

    private void compose() {
        AgroPredictApplication application = (AgroPredictApplication) getApplication();
        application.provide(factory -> {
            LoadDiagnosticDetailUseCase loadDetail = new LoadDiagnosticDetailUseCase(factory.createDiagnosticRepository());
            viewModel = new PredictionResultViewModel(loadDetail);
            viewModel.bind(this);
        });
    }

    private void bind() {
        cropNameLabel = findViewById(R.id.tvCropName);
        confidenceLabel = findViewById(R.id.tvConfidence);
        severityLabel = findViewById(R.id.tvSeverity);
        summaryLabel = findViewById(R.id.tvSummary);
        recommendationsLabel = findViewById(R.id.tvRecommendations);
        findViewById(R.id.btnBackToHome).setOnClickListener(clickedView -> navigateToHome());
    }

    private void load() {
        String diagnosticIdentifier = getIntent().getStringExtra("diagnostic_identifier");
        if (diagnosticIdentifier != null) viewModel.load(diagnosticIdentifier);
    }

    @Override
    public void display(Map<String, Object> diagnosticDetail) {
        cropNameLabel.setText(String.valueOf(diagnosticDetail.get("crop_name")));
        confidenceLabel.setText(String.valueOf(diagnosticDetail.get("confidence")));
        severityLabel.setText(String.valueOf(diagnosticDetail.get("severity")));
        summaryLabel.setText(String.valueOf(diagnosticDetail.get("summary")));
        recommendationsLabel.setText(String.valueOf(diagnosticDetail.get("recommendations")));
    }

    @Override
    public void warnLowConfidence() {
        notify("Confianza baja en la prediccion");
    }

    @Override
    public void navigateToHome() {
        redirect(HomeActivity.class);
    }
}
