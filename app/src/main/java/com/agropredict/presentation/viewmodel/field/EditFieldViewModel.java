package com.agropredict.presentation.viewmodel.field;

import com.agropredict.application.request.CropUpdateRequest;
import com.agropredict.application.usecase.catalog.ListCatalogUseCase;
import com.agropredict.application.usecase.crop.FindCropUseCase;
import com.agropredict.application.usecase.crop.UpdateCropUseCase;
import com.agropredict.domain.entity.Crop;
import com.agropredict.presentation.user_interface.input.SoilTypeCatalog;
import com.agropredict.presentation.user_interface.input.StageCatalog;

public final class EditFieldViewModel {

    private final UpdateCropUseCase updateCropUseCase;
    private final IEditFieldView view;

    public EditFieldViewModel(UpdateCropUseCase updateUseCase, IEditFieldView view) {
        this.updateCropUseCase = updateUseCase;
        this.view = view;
    }

    public void populate(ListCatalogUseCase soilTypesUseCase, ListCatalogUseCase stagesUseCase) {
        view.populate(new SoilTypeCatalog(soilTypesUseCase.list()));
        view.populate(new StageCatalog(stagesUseCase.list()));
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
