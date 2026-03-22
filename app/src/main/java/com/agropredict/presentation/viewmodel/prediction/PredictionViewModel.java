package com.agropredict.presentation.viewmodel.prediction;

import com.agropredict.application.PredictionFacade;
import com.agropredict.application.request.SubmissionRequest;
import com.agropredict.application.result.OperationResult;
import com.agropredict.application.usecase.catalog.ListCatalogUseCase;
import com.agropredict.presentation.user_interface.input.SoilTypeCatalog;
import com.agropredict.presentation.user_interface.input.StageCatalog;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class PredictionViewModel {

    private final PredictionFacade facade;
    private final IPredictionView view;
    private final ExecutorService executor;

    public PredictionViewModel(PredictionFacade facade, IPredictionView view) {
        this.facade = facade;
        this.view = view;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void populate(ListCatalogUseCase soilTypesUseCase, ListCatalogUseCase stagesUseCase) {
        view.populate(new SoilTypeCatalog(soilTypesUseCase.list()));
        view.populate(new StageCatalog(stagesUseCase.list()));
    }

    public void classify(String imagePath) {
        view.load();
        executor.execute(() -> facade.classify(imagePath, new ClassificationResultStrategy(view)));
    }

    public void submit(SubmissionRequest request) {
        view.load();
        executor.execute(() -> {
            OperationResult result = facade.submit(request);
            result.accept(new DiagnosticResultStrategy(view));
        });
    }

    public void release() {
        executor.shutdown();
    }
}
