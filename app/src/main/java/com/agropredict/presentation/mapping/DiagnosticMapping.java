package com.agropredict.presentation.mapping;

import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.domain.value.diagnostic.DiagnosticAssessment;
import com.agropredict.domain.value.diagnostic.DiagnosticConditions;
import com.agropredict.domain.value.diagnostic.DiagnosticContent;
import com.agropredict.domain.value.diagnostic.DiagnosticData;
import com.agropredict.domain.value.diagnostic.DiagnosticOwnership;
import com.agropredict.domain.value.diagnostic.DiagnosticSummary;
import com.agropredict.domain.value.diagnostic.Prediction;
import com.agropredict.domain.visitor.IDiagnosticAssessmentVisitor;
import com.agropredict.domain.visitor.IDiagnosticContentVisitor;
import com.agropredict.domain.visitor.IDiagnosticDataVisitor;
import com.agropredict.domain.visitor.IDiagnosticSummaryVisitor;
import com.agropredict.domain.visitor.IDiagnosticVisitor;
import com.agropredict.domain.visitor.IPredictionVisitor;
import java.util.HashMap;
import java.util.Map;

public final class DiagnosticMapping implements IDiagnosticVisitor,
        IDiagnosticDataVisitor, IDiagnosticContentVisitor,
        IDiagnosticAssessmentVisitor, IPredictionVisitor,
        IDiagnosticSummaryVisitor {

    private final Map<String, Object> content;

    public DiagnosticMapping() {
        this.content = new HashMap<>();
    }

    public Map<String, Object> map(Diagnostic diagnostic) {
        content.clear();
        diagnostic.accept(this);
        return new HashMap<>(content);
    }

    @Override
    public void visit(String identifier, DiagnosticData data) {
        content.put("identifier", identifier);
        data.accept(this);
    }

    @Override
    public void visit(Prediction prediction, DiagnosticContent diagnosticContent) {
        if (prediction != null) prediction.accept(this);
        if (diagnosticContent != null) diagnosticContent.accept(this);
    }

    @Override
    public void visit(DiagnosticConditions conditions, DiagnosticAssessment assessment) {
        if (assessment != null) assessment.accept(this);
    }

    @Override
    public void visit(DiagnosticSummary summary, DiagnosticOwnership ownership) {
        if (summary != null) summary.accept(this);
    }

    @Override
    public void visitPrediction(String predictedCrop, double confidence) {
        content.put("predicted_crop", predictedCrop);
        content.put("confidence", confidence);
    }

    @Override
    public void visitSummary(String severity, String shortSummary) {
        content.put("severity", severity);
        content.put("short_summary", shortSummary);
    }
}
