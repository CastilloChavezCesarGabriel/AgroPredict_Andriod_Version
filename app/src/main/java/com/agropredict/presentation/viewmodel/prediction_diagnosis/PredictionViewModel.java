package com.agropredict.presentation.viewmodel.prediction_diagnosis;

import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;
import com.agropredict.application.operation_result.OperationResult;
import com.agropredict.application.usecase.catalog.ListCatalogUseCase;
import com.agropredict.presentation.user_interface.catalog_input.SoilTypeOption;
import com.agropredict.presentation.user_interface.catalog_input.StageOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class PredictionViewModel {
    private final PredictionWorkflow workflow;
    private final IPredictionView view;
    private final ExecutorService executor;

    public PredictionViewModel(PredictionWorkflow workflow, IPredictionView view) {
        this.workflow = workflow;
        this.view = view;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void populate(ListCatalogUseCase soilTypesUseCase, ListCatalogUseCase stagesUseCase) {
        view.populate(new SoilTypeOption(soilTypesUseCase.list()));
        view.populate(new StageOption(stagesUseCase.list()));
    }

    public void classify(String imagePath) {
        view.onLoading();
        executor.execute(() -> workflow.classify(imagePath, new ClassificationResultPresenter(view)));
    }

    public void submit(SubmissionRequest request) {
        view.onLoading();
        executor.execute(() -> diagnose(request));
    }

    private void diagnose(SubmissionRequest request) {
        OperationResult result = workflow.submit(request);
        result.accept(new DiagnosticResultPresenter(view));
    }

    public void release() {
        executor.shutdown();
    }
}
