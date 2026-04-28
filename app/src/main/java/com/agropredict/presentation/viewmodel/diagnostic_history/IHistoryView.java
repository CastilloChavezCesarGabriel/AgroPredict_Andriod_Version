package com.agropredict.presentation.viewmodel.diagnostic_history;

import com.agropredict.domain.entity.Diagnostic;
import java.util.List;

public interface IHistoryView {
    void notify(String message);
    void display(List<Diagnostic> diagnostics);
    void inspect(String diagnosticIdentifier);
}