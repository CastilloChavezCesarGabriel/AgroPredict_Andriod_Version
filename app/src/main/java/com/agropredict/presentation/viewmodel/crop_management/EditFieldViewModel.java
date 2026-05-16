package com.agropredict.presentation.viewmodel.crop_management;

import com.agropredict.application.crop_management.request.CropUpdateRequest;
import com.agropredict.application.catalog.ListCatalogUseCase;
import com.agropredict.application.crop_management.usecase.FindCropUseCase;
import com.agropredict.application.crop_management.usecase.UpdateCropUseCase;
import com.agropredict.domain.crop.Crop;
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
        view.furnish(new SoilTypeOption(soilTypes.list()));
        view.arrange(new StageOption(stages.list()));
    }

    public void load(FindCropUseCase loadUseCase, String cropIdentifier) {
        Crop crop = loadUseCase.find(cropIdentifier);
        if (crop != null) {
            view.apply(crop);
        }
    }

    public void save(CropUpdateRequest request) {
        updateCropUseCase.update(request);
        view.confirm();
        view.dismiss();
    }
}