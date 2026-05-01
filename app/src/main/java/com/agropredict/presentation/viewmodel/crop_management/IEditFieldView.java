package com.agropredict.presentation.viewmodel.crop_management;

import com.agropredict.domain.entity.Crop;
import com.agropredict.presentation.user_interface.catalog_input.SoilTypeOption;
import com.agropredict.presentation.user_interface.catalog_input.StageOption;

public interface IEditFieldView {
    void populate(Crop crop);
    void populate(SoilTypeOption soilTypeOption);
    void populate(StageOption stageOption);
    void notify(String message);
    void dismiss();
}