package com.agropredict.presentation.viewmodel.history;

import java.util.List;
import java.util.Map;

public interface IHistoryView {
    void notify(String message);
    void display(List<Map<String, Object>> diagnostics);
    void navigateToDetail(String diagnosticIdentifier);
    void showEmpty();
}