package com.agropredict.application.usecase.crop;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.result.HistoryRecord;
import java.util.List;

public final class TraceCropHistoryUseCase {
    private final ICropRepository cropRepository;

    public TraceCropHistoryUseCase(ICropRepository cropRepository) {
        this.cropRepository = cropRepository;
    }

    public List<HistoryRecord> trace(String cropIdentifier) {
        return cropRepository.trace(cropIdentifier);
    }
}