package com.agropredict.presentation.user_interface.component;

import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;
import com.agropredict.R;

public final class ListEntry {
    private final String label;
    private final int color;

    public ListEntry(String label, int color) {
        this.label = label;
        this.color = color;
    }

    public void present(View row) {
        TextView textView = row.findViewById(R.id.tvDiagnosticLabel);
        View indicator = row.findViewById(R.id.severityIndicator);
        textView.setText(label);
        ((GradientDrawable) indicator.getBackground().mutate()).setColor(color);
    }
}
