package com.agropredict.presentation.viewmodel.prediction_diagnosis;

import com.agropredict.domain.diagnostic.visitor.IClassificationResult;

public final class ClassificationResultPresenter implements IClassificationResult {
    private final IPredictionView view;

    public ClassificationResultPresenter(IPredictionView view) {
        this.view = view;
    }

    @Override
    public void onClassify(String predictedCrop, double confidence) {
        view.onIdle();
        view.onClassified(predictedCrop, confidence);
    }

    @Override
    public void onReject(String reason) {
        view.onIdle();
        view.notify(reason);
    }
}
