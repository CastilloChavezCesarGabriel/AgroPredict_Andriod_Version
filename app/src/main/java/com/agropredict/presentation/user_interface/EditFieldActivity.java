package com.agropredict.presentation.user_interface;

import android.os.Bundle;
import com.agropredict.AgroPredictApplication;
import com.agropredict.R;
import com.agropredict.application.usecase.catalog.ListCatalogUseCase;
import com.agropredict.application.usecase.crop.FindCropUseCase;
import com.agropredict.application.usecase.crop.UpdateCropUseCase;
import com.agropredict.domain.entity.Crop;
import com.agropredict.presentation.user_interface.component.input.SoilTypeCatalog;
import com.agropredict.presentation.user_interface.component.FieldEditor;
import com.agropredict.presentation.user_interface.component.input.StageCatalog;
import com.agropredict.presentation.viewmodel.field.EditFieldViewModel;
import com.agropredict.presentation.viewmodel.field.IEditFieldView;

public final class EditFieldActivity extends BaseActivity implements IEditFieldView {
    private EditFieldViewModel viewModel;
    private FieldEditor fieldEditor;
    private FindCropUseCase loadUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_field);
        fieldEditor = new FieldEditor(this);
        ((AgroPredictApplication) getApplication()).provide(factory -> {
            UpdateCropUseCase updateUseCase = new UpdateCropUseCase(factory.createCropRepository());
            loadUseCase = new FindCropUseCase(factory.createCropRepository());
            ListCatalogUseCase soilTypes = new ListCatalogUseCase(factory.createSoilTypeCatalog());
            ListCatalogUseCase stages = new ListCatalogUseCase(factory.createStageCatalog());
            viewModel = new EditFieldViewModel(updateUseCase, this);
            viewModel.populate(soilTypes, stages);
        });
        findViewById(R.id.btnSaveChanges).setOnClickListener(view -> save());
        String identifier = getIntent().getStringExtra("crop_identifier");
        if (identifier != null) viewModel.load(loadUseCase, identifier);
    }

    private void save() {
        String identifier = getIntent().getStringExtra("crop_identifier");
        viewModel.save(fieldEditor.collect(identifier));
    }

    @Override
    public void populate(Crop crop) {
        fieldEditor.populate(crop);
    }

    @Override
    public void populate(SoilTypeCatalog soilTypeOption) {
        fieldEditor.populate(soilTypeOption);
    }

    @Override
    public void populate(StageCatalog stageOption) {
        fieldEditor.populate(stageOption);
    }

    @Override
    public void dismiss() {
        finish();
    }
}