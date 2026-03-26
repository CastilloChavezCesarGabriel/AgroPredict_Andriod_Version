package com.agropredict.presentation.user_interface.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.agropredict.R;
import java.util.ArrayList;
import java.util.List;

public final class EntryAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final List<ListEntry> entries;

    public EntryAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.entries = new ArrayList<>();
    }

    public void populate(List<ListEntry> newEntries) {
        entries.clear();
        entries.addAll(newEntries);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public ListEntry getItem(int position) {
        return entries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView != null ? convertView : inflater.inflate(R.layout.item_diagnostic, parent, false);
        getItem(position).present(row);
        return row;
    }
}
