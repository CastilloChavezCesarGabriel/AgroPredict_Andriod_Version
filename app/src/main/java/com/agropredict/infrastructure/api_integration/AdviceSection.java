package com.agropredict.infrastructure.api_integration;

import java.util.List;
import java.util.Objects;

public final class AdviceSection {
    private static final String BULLET = "• ";
    private final String title;
    private final List<String> items;

    public AdviceSection(String title, List<String> items) {
        this.title = Objects.requireNonNull(title, "advice section requires a title");
        this.items = List.copyOf(Objects.requireNonNull(items, "advice section requires items"));
    }

    public String compose() {
        if (items.isEmpty()) return "";
        StringBuilder builder = new StringBuilder(title).append(":\n");
        for (String item : items) {
            String trimmed = item.trim();
            if (trimmed.isEmpty()) continue;
            builder.append(BULLET).append(trimmed).append('\n');
        }
        return builder.toString();
    }
}
