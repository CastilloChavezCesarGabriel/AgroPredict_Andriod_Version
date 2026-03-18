package com.agropredict.presentation.user_interface;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.agropredict.AgroPredictApplication;
import com.agropredict.R;
import com.agropredict.application.usecase.diagnostic.LoadDiagnosticDetailUseCase;
import com.agropredict.presentation.viewmodel.field.FieldDetailViewModel;
import com.agropredict.presentation.viewmodel.field.IFieldDetailView;
import java.util.Map;

public final class FieldDetailActivity extends BaseActivity implements IFieldDetailView {

    private FieldDetailViewModel viewModel;
    private TextView cropNameLabel;
    private TextView soilTypeLabel;
    private TextView stageLabel;
    private TextView severityLabel;
    private TextView summaryLabel;
    private TextView recommendationsLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_detail);
        compose();
        bind();
        load();
    }

    private void compose() {
        AgroPredictApplication application = (AgroPredictApplication) getApplication();
        application.provide(factory -> {
            LoadDiagnosticDetailUseCase loadDetail = new LoadDiagnosticDetailUseCase(factory.createDiagnosticRepository());
            viewModel = new FieldDetailViewModel(loadDetail);
            viewModel.bind(this);
        });
    }

    private void bind() {
        cropNameLabel = findViewById(R.id.tvCropType);
        soilTypeLabel = findViewById(R.id.tvSoilType);
        stageLabel = findViewById(R.id.tvStage);
        severityLabel = findViewById(R.id.tvSeverity);
        summaryLabel = findViewById(R.id.tvRecommendations);
        recommendationsLabel = findViewById(R.id.tvRecommendations);
        findViewById(R.id.btnEditField).setOnClickListener(clickedView -> onEditClicked());
    }

    private void load() {
        String diagnosticIdentifier = getIntent().getStringExtra("diagnostic_identifier");
        if (diagnosticIdentifier != null) viewModel.load(diagnosticIdentifier);
    }

    private void onEditClicked() {
        String cropIdentifier = getIntent().getStringExtra("diagnostic_identifier");
        if (cropIdentifier != null) navigateToEdit(cropIdentifier);
    }

    @Override
    public void display(Map<String, Object> fieldDetail) {
        cropNameLabel.setText(String.valueOf(fieldDetail.get("crop_name")));
        soilTypeLabel.setText(String.valueOf(fieldDetail.get("soil_type")));
        stageLabel.setText(String.valueOf(fieldDetail.get("stage")));
        severityLabel.setText(String.valueOf(fieldDetail.get("severity")));
        summaryLabel.setText(String.valueOf(fieldDetail.get("summary")));
        recommendationsLabel.setText(String.valueOf(fieldDetail.get("recommendations")));
    }

    @Override
    public void navigateToEdit(String cropIdentifier) {
        Intent intent = new Intent(this, EditFieldActivity.class);
        intent.putExtra("crop_identifier", cropIdentifier);
        startActivity(intent);
    }
}
