package com.agropredict.presentation.user_interface.component;

import android.app.Activity;
import android.widget.ListView;
import androidx.core.content.ContextCompat;
import com.agropredict.R;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticVisitor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class DiagnosticHistory implements IDiagnosticVisitor {
    private final ListView listView;
    private final EntryAdapter entryAdapter;
    private final List<String> identifiers;
    private final int[] severityColors;
    private StringBuilder builder;

    public DiagnosticHistory(Activity activity) {
        this.listView = activity.findViewById(R.id.recyclerHistory);
        this.identifiers = new ArrayList<>();
        this.entryAdapter = new EntryAdapter(activity);
        this.severityColors = new int[]{
            ContextCompat.getColor(activity, R.color.severity_low),
            ContextCompat.getColor(activity, R.color.severity_moderate),
            ContextCompat.getColor(activity, R.color.severity_high)
        };
        listView.setAdapter(entryAdapter);
        listView.setEmptyView(activity.findViewById(R.id.tvEmptyState));
    }

    public void listen(ISelectionListener action) {
        listView.setOnItemClickListener(
                (parent, view, position, id) -> resolve(position, action));
    }

    public void observe(ISelectionListener action) {
        listView.setOnItemLongClickListener(
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
            diagnostic.accept(this);
            builder.append(" — ");
            builder.append(diagnostic.classify());
            entries.add(new ListEntry(builder.toString(), severityColors[0]));
        }
        entryAdapter.populate(entries);
    }

    public void empty() {
        identifiers.clear();
        entryAdapter.populate(new ArrayList<>());
    }

    @Override
    public void visitIdentity(String identifier) {
        identifiers.add(identifier);
    }

    @Override
    public void visitPrediction(String predictedCrop, double confidence) {
        builder.append(predictedCrop);
        builder.append(" (");
        builder.append(String.format(Locale.getDefault(), "%.0f%%", confidence * 100));
        builder.append(")");
    }

    @Override
    public void visitAssessment(String severity, String shortSummary) {}

    @Override
    public void visitRecommendation(String recommendationText) {}
}
