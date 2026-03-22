package com.agropredict.presentation.viewmodel.field;

import com.agropredict.domain.entity.Crop;
import com.agropredict.presentation.user_interface.component.input.SoilTypeCatalog;
import com.agropredict.presentation.user_interface.component.input.StageCatalog;

public interface IEditFieldView {
    void populate(Crop crop);
    void populate(SoilTypeCatalog soilTypeOption);
    void populate(StageCatalog stageOption);
    void notify(String message);
    void dismiss();
}