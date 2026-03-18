package com.agropredict.presentation.user_interface;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import com.agropredict.AgroPredictApplication;
import com.agropredict.R;
import com.agropredict.application.usecase.catalog.ListCatalogUseCase;
import com.agropredict.application.usecase.crop.LoadCropDetailUseCase;
import com.agropredict.application.usecase.crop.UpdateCropUseCase;
import com.agropredict.presentation.viewmodel.field.EditFieldViewModel;
import com.agropredict.presentation.viewmodel.field.IEditFieldView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EditFieldActivity extends BaseActivity implements IEditFieldView {

    private EditFieldViewModel viewModel;
    private EditText cropNameInput;
    private EditText notesInput;
    private Spinner soilTypeSpinner;
    private Spinner stageSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_field);
        compose();
        bind();
        load();
    }

    private void compose() {
        AgroPredictApplication application = (AgroPredictApplication) getApplication();
        application.provide(factory -> {
            UpdateCropUseCase updateCrop = new UpdateCropUseCase(factory.createCropRepository());
            LoadCropDetailUseCase loadCropDetail = new LoadCropDetailUseCase(factory.createCropRepository());
            ListCatalogUseCase listSoilTypes = new ListCatalogUseCase(factory.createSoilTypeCatalog());
            ListCatalogUseCase listStages = new ListCatalogUseCase(factory.createStageCatalog());
            viewModel = new EditFieldViewModel(updateCrop, loadCropDetail);
            viewModel.bind(this);
            viewModel.populate(listSoilTypes, listStages);
        });
    }

    private void bind() {
        cropNameInput = findViewById(R.id.etFieldName);
        notesInput = findViewById(R.id.etCropType);
        soilTypeSpinner = findViewById(R.id.spnSoilType);
        stageSpinner = findViewById(R.id.spnStage);
        findViewById(R.id.btnSaveChanges).setOnClickListener(clickedView -> onSaveClicked());
    }

    private void load() {
        String cropIdentifier = getIntent().getStringExtra("crop_identifier");
        if (cropIdentifier != null) viewModel.load(cropIdentifier);
    }

    private void onSaveClicked() {
        viewModel.save(collect());
    }

    private Map<String, String> collect() {
        Map<String, String> cropData = new HashMap<>();
        cropData.put("identifier", getIntent().getStringExtra("crop_identifier"));
        cropData.put("crop_name", cropNameInput.getText().toString().trim());
        cropData.put("soil_type", soilTypeSpinner.getSelectedItem().toString());
        cropData.put("stage", stageSpinner.getSelectedItem().toString());
        cropData.put("notes", notesInput.getText().toString().trim());
        return cropData;
    }

    @Override
    public void populate(Map<String, String> cropData) {
        cropNameInput.setText(cropData.get("crop_name"));
        notesInput.setText(cropData.get("notes"));
        select(soilTypeSpinner, cropData.get("soil_type"));
        select(stageSpinner, cropData.get("stage"));
    }

    private void select(Spinner spinner, String value) {
        if (value == null || spinner.getAdapter() == null) return;
        for (int index = 0; index < spinner.getCount(); index++) {
            if (spinner.getItemAtPosition(index).toString().equals(value)) {
                spinner.setSelection(index);
                return;
            }
        }
    }

    @Override
    public void populateSoilTypes(List<String> soilTypes) {
        SpinnerPopulator.populate(soilTypeSpinner, soilTypes);
    }

    @Override
    public void populateStages(List<String> stages) {
        SpinnerPopulator.populate(stageSpinner, stages);
    }

    @Override
    public void navigateBack() {
        finish();
    }
}
