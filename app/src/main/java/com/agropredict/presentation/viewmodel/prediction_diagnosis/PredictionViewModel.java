package com.agropredict.presentation.viewmodel.prediction_diagnosis;

import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;
import com.agropredict.application.operation_result.OperationResult;
import com.agropredict.application.usecase.catalog.ListCatalogUseCase;
import com.agropredict.application.usecase.diagnostic.ClassifyImageUseCase;
import com.agropredict.application.usecase.diagnostic.SubmitDiagnosticUseCase;
import com.agropredict.presentation.user_interface.catalog_input.SoilTypeOption;
import com.agropredict.presentation.user_interface.catalog_input.StageOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class PredictionViewModel {
    private final ClassifyImageUseCase classifyUseCase;
    private final SubmitDiagnosticUseCase submitUseCase;
    private final IPredictionView view;
    private final ExecutorService executor;

    public PredictionViewModel(ClassifyImageUseCase classifyUseCase,
                               SubmitDiagnosticUseCase submitUseCase,
                               IPredictionView view) {
        this.classifyUseCase = classifyUseCase;
        this.submitUseCase = submitUseCase;
        this.view = view;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void populate(ListCatalogUseCase soilTypesUseCase, ListCatalogUseCase stagesUseCase) {
        view.populate(new SoilTypeOption(soilTypesUseCase.list()));
        view.populate(new StageOption(stagesUseCase.list()));
    }

    public void classify(String imagePath) {
        view.onLoading();
        executor.execute(() -> classifyUseCase.classify(imagePath, new ClassificationResultPresenter(view)));
    }

    public void submit(SubmissionRequest request) {
        android.util.Log.i("PredictionViewModel",
                "submit() called. request=" + (request == null ? "NULL" : "set"));
        view.onLoading();
        executor.execute(() -> {
            try {
                diagnose(request);
            } catch (RuntimeException exception) {
                android.util.Log.e("PredictionViewModel",
                        "Background diagnose() threw — UI will stay on loading.", exception);
            }
        });
    }

    private void diagnose(SubmissionRequest request) {
        android.util.Log.i("PredictionViewModel", "diagnose() running on background thread");
        OperationResult result = submitUseCase.submit(request);
        result.accept((completed, identifier) ->
                android.util.Log.i("PredictionViewModel",
                        "submit returned. completed=" + completed
                                + " identifier=" + identifier));
        result.accept(new DiagnosticResultPresenter(view));
    }

    public void release() {
        executor.shutdown();
    }
}