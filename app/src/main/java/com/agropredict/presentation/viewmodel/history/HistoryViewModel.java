package com.agropredict.presentation.viewmodel.history;

import com.agropredict.application.consumer.IOperationResultConsumer;
import com.agropredict.application.result.OperationResult;
import com.agropredict.application.usecase.diagnostic.DeleteDiagnosticUseCase;
import com.agropredict.application.usecase.diagnostic.ListDiagnosticsUseCase;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.presentation.mapping.DiagnosticMapping;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class HistoryViewModel implements IOperationResultConsumer {

    private final ListDiagnosticsUseCase listUseCase;
    private final DeleteDiagnosticUseCase deleteUseCase;
    private IHistoryView view;
    private String pendingUserIdentifier;

    public HistoryViewModel(ListDiagnosticsUseCase listUseCase, DeleteDiagnosticUseCase deleteUseCase) {
        this.listUseCase = listUseCase;
        this.deleteUseCase = deleteUseCase;
    }

    public void bind(IHistoryView view) {
        this.view = view;
    }

    public void load(String userIdentifier) {
        List<Diagnostic> diagnostics = listUseCase.list(userIdentifier);
        if (view == null) {
            return;
        }
        if (diagnostics.isEmpty()) {
            view.showEmpty();
        } else {
            view.display(map(diagnostics));
        }
    }

    public void filter(List<Map<String, Object>> allDiagnostics, String cropType) {
        if (cropType == null || cropType.equals("Todos")) {
            if (view != null) view.display(allDiagnostics);
            return;
        }
        List<Map<String, Object>> filtered = new ArrayList<>();
        for (Map<String, Object> diagnostic : allDiagnostics) {
            Object type = diagnostic.get("crop_type");
            if (type != null && type.toString().equals(cropType)) {
                filtered.add(diagnostic);
            }
        }
        if (view != null) view.display(filtered);
    }

    public void delete(String diagnosticIdentifier, String userIdentifier) {
        this.pendingUserIdentifier = userIdentifier;
        OperationResult result = deleteUseCase.delete(diagnosticIdentifier);
        result.accept(this);
    }

    @Override
    public void visit(boolean completed, String resultIdentifier) {
        if (view == null) return;
        if (completed) {
            view.notify("Diagnostico eliminado exitosamente");
            load(pendingUserIdentifier);
        } else {
            view.notify("Error al eliminar el diagnostico");
        }
    }

    private List<Map<String, Object>> map(List<Diagnostic> diagnostics) {
        DiagnosticMapping mapping = new DiagnosticMapping();
        List<Map<String, Object>> mapped = new ArrayList<>();
        for (Diagnostic diagnostic : diagnostics) {
            mapped.add(mapping.map(diagnostic));
        }
        return mapped;
    }
}
