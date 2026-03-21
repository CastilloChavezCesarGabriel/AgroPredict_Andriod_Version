package com.agropredict.infrastructure.export;

import com.agropredict.domain.component.diagnostic.DiagnosticAssessment;
import com.agropredict.domain.component.diagnostic.DiagnosticConditions;
import com.agropredict.domain.component.diagnostic.DiagnosticContent;
import com.agropredict.domain.component.diagnostic.DiagnosticData;
import com.agropredict.domain.component.diagnostic.DiagnosticOwnership;
import com.agropredict.domain.component.diagnostic.DiagnosticSummary;
import com.agropredict.domain.component.diagnostic.Prediction;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticAssessmentVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticContentVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticDataVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticOwnershipVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticSummaryVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticVisitor;
import com.agropredict.domain.visitor.diagnostic.IPredictionVisitor;
import java.util.Locale;

public final class DiagnosticTraversal implements IDiagnosticVisitor, IDiagnosticDataVisitor,
        IPredictionVisitor, IDiagnosticContentVisitor, IDiagnosticAssessmentVisitor,
        IDiagnosticSummaryVisitor, IDiagnosticOwnershipVisitor {
    private final IReportWriter writer;

    public DiagnosticTraversal(IReportWriter writer) {
        this.writer = writer;
    }

    public void traverse(Diagnostic diagnostic) {
        diagnostic.accept(this);
    }

    @Override
    public void visit(String identifier, DiagnosticData data) {
        data.accept(this);
    }

    @Override
    public void visit(Prediction prediction, DiagnosticContent content) {
        prediction.accept(this);
        if (content != null) content.accept(this);
    }

    @Override
    public void visitPrediction(String predictedCrop, double confidence) {
        writer.write("cultivo_detectado", predictedCrop);
        writer.write("confianza", String.format(Locale.getDefault(), "%.1f%%", confidence * 100));
    }

    @Override
    public void visit(DiagnosticConditions conditions, DiagnosticAssessment assessment) {
        if (assessment != null) assessment.accept(this);
    }

    @Override
    public void visit(DiagnosticSummary summary, DiagnosticOwnership ownership) {
        if (summary != null) summary.accept(this);
        if (ownership != null) ownership.accept(this);
    }

    @Override
    public void visitSummary(String severity, String shortSummary) {
        writer.write("resumen", shortSummary);
    }

    @Override
    public void visitOwnership(String userIdentifier, String recommendation) {
        writer.write("recomendacion", recommendation);
    }
}