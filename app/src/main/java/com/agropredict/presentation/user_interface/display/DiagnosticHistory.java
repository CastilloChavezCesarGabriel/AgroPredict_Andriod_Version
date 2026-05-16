package com.agropredict.presentation.user_interface.display;

import android.app.Activity;
import android.widget.ListView;
import androidx.core.content.ContextCompat;
import com.agropredict.R;
import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.presentation.user_interface.selector.ISelectionListener;
import java.util.ArrayList;
import java.util.List;

public final class DiagnosticHistory {
    private final ListView listView;
    private final EntryAdapter entryAdapter;
    private final List<String> identifiers;
    private final int[] severityColors;

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
            DiagnosticEntryBuilder builder = new DiagnosticEntryBuilder(severityColors);
            diagnostic.identify(builder);
            diagnostic.classify(builder);
            diagnostic.label(builder);
            builder.release((identifier, entry) -> {
                identifiers.add(identifier);
                entries.add(entry);
            });
        }
        entryAdapter.populate(entries);
    }
}
