package com.agropredict.presentation.user_interface.display;

import java.util.Locale;

final class EntryBuilder {
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