package com.agropredict.presentation.user_interface;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import com.agropredict.AgroPredictApplication;
import com.agropredict.R;
import com.agropredict.application.usecase.catalog.ListCatalogUseCase;
import com.agropredict.application.usecase.diagnostic.ClassifyImageUseCase;
import com.agropredict.application.usecase.diagnostic.SubmitDiagnosticUseCase;
import com.agropredict.presentation.viewmodel.prediction.IPredictionView;
import com.agropredict.presentation.viewmodel.prediction.PredictionViewModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PredictionActivity extends BaseActivity implements IPredictionView {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private PredictionViewModel viewModel;
    private PredictionViewHolder holder;
    private String selectedImagePath;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);
        compose();
        bind();
    }

    private void compose() {
        AgroPredictApplication application = (AgroPredictApplication) getApplication();
        application.provide(factory -> {
            ClassifyImageUseCase classifyImage = new ClassifyImageUseCase(factory.createClassifierService());
            SubmitDiagnosticUseCase submitDiagnostic = new SubmitDiagnosticUseCase(factory);
            ListCatalogUseCase listSoilTypes = new ListCatalogUseCase(factory.createSoilTypeCatalog());
            ListCatalogUseCase listStages = new ListCatalogUseCase(factory.createStageCatalog());
            viewModel = new PredictionViewModel(classifyImage, submitDiagnostic);
            viewModel.bind(this);
            viewModel.bind(factory.createImageValidatorService());
            viewModel.populate(listSoilTypes, listStages);
        });
    }

    private void bind() {
        holder = new PredictionViewHolder(this);
        attach();
    }

    private void attach() {
        findViewById(R.id.btnCapture).setOnClickListener(clickedView -> openCamera());
        findViewById(R.id.btnGallery).setOnClickListener(clickedView -> openGallery());
        findViewById(R.id.etPlantingDate).setOnClickListener(clickedView -> showDatePicker());
        findViewById(R.id.btnSubmitDiagnosis).setOnClickListener(clickedView -> onSubmitClicked());
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY);
    }

    private void showDatePicker() {
        new DateSelection(this::onDateSelected).show(this);
    }

    private void onDateSelected(String formattedDate) {
        selectedDate = formattedDate;
        holder.displayDate(selectedDate);
    }

    private void onSubmitClicked() {
        if (selectedImagePath == null || selectedImagePath.isEmpty()) {
            notify(getString(R.string.image_invalid));
            return;
        }
        viewModel.submit(collect(), this);
    }

    private Map<String, String> collect() {
        Map<String, String> formData = new HashMap<>();
        formData.put("image_path", selectedImagePath);
        formData.put("soil_type", holder.selectedSoilType());
        formData.put("stage", holder.selectedStage());
        formData.put("planting_date", selectedDate != null ? selectedDate : "");
        return formData;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) return;
        display(data.getData());
    }

    private void display(Uri imageUri) {
        if (imageUri == null) return;
        selectedImagePath = imageUri.toString();
        holder.displayImage(imageUri);
        viewModel.classify(selectedImagePath, this);
    }

    @Override
    public void showLoading() {
        holder.showLoading();
    }

    @Override
    public void hideLoading() {
        holder.hideLoading();
    }

    @Override
    public void displayClassification(String cropName, String confidence) {
        holder.displayClassification(cropName, confidence);
    }

    @Override
    public void navigateToResult(String diagnosticIdentifier) {
        Intent intent = new Intent(this, PredictionResultActivity.class);
        intent.putExtra("diagnostic_identifier", diagnosticIdentifier);
        startActivity(intent);
        finish();
    }

    @Override
    public void populateSoilTypes(List<String> soilTypes) {
        holder.populateSoilTypes(soilTypes);
    }

    @Override
    public void populateStages(List<String> stages) {
        holder.populateStages(stages);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewModel != null) viewModel.release();
    }
}
