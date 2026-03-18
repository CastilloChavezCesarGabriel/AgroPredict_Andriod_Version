package com.agropredict.presentation.viewmodel.report;

import java.util.List;
import java.util.Map;

public interface IReportView {
    void notify(String message);
    void showLoading();
    void hideLoading();
    void populateCrops(List<Map<String, String>> crops);
    void showShareOption(String filePath);
}