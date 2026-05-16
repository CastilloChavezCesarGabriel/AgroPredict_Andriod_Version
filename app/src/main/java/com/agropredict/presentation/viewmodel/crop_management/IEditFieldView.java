package com.agropredict.presentation.viewmodel.crop_management;

import com.agropredict.domain.crop.Crop;
import com.agropredict.presentation.user_interface.catalog_input.SoilTypeOption;
import com.agropredict.presentation.user_interface.catalog_input.StageOption;

public interface IEditFieldView {
    void apply(Crop crop);
    void furnish(SoilTypeOption soilTypeOption);
    void arrange(StageOption stageOption);
    void notify(String message);
    void confirm();
    void dismiss();
}