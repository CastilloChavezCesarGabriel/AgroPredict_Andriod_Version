package com.agropredict.presentation.user_interface.component;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
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

public final class DiagnosticHistory implements IDiagnosticVisitor, IDiagnosticDataVisitor,
        IPredictionVisitor, IDiagnosticContentVisitor, IDiagnosticAssessmentVisitor,
        IDiagnosticSummaryVisitor {
    private final Activity activity;
    private final ListView diagnosticListView;
    private final TextView emptyLabel;
    private final EntryAdapter entryAdapter;
    private final List<String> identifiers;
    private StringBuilder builder;
    private int severityColor;

    public DiagnosticHistory(Activity activity) {
        this.activity = activity;
        this.diagnosticListView = activity.findViewById(R.id.recyclerHistory);
        this.emptyLabel = activity.findViewById(R.id.tvEmptyState);
        this.identifiers = new ArrayList<>();
        this.entryAdapter = new EntryAdapter(activity);
        diagnosticListView.setAdapter(entryAdapter);
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
        List<ListEntry> entries = new ArrayList<>();
        for (Diagnostic diagnostic : diagnostics) {
            builder = new StringBuilder();
            severityColor = ContextCompat.getColor(activity, R.color.severity_low);
            diagnostic.accept(this);
            entries.add(new ListEntry(builder.toString(), severityColor));
        }
        entryAdapter.populate(entries);
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
        String classification = DiagnosticSummary.classify(severity);
        builder.append(classification);
        colorize(classification);
    }

    private void colorize(String classification) {
        if (classification.contains("Moderate"))
            severityColor = ContextCompat.getColor(activity, R.color.severity_moderate);
        else if (classification.contains("Severe"))
            severityColor = ContextCompat.getColor(activity, R.color.severity_high);
    }
}