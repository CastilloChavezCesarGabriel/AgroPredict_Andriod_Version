package com.agropredict.presentation.user_interface.display;

import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.domain.diagnostic.visitor.IPredictionConsumer;
import com.agropredict.domain.diagnostic.visitor.IRecommendationConsumer;
import com.agropredict.domain.diagnostic.visitor.ISeverityLevelConsumer;
import com.agropredict.domain.diagnostic.visitor.ISummaryConsumer;

public abstract class DiagnosticDisplay implements IPredictionConsumer, ISeverityLevelConsumer, ISummaryConsumer, IRecommendationConsumer {
    protected final PredictionDisplay predictionDisplay;

    protected DiagnosticDisplay(PredictionDisplay predictionDisplay) {
        this.predictionDisplay = predictionDisplay;
    }

    public void display(Diagnostic diagnostic) {
        diagnostic.classify(this);
        diagnostic.review(this);
        diagnostic.summarize(this);
        diagnostic.recommend(this);
    }

    @Override
    public void classify(String predictedCrop, double confidence) {
        predictionDisplay.classify(predictedCrop, confidence);
    }
}
