package com.agropredict.application.repository;

import com.agropredict.application.result.HistoryRecord;
import com.agropredict.domain.entity.Crop;
import java.util.List;

public interface ICropRepository {
    void store(Crop crop);
    void update(Crop crop);
    List<Crop> list(String userIdentifier);
    Crop find(String cropIdentifier);
    List<HistoryRecord> trace(String cropIdentifier);
    void delete(String cropIdentifier);
}