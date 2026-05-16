package com.agropredict.presentation.user_interface.screen;

import android.os.Bundle;
import com.agropredict.R;
import com.agropredict.application.factory.ICatalogFactory;
import com.agropredict.application.catalog.ListCatalogUseCase;
import com.agropredict.application.crop_management.usecase.FindCropUseCase;
import com.agropredict.application.crop_management.usecase.UpdateCropUseCase;
import com.agropredict.domain.crop.Crop;
import com.agropredict.presentation.user_interface.catalog_input.SoilTypeOption;
import com.agropredict.presentation.user_interface.catalog_input.StageOption;
import com.agropredict.presentation.user_interface.selector.FieldEditor;
import com.agropredict.presentation.viewmodel.crop_management.EditFieldViewModel;
import com.agropredict.presentation.viewmodel.crop_management.IEditFieldView;

public final class EditFieldActivity extends BaseActivity implements IEditFieldView {
    private EditFieldViewModel viewModel;
    private FieldEditor fieldEditor;
    private FindCropUseCase loadUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_field);
        initialize();
        listen();
        load();
    }

    private void initialize() {
        ICatalogFactory factory = (ICatalogFactory) getApplication();
        fieldEditor = new FieldEditor(this, factory.createSoilTypeCatalog(), factory.createStageCatalog());
        UpdateCropUseCase updateUseCase = new UpdateCropUseCase(factory.createCropRepository());
        loadUseCase = new FindCropUseCase(factory.createCropRepository());
        ListCatalogUseCase soilTypes = new ListCatalogUseCase(factory.createSoilTypeCatalog());
        ListCatalogUseCase stages = new ListCatalogUseCase(factory.createStageCatalog());
        viewModel = new EditFieldViewModel(updateUseCase, this);
        viewModel.populate(soilTypes, stages);
    }

    private void listen() {
        findViewById(R.id.btnSaveChanges).setOnClickListener(view -> save());
    }

    private void load() {
        String identifier = IntentExtra.CROP_IDENTIFIER.read(getIntent());
        if (identifier != null) viewModel.load(loadUseCase, identifier);
    }

    private void save() {
        String identifier = IntentExtra.CROP_IDENTIFIER.read(getIntent());
        viewModel.save(fieldEditor.collect(identifier));
    }

    @Override
    public void apply(Crop crop) {
        fieldEditor.apply(crop);
    }

    @Override
    public void furnish(SoilTypeOption soilTypeOption) {
        fieldEditor.furnish(soilTypeOption);
    }

    @Override
    public void arrange(StageOption stageOption) {
        fieldEditor.arrange(stageOption);
    }

    @Override
    public void confirm() {
        notify(getString(R.string.changes_saved));
    }

    @Override
    public void dismiss() {
        finish();
    }
}