package com.agropredict.infrastructure.persistence.visitor;

import com.agropredict.domain.identifier.IIdentifierConsumer;
import com.agropredict.domain.authentication.ISession;
import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.domain.diagnostic.visitor.IPredictionConsumer;
import com.agropredict.domain.diagnostic.visitor.IRecommendationConsumer;
import com.agropredict.domain.authentication.ISessionConsumer;
import com.agropredict.domain.diagnostic.visitor.ISeverityLevelConsumer;
import com.agropredict.domain.diagnostic.visitor.ISummaryConsumer;
import com.agropredict.domain.diagnostic.visitor.IDiagnosticTargetConsumer;
import com.agropredict.infrastructure.persistence.database.IRow;

public final class DiagnosticPersistenceVisitor implements
        IIdentifierConsumer, IPredictionConsumer, ISeverityLevelConsumer,
        ISummaryConsumer, IRecommendationConsumer, IDiagnosticTargetConsumer, ISessionConsumer {
    private final IRow row;

    public DiagnosticPersistenceVisitor(IRow row, ISession session) {
        this.row = row;
        session.report(this);
    }

    public void persist(Diagnostic diagnostic) {
        diagnostic.identify(this);
        diagnostic.classify(this);
        diagnostic.review(this);
        diagnostic.summarize(this);
        diagnostic.recommend(this);
        diagnostic.bind(this);
    }

    @Override
    public void report(String userIdentifier, String occupation) {
        row.record("user_id", userIdentifier);
    }

    @Override
    public void identify(String identifier) {
        row.record("id", identifier);
    }

    @Override
    public void classify(String predictedCrop, double confidence) {
        row.record("predicted_crop", predictedCrop);
        row.record("confidence", String.valueOf(confidence));
    }

    @Override
    public void review(String value) {
        row.record("severity", value);
    }

    @Override
    public void summarize(String text) {
        row.record("short_summary", text);
    }

    @Override
    public void recommend(String text) {
        row.record("recommendation_text", text);
    }

    @Override
    public void bind(String cropIdentifier, String imageIdentifier) {
        row.record("crop_id", cropIdentifier);
        row.record("image_id", imageIdentifier);
    }
}
