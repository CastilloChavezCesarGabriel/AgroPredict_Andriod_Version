package com.agropredict.presentation.user_interface.component;

import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.agropredict.R;
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
import com.agropredict.domain.visitor.diagnostic.IDiagnosticSummaryVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticVisitor;
import com.agropredict.domain.visitor.diagnostic.IPredictionVisitor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class DiagnosticHistory implements IDiagnosticVisitor,
        IDiagnosticDataVisitor, IPredictionVisitor,
        IDiagnosticContentVisitor, IDiagnosticAssessmentVisitor,
        IDiagnosticSummaryVisitor {
    private final ListView diagnosticListView;
    private final TextView emptyLabel;
    private final ArrayAdapter<String> listAdapter;
    private final List<String> identifiers;
    private final StringBuilder builder;

    public DiagnosticHistory(Activity activity) {
        this.diagnosticListView = activity.findViewById(R.id.recyclerHistory);
        this.emptyLabel = activity.findViewById(R.id.tvEmptyState);
        this.identifiers = new ArrayList<>();
        this.builder = new StringBuilder();
        this.listAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, new ArrayList<>());
        diagnosticListView.setAdapter(listAdapter);
    }

    public void listen(ISelectionListener action) {
        diagnosticListView.setOnItemClickListener(
                (parent, view, position, id) -> resolve(position, action));
    }

    public void observe(ISelectionListener action) {
        diagnosticListView.setOnItemLongClickListener(
                (parent, view, position, id) -> resolve(position, action));
    }

    private boolean resolve(int position, ISelectionListener action) {
        if (position >= identifiers.size()) return false;
        action.onSelect(identifiers.get(position));
        return true;
    }

    public void display(List<Diagnostic> diagnostics) {
        identifiers.clear();
        listAdapter.clear();
        for (Diagnostic diagnostic : diagnostics) {
            builder.setLength(0);
            diagnostic.accept(this);
            listAdapter.add(builder.toString());
        }
        listAdapter.notifyDataSetChanged();
        diagnosticListView.setVisibility(View.VISIBLE);
        emptyLabel.setVisibility(View.GONE);
    }

    public void empty() {
        diagnosticListView.setVisibility(View.GONE);
        emptyLabel.setVisibility(View.VISIBLE);
    }

    @Override
    public void visit(String identifier, DiagnosticData data) {
        identifiers.add(identifier);
        data.accept(this);
    }

    @Override
    public void visit(Prediction prediction, DiagnosticContent content) {
        if (prediction != null) prediction.accept(this);
        if (content != null) {
            builder.append(" — ");
            content.accept(this);
        }
    }

    @Override
    public void visitPrediction(String predictedCrop, double confidence) {
        builder.append(predictedCrop);
        builder.append(" (");
        builder.append(String.format(Locale.getDefault(), "%.0f%%", confidence * 100));
        builder.append(")");
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
    public void visitSummary(String severity, String shortSummary) {
        builder.append(classify(severity));
    }

    private String classify(String severity) {
        if (severity == null) return "Análisis completado";
        String normalized = severity.toLowerCase();
        if (normalized.contains("low") || normalized.contains("baja")) return "Saludable";
        if (normalized.contains("moderate") || normalized.contains("moderada")) return "Problema moderado";
        if (normalized.contains("high") || normalized.contains("alta")) return "Problema grave";
        return "Análisis completado";
    }
}