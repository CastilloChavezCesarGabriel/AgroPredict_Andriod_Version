package com.agropredict.presentation.viewmodel.field;

import com.agropredict.application.usecase.catalog.ListCatalogUseCase;
import com.agropredict.application.usecase.crop.LoadCropDetailUseCase;
import com.agropredict.application.usecase.crop.UpdateCropUseCase;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.value.crop.CropContent;
import com.agropredict.domain.value.crop.CropData;
import com.agropredict.domain.value.crop.CropDetail;
import com.agropredict.domain.value.crop.CropEnvironment;
import com.agropredict.domain.value.crop.CropOwnership;
import com.agropredict.domain.value.crop.CropSoil;
import com.agropredict.presentation.mapping.CropMapping;
import java.util.Map;

public final class EditFieldViewModel {

    private final UpdateCropUseCase updateCropUseCase;
    private final LoadCropDetailUseCase loadCropDetailUseCase;
    private IEditFieldView view;

    public EditFieldViewModel(UpdateCropUseCase updateUseCase, LoadCropDetailUseCase loadUseCase) {
        this.updateCropUseCase = updateUseCase;
        this.loadCropDetailUseCase = loadUseCase;
    }

    public void bind(IEditFieldView view) {
        this.view = view;
    }

    public void populate(ListCatalogUseCase soilTypesUseCase, ListCatalogUseCase stagesUseCase) {
        if (view == null) return;
        view.populateSoilTypes(soilTypesUseCase.list());
        view.populateStages(stagesUseCase.list());
    }

    public void load(String cropIdentifier) {
        Crop crop = loadCropDetailUseCase.load(cropIdentifier);
        if (view != null && crop != null) {
            view.populate(new CropMapping().map(crop));
        }
    }

    public void save(Map<String, String> cropData) {
        Crop crop = build(cropData);
        updateCropUseCase.update(crop);
        if (view != null) {
            view.notify("Cambios guardados exitosamente");
            view.navigateBack();
        }
    }

    private Crop build(Map<String, String> cropData) {
        CropDetail detail = new CropDetail(null, cropData.get("crop_name"));
        CropSoil soil = new CropSoil(cropData.get("soil_type"), null);
        CropEnvironment environment = new CropEnvironment(null, soil);
        CropOwnership ownership = new CropOwnership(null, cropData.get("stage"));
        CropContent content = new CropContent(environment, ownership);
        CropData data = new CropData(detail, content);
        return Crop.create(cropData.get("identifier"), data);
    }
}
