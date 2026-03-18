package com.agropredict.presentation.user_interface;

import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.agropredict.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class HistoryViewHolder {
    private final ListView diagnosticListView;
    private final TextView emptyLabel;
    private final ArrayAdapter<String> listAdapter;
    private final List<Map<String, Object>> diagnosticItems;

    public HistoryViewHolder(Activity activity) {
        this.diagnosticListView = activity.findViewById(R.id.recyclerHistory);
        this.emptyLabel = activity.findViewById(R.id.tvEmptyState);
        this.diagnosticItems = new ArrayList<>();
        this.listAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, new ArrayList<>());
        diagnosticListView.setAdapter(listAdapter);
    }

    public void attachClickListener(ListView.OnItemClickListener listener) {
        diagnosticListView.setOnItemClickListener(listener);
    }

    public void attachLongClickListener(ListView.OnItemLongClickListener listener) {
        diagnosticListView.setOnItemLongClickListener(listener);
    }

    public void display(List<Map<String, Object>> diagnostics) {
        diagnosticItems.clear();
        diagnosticItems.addAll(diagnostics);
        List<String> labels = extractLabels(diagnostics);
        listAdapter.clear();
        listAdapter.addAll(labels);
        listAdapter.notifyDataSetChanged();
        diagnosticListView.setVisibility(View.VISIBLE);
        emptyLabel.setVisibility(View.GONE);
    }

    public void showEmpty() {
        diagnosticListView.setVisibility(View.GONE);
        emptyLabel.setVisibility(View.VISIBLE);
    }

    public String identifierAt(int position) {
        if (position >= diagnosticItems.size()) return null;
        return String.valueOf(diagnosticItems.get(position).get("identifier"));
    }

    public boolean isValidPosition(int position) {
        return position < diagnosticItems.size();
    }

    private List<String> extractLabels(List<Map<String, Object>> diagnostics) {
        List<String> labels = new ArrayList<>();
        for (Map<String, Object> diagnostic : diagnostics) {
            labels.add(String.valueOf(diagnostic.get("crop_name")));
        }
        return labels;
    }
}
