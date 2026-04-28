package com.agropredict.presentation.user_interface.display;

import android.app.Activity;
import android.widget.ListView;
import androidx.core.content.ContextCompat;
import com.agropredict.R;
import com.agropredict.domain.component.diagnostic.ISeverityVisitor;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticVisitor;
import com.agropredict.presentation.user_interface.selector.ISelectionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class DiagnosticHistory implements IDiagnosticVisitor, ISeverityVisitor {
    private final ListView listView;
    private final EntryAdapter entryAdapter;
    private final List<String> identifiers;
    private final int[] severityColors;
    private EntryBuilder current;

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
            current = new EntryBuilder(severityColors[0]);
            diagnostic.accept(this);
            diagnostic.inspect(this);
            entries.add(current.build());
        }
        entryAdapter.populate(entries);
    }

    @Override
    public void visitIdentity(String identifier) {
        identifiers.add(identifier);
    }

    @Override
    public void visitPrediction(String predictedCrop, double confidence) {
        current.describe(predictedCrop, confidence);
    }

    @Override
    public void visit(String name, int urgency) {
        current.tag(name, severityColors[urgency]);
    }

    private static final class EntryBuilder {
        private final StringBuilder text = new StringBuilder();
        private int color;

        EntryBuilder(int defaultColor) {
            this.color = defaultColor;
        }

        void describe(String predictedCrop, double confidence) {
            text.append(predictedCrop).append(" (")
                .append(String.format(Locale.getDefault(), "%.0f%%", confidence * 100))
                .append(")");
        }

        void tag(String label, int tagColor) {
            text.append(" — ").append(label);
            this.color = tagColor;
        }

        ListEntry build() {
            return new ListEntry(text.toString(), color);
        }
    }
}