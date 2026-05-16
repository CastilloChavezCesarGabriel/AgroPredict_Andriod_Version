package com.agropredict.presentation.viewmodel.crop_management;

import com.agropredict.application.DeleteUseCase;
import com.agropredict.application.crop_management.usecase.TraceCropHistoryUseCase;
import com.agropredict.application.operation_result.IOperationResult;
import com.agropredict.application.operation_result.IUseCaseResult;
import com.agropredict.application.photograph.LoadPhotographUseCase;
import com.agropredict.domain.history.HistoryRecord;
import com.agropredict.domain.photograph.Photograph;
import java.util.List;
import java.util.Objects;

public final class FieldInspection {
    private final DeleteUseCase deleteUseCase;
    private final TraceCropHistoryUseCase traceUseCase;
    private final LoadPhotographUseCase loadPhotograph;

    public FieldInspection(DeleteUseCase deleteUseCase, TraceCropHistoryUseCase traceUseCase, LoadPhotographUseCase loadPhotograph) {
        this.deleteUseCase = Objects.requireNonNull(deleteUseCase, "field inspection requires a delete use case");
        this.traceUseCase = Objects.requireNonNull(traceUseCase, "field inspection requires a trace use case");
        this.loadPhotograph = Objects.requireNonNull(loadPhotograph, "field inspection requires a load photograph use case");
    }

    public Photograph find(String cropIdentifier) {
        return loadPhotograph.find(cropIdentifier);
    }

    public List<HistoryRecord> trace(String cropIdentifier) {
        return traceUseCase.trace(cropIdentifier);
    }

    public void delete(String cropIdentifier, IOperationResult outcome) {
        IUseCaseResult result = deleteUseCase.delete(cropIdentifier);
        result.accept(outcome);
    }
}
