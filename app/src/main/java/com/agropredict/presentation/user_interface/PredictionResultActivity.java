package com.agropredict.presentation.user_interface;

import android.os.Bundle;
import com.agropredict.AgroPredictApplication;
import com.agropredict.R;
import com.agropredict.application.usecase.diagnostic.FindDiagnosticUseCase;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.presentation.user_interface.holder.PredictionResultViewHolder;
import com.agropredict.presentation.viewmodel.prediction.IPredictionResultView;
import com.agropredict.presentation.viewmodel.prediction.PredictionResultViewModel;

public final class PredictionResultActivity extends BaseActivity implements IPredictionResultView {
    private PredictionResultViewModel viewModel;
    private PredictionResultViewHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction_result);
        findViewById(R.id.btnBackToHome).setOnClickListener(view -> redirect(HomeActivity.class));
        ((AgroPredictApplication) getApplication()).provide(factory -> {
            holder = new PredictionResultViewHolder(this, factory.createCropImageRepository());
            FindDiagnosticUseCase useCase = new FindDiagnosticUseCase(factory.createDiagnosticRepository());
            viewModel = new PredictionResultViewModel(useCase, this);
        });
        String identifier = getIntent().getStringExtra("diagnostic_identifier");
        if (identifier != null) viewModel.load(identifier);
    }

    @Override
    public void display(Diagnostic diagnostic) {
        holder.display(diagnostic);
    }

    @Override
    public void warn() {
        notify(getString(R.string.classification_low_confidence));
    }
}
