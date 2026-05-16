package com.agropredict.presentation.user_interface.display;

import com.agropredict.domain.diagnostic.visitor.IPredictionConsumer;
import com.agropredict.domain.diagnostic.visitor.ISeverityConsumer;
import com.agropredict.domain.identifier.IIdentifierConsumer;
import java.util.Locale;
import java.util.Objects;

public final class DiagnosticEntryBuilder implements IIdentifierConsumer, IPredictionConsumer, ISeverityConsumer {
    private final int[] severityColors;
    private final StringBuilder text;
    private String identifier;
    private int color;

    public DiagnosticEntryBuilder(int[] severityColors) {
        this.severityColors = Objects.requireNonNull(severityColors, "diagnostic entry builder requires severity colors");
        this.text = new StringBuilder();
        this.color = severityColors[0];
    }

    @Override
    public void identify(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public void classify(String predictedCrop, double confidence) {
        text.append(predictedCrop).append(" (")
            .append(String.format(Locale.getDefault(), "%.0f%%", confidence * 100))
            .append(")");
    }

    @Override
    public void label(String name, int urgency) {
        text.append(", ").append(name);
        if (urgency >= 0 && urgency < severityColors.length) {
            this.color = severityColors[urgency];
        }
    }

    public void release(IHistoryEntryConsumer consumer) {
        consumer.accept(identifier, new ListEntry(text.toString(), color));
    }
}
