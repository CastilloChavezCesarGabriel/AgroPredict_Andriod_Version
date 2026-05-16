package com.agropredict.presentation.viewmodel.report_generation;

import com.agropredict.domain.crop.Crop;
import com.agropredict.presentation.user_interface.export.IExportedFile;
import java.util.List;

public interface IReportView {
    void notify(String message);
    void load();
    void rest();
    void populate(List<Crop> crops);
    void offer(IExportedFile artifact);
}