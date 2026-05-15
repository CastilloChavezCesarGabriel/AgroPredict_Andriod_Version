package com.agropredict.application.report_generation.usecase;

import com.agropredict.application.service.IReportWriter;
import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.domain.diagnostic.visitor.IPredictionConsumer;
import com.agropredict.domain.diagnostic.visitor.IRecommendationConsumer;
import com.agropredict.domain.diagnostic.visitor.ISummaryConsumer;
import java.util.Locale;
import java.util.Objects;

public final class DiagnosticReportComposer implements IPredictionConsumer, ISummaryConsumer, IRecommendationConsumer {
    private final IReportWriter report;

    public DiagnosticReportComposer(IReportWriter report) {
        this.report = Objects.requireNonNull(report,
                "diagnostic report composer requires a report writer");
    }

    public void compose(Diagnostic diagnostic) {
        diagnostic.classify(this);
        diagnostic.summarize(this);
        diagnostic.recommend(this);
    }

    @Override
    public void classify(String predictedCrop, double confidence) {
        report.write("detected_crop", predictedCrop);
        report.write("confidence", format(confidence));
    }

    @Override
    public void summarize(String text) {
        report.write("summary", text);
    }

    @Override
    public void recommend(String text) {
        report.write("recommendation", text);
    }

    private String format(double ratio) {
        return String.format(Locale.getDefault(), "%.1f%%", ratio * 100);
    }
}