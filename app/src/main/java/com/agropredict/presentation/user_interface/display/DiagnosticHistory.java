package com.agropredict.presentation.user_interface.display;

import android.app.Activity;
import android.widget.ListView;
import androidx.core.content.ContextCompat;
import com.agropredict.R;
import com.agropredict.domain.identifier.IIdentifierConsumer;
import com.agropredict.domain.diagnostic.visitor.ISeverityConsumer;
import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.domain.diagnostic.visitor.IPredictionConsumer;
import com.agropredict.presentation.user_interface.selector.ISelectionListener;
import java.util.ArrayList;
import java.util.List;

public final class DiagnosticHistory implements IIdentifierConsumer, IPredictionConsumer, ISeverityConsumer {
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
            diagnostic.identify(this);
            diagnostic.classify(this);
            diagnostic.label(this);
            entries.add(current.build());
        }
        entryAdapter.populate(entries);
    }

    @Override
    public void identify(String identifier) {
        identifiers.add(identifier);
    }

    @Override
    public void classify(String predictedCrop, double confidence) {
        current.describe(predictedCrop, confidence);
    }

    @Override
    public void label(String name, int urgency) {
        current.tag(name, severityColors[urgency]);
    }
}
