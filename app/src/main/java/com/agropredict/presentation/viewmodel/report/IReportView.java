package com.agropredict.presentation.viewmodel.report;

import com.agropredict.domain.entity.Crop;
import java.util.List;

public interface IReportView {
    void notify(String message);
    void load();
    void idle();
    void populate(List<Crop> crops);
    void offer(String filePath);
}
