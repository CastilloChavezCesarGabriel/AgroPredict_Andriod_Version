package com.agropredict.presentation.viewmodel.crop_management;

import com.agropredict.application.request.CropUpdateRequest;
import com.agropredict.application.usecase.catalog.ListCatalogUseCase;
import com.agropredict.application.usecase.crop.FindCropUseCase;
import com.agropredict.application.usecase.crop.UpdateCropUseCase;
import com.agropredict.domain.entity.Crop;
import com.agropredict.presentation.user_interface.catalog_input.SoilTypeOption;
import com.agropredict.presentation.user_interface.catalog_input.StageOption;

public final class EditFieldViewModel {
    private final UpdateCropUseCase updateCropUseCase;
    private final IEditFieldView view;

    public EditFieldViewModel(UpdateCropUseCase updateUseCase, IEditFieldView view) {
        this.updateCropUseCase = updateUseCase;
        this.view = view;
    }

    public void populate(ListCatalogUseCase soilTypes, ListCatalogUseCase stages) {
        view.populate(new SoilTypeOption(soilTypes.list()));
        view.populate(new StageOption(stages.list()));
    }

    public void load(FindCropUseCase loadUseCase, String cropIdentifier) {
        Crop crop = loadUseCase.find(cropIdentifier);
        if (crop != null) {
            view.populate(crop);
        }
    }

    public void save(CropUpdateRequest request) {
        updateCropUseCase.update(request);
        view.notify("Cambios guardados exitosamente");
        view.dismiss();
    }
}