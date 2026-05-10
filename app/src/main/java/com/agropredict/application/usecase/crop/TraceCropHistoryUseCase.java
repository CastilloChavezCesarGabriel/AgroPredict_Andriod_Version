package com.agropredict.application.usecase.crop;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.domain.history.HistoryRecord;
import java.util.List;
import java.util.Objects;

public final class TraceCropHistoryUseCase {
    private final ICropRepository cropRepository;

    public TraceCropHistoryUseCase(ICropRepository cropRepository) {
        this.cropRepository = Objects.requireNonNull(cropRepository, "trace crop history use case requires a crop repository");
    }

    public List<HistoryRecord> trace(String cropIdentifier) {
        return cropRepository.trace(cropIdentifier);
    }
}
