package com.agropredict.presentation.viewmodel.field;

import com.agropredict.domain.entity.Crop;
import java.util.List;

public interface IEditFieldView {
    void populate(Crop crop);
    void populateSoilTypes(List<String> soilTypes);
    void populateStages(List<String> stages);
    void notify(String message);
    void dismiss();
}
