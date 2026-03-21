package com.agropredict.presentation.viewmodel.field;

import com.agropredict.application.result.HistoryRecord;
import com.agropredict.application.usecase.crop.TraceCropHistoryUseCase;
import java.util.List;

public final class CropHistoryViewModel {
    private final TraceCropHistoryUseCase traceUseCase;
    private final ICropHistoryView view;

    public CropHistoryViewModel(TraceCropHistoryUseCase traceUseCase, ICropHistoryView view) {
        this.traceUseCase = traceUseCase;
        this.view = view;
    }

    public void load(String cropIdentifier) {
        List<HistoryRecord> records = traceUseCase.trace(cropIdentifier);
        if (records.isEmpty()) {
            view.empty();
        } else {
            view.display(records);
        }
    }
}
