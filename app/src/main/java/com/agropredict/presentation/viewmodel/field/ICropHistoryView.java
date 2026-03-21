package com.agropredict.presentation.viewmodel.field;

import com.agropredict.application.result.HistoryRecord;
import java.util.List;

public interface ICropHistoryView {
    void display(List<HistoryRecord> records);
    void empty();
}
