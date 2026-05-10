package com.agropredict.presentation.user_interface.display;

import com.agropredict.domain.history.HistoryRecord;
import com.agropredict.domain.history.IHistoryTransitionConsumer;
import com.agropredict.domain.history.IModificationConsumer;
import com.agropredict.domain.history.ITimestampConsumer;
import java.util.List;

public final class HistoryDialogRenderer
        implements IModificationConsumer, IHistoryTransitionConsumer, ITimestampConsumer {
    private final StringBuilder buffer = new StringBuilder();

    public void render(List<HistoryRecord> records) {
        for (HistoryRecord record : records) {
            record.inscribe(this);
            record.link(this);
            record.stamp(this);
        }
    }

    public String reveal() {
        return buffer.toString().trim();
    }

    @Override
    public void inscribe(String field) {
        buffer.append(field).append(": ");
    }

    @Override
    public void link(String previousValue, String currentValue) {
        buffer.append(previousValue).append(" → ").append(currentValue).append("\n");
    }

    @Override
    public void stamp(String value) {
        buffer.append(value).append("\n\n");
    }
}
