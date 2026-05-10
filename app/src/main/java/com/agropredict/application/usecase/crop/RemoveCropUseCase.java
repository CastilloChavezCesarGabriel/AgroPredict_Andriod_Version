package com.agropredict.application.usecase.crop;

import com.agropredict.application.operation_result.IUseCaseResult;
import com.agropredict.application.operation_result.SuccessfulOperation;
import com.agropredict.application.operation_result.FailedOperation;
import com.agropredict.application.repository.ICropRecord;
import com.agropredict.application.repository.IRecordEraser;
import java.util.List;
import java.util.Objects;

public final class RemoveCropUseCase {
    private final IRecordEraser cropEraser;
    private final List<ICropRecord> records;

    public RemoveCropUseCase(IRecordEraser cropEraser, List<ICropRecord> records) {
        this.cropEraser = Objects.requireNonNull(cropEraser, "remove crop use case requires a crop eraser");
        this.records = List.copyOf(Objects.requireNonNull(records, "remove crop use case requires a record list"));
    }

    public IUseCaseResult remove(String cropIdentifier) {
        try {
            records.forEach(record -> record.discard(cropIdentifier));
            cropEraser.erase(cropIdentifier);
            return new SuccessfulOperation(cropIdentifier);
        } catch (RuntimeException exception) {
            return new FailedOperation();
        }
    }
}