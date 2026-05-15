package com.agropredict.presentation.viewmodel.prediction_diagnosis;

import com.agropredict.application.crop_management.usecase.ListCropUseCase;
import com.agropredict.application.diagnostic_submission.request.SubmissionRequest;
import com.agropredict.application.operation_result.IUseCaseResult;
import com.agropredict.application.catalog.ListCatalogUseCase;
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
        view.furnish(new SoilTypeOption(soilTypesUseCase.list()));
        view.arrange(new StageOption(stagesUseCase.list()));
    }

    public void load(ListCropUseCase listCrops, String userIdentifier) {
        view.offer(listCrops.list(userIdentifier));
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
        IUseCaseResult result = workflow.submit(request);
        result.accept(new DiagnosticResultPresenter(view));
    }

    public void release() {
        executor.shutdown();
    }
}
