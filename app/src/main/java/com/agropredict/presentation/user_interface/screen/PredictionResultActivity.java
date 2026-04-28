package com.agropredict.presentation.user_interface.screen;

import android.os.Bundle;
import com.agropredict.R;
import com.agropredict.application.factory.IReviewFactory;
import com.agropredict.application.usecase.diagnostic.FindDiagnosticUseCase;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.presentation.user_interface.display.PredictionResult;
import com.agropredict.presentation.viewmodel.prediction_diagnosis.IPredictionResultView;
import com.agropredict.presentation.viewmodel.prediction_diagnosis.PredictionResultViewModel;

public final class PredictionResultActivity extends BaseActivity implements IPredictionResultView {
    private PredictionResultViewModel viewModel;
    private PredictionResult predictionResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction_result);
        bind();
        initialize();
        listen();
        load();
    }

    private void bind() {
        predictionResult = new PredictionResult(this);
    }

    private void initialize() {
        IReviewFactory factory = (IReviewFactory) getApplication();
        FindDiagnosticUseCase useCase = new FindDiagnosticUseCase(factory.createDiagnosticRepository());
        viewModel = new PredictionResultViewModel(useCase, this);
    }

    private void listen() {
        findViewById(R.id.btnBackToHome).setOnClickListener(view -> redirect(HomeActivity.class));
    }

    private void load() {
        String identifier = IntentExtra.DIAGNOSTIC_IDENTIFIER.read(getIntent());
        if (identifier != null) viewModel.load(identifier);
    }

    @Override
    public void display(Diagnostic diagnostic) {
        predictionResult.display(diagnostic);
    }

    @Override
    public void warn() {
        notify(getString(R.string.classification_low_confidence));
    }
}