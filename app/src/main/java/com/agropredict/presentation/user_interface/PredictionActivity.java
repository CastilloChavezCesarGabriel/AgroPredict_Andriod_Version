package com.agropredict.presentation.user_interface;
import com.agropredict.presentation.user_interface.component.PredictionForm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import com.agropredict.AgroPredictApplication;
import com.agropredict.R;
import com.agropredict.application.PredictionFacade;
import com.agropredict.application.request.input.Photograph;
import com.agropredict.application.request.data.Classification;
import com.agropredict.application.service.IImageService;
import com.agropredict.application.usecase.catalog.ListCatalogUseCase;
import com.agropredict.application.usecase.diagnostic.ClassifyImageUseCase;
import com.agropredict.application.usecase.diagnostic.SubmitDiagnosticUseCase;
import com.agropredict.presentation.user_interface.component.input.SoilTypeCatalog;
import com.agropredict.presentation.user_interface.component.input.StageCatalog;
import com.agropredict.presentation.viewmodel.prediction.IPredictionView;
import com.agropredict.presentation.viewmodel.prediction.PredictionViewModel;

public final class PredictionActivity extends BaseActivity implements IPredictionView {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private PredictionViewModel viewModel;
    private PredictionForm predictionForm;
    private IImageService imageService;
    private String selectedImagePath;
    private Classification classification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);
        predictionForm = new PredictionForm(this);
        ((AgroPredictApplication) getApplication()).provide(factory -> {
            imageService = factory.createImageService();
            ClassifyImageUseCase classifyUseCase = new ClassifyImageUseCase(imageService);
            SubmitDiagnosticUseCase submitUseCase = new SubmitDiagnosticUseCase(
                    factory.createApiService(), factory.createDiagnosticWorkflow());
            PredictionFacade facade = new PredictionFacade(classifyUseCase, submitUseCase);
            ListCatalogUseCase soilTypes = new ListCatalogUseCase(factory.createSoilTypeCatalog());
            ListCatalogUseCase stages = new ListCatalogUseCase(factory.createStageCatalog());
            viewModel = new PredictionViewModel(facade, this);
            viewModel.populate(soilTypes, stages);
        });
        findViewById(R.id.btnCapture).setOnClickListener(view -> capture());
        findViewById(R.id.btnGallery).setOnClickListener(view -> browse());
        findViewById(R.id.etPlantingDate).setOnClickListener(view -> schedule());
        findViewById(R.id.btnSubmitDiagnosis).setOnClickListener(view -> diagnose());
    }

    private void capture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    private void browse() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
    }

    private void schedule() {
        new DateSelection(date -> predictionForm.stamp(date)).show(this);
    }

    private void diagnose() {
        if (selectedImagePath == null || selectedImagePath.isEmpty()) {
            notify(getString(R.string.image_invalid));
            return;
        }
        Photograph photograph = new Photograph(selectedImagePath);
        viewModel.submit(predictionForm.collect(classification, photograph));
    }

    private void present(Uri imageUri) {
        if (imageUri == null) return;
        selectedImagePath = imageService.compress(imageUri.toString());
        predictionForm.preview(imageUri);
        viewModel.classify(selectedImagePath);
    }

    private void inspect(String diagnosticIdentifier) {
        Intent intent = new Intent(this, PredictionResultActivity.class);
        intent.putExtra("diagnostic_identifier", diagnosticIdentifier);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) return;
        present(data.getData());
    }

    @Override
    public void load() {
        runOnUiThread(() -> predictionForm.load());
    }

    @Override
    public void idle() {
        runOnUiThread(() -> predictionForm.idle());
    }

    @Override
    public void classify(String cropName, String classificationConfidence) {
        double parsed = Double.parseDouble(classificationConfidence.replace("%", "")) / 100;
        this.classification = new Classification(cropName, parsed);
        runOnUiThread(() -> predictionForm.classify(cropName, classificationConfidence));
    }

    @Override
    public void reveal(String diagnosticIdentifier) {
        runOnUiThread(() -> inspect(diagnosticIdentifier));
    }

    @Override
    public void populate(SoilTypeCatalog soilTypeOption) {
        predictionForm.populate(soilTypeOption);
    }

    @Override
    public void populate(StageCatalog stageOption) {
        predictionForm.populate(stageOption);
    }

    @Override
    public void notify(String message) {
        runOnUiThread(() -> super.notify(message));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewModel != null) viewModel.release();
    }
}