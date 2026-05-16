package com.agropredict.presentation.user_interface.screen;

import android.os.Bundle;
import com.agropredict.R;
import com.agropredict.application.factory.IReviewFactory;
import com.agropredict.application.photograph.LoadPhotographUseCase;
import com.agropredict.application.diagnostic_history.FindDiagnosticUseCase;
import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.domain.photograph.Photograph;
import com.agropredict.presentation.user_interface.display.PredictionResultDisplay;
import com.agropredict.presentation.viewmodel.prediction_diagnosis.IPredictionResultView;
import com.agropredict.presentation.viewmodel.prediction_diagnosis.PredictionResultViewModel;

public final class PredictionResultActivity extends BaseActivity implements IPredictionResultView {
    private PredictionResultViewModel viewModel;
    private PredictionResultDisplay predictionResult;
    private LoadPhotographUseCase loadPhotograph;

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
        predictionResult = new PredictionResultDisplay(this);
    }

    private void initialize() {
        IReviewFactory factory = (IReviewFactory) getApplication();
        FindDiagnosticUseCase useCase = new FindDiagnosticUseCase(factory.createDiagnosticRepository());
        loadPhotograph = new LoadPhotographUseCase(factory.createPhotographRepository());
        viewModel = new PredictionResultViewModel(useCase, this);
    }

    private void listen() {
        findViewById(R.id.btnBackToHome).setOnClickListener(view -> redirect(HomeActivity.class));
    }

    private void load() {
        String identifier = IntentExtra.DIAGNOSTIC_IDENTIFIER.read(getIntent());
        if (identifier == null) return;
        viewModel.load(identifier);
        Photograph photograph = loadPhotograph.find(identifier);
        if (photograph != null) show(photograph);
    }

    @Override
    public void present(Diagnostic diagnostic) {
        predictionResult.display(diagnostic);
    }

    @Override
    public void show(Photograph photograph) {
        predictionResult.show(photograph);
    }

    @Override
    public void warn() {
        notify(getString(R.string.classification_low_confidence));
    }
}
