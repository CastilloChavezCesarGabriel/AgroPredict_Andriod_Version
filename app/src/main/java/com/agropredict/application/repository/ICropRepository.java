package com.agropredict.application.repository;

import com.agropredict.application.request.CropUpdateRequest;
import com.agropredict.domain.history.HistoryRecord;
import com.agropredict.domain.entity.Crop;
import java.util.List;

public interface ICropRepository extends IRecordEraser {
    void store(Crop crop);
    void update(CropUpdateRequest request);
    List<Crop> list(String userIdentifier);
    Crop find(String cropIdentifier);
    List<HistoryRecord> trace(String cropIdentifier);
}