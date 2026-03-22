package com.agropredict.presentation.user_interface;
import com.agropredict.presentation.user_interface.holder.EditFieldViewHolder;

import android.os.Bundle;
import com.agropredict.AgroPredictApplication;
import com.agropredict.R;
import com.agropredict.application.usecase.catalog.ListCatalogUseCase;
import com.agropredict.application.usecase.crop.FindCropUseCase;
import com.agropredict.application.usecase.crop.UpdateCropUseCase;
import com.agropredict.domain.entity.Crop;
import com.agropredict.presentation.user_interface.input.SoilTypeCatalog;
import com.agropredict.presentation.user_interface.input.StageCatalog;
import com.agropredict.presentation.viewmodel.field.EditFieldViewModel;
import com.agropredict.presentation.viewmodel.field.IEditFieldView;

public final class EditFieldActivity extends BaseActivity implements IEditFieldView {
    private EditFieldViewModel viewModel;
    private EditFieldViewHolder holder;
    private FindCropUseCase loadUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_field);
        holder = new EditFieldViewHolder(this);
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
        viewModel.save(holder.collect(identifier));
    }

    @Override
    public void populate(Crop crop) {
        holder.populate(crop);
    }

    @Override
    public void populate(SoilTypeCatalog soilTypeOption) {
        holder.populate(soilTypeOption);
    }

    @Override
    public void populate(StageCatalog stageOption) {
        holder.populate(stageOption);
    }

    @Override
    public void dismiss() {
        finish();
    }
}