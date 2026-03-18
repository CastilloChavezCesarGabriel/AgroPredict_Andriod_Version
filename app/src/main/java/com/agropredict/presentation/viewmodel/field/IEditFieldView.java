package com.agropredict.presentation.viewmodel.field;

import java.util.List;
import java.util.Map;

public interface IEditFieldView {
    void notify(String message);
    void populate(Map<String, String> cropData);
    void populateSoilTypes(List<String> soilTypes);
    void populateStages(List<String> stages);
    void navigateBack();
}