package com.agropredict.application.usecase.report;

import com.agropredict.application.service.IReportWriter;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticVisitor;
import java.util.Locale;

public final class DiagnosticTraversal implements IDiagnosticVisitor {
    private final IReportWriter report;

    public DiagnosticTraversal(IReportWriter report) {
        this.report = report;
    }

    public void traverse(Diagnostic diagnostic) {
        diagnostic.accept(this);
    }

    @Override
    public void visitPrediction(String predictedCrop, double confidence) {
        report.write("detected_crop", predictedCrop);
        report.write("confidence", String.format(Locale.getDefault(), "%.1f%%", confidence * 100));
    }

    @Override
    public void visitAssessment(String severity, String shortSummary) {
        report.write("summary", shortSummary);
    }

    @Override
    public void visitRecommendation(String recommendationText) {
        report.write("recommendation", recommendationText);
    }
}