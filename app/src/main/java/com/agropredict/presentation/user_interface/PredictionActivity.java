package com.agropredict.presentation.user_interface;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.agropredict.presentation.user_interface.component.PredictionForm;

public final class PredictionActivity extends BaseActivity implements IPredictionView {
    private PredictionViewModel viewModel;
    private PredictionForm predictionForm;
    private IImageService imageService;
    private String selectedImagePath;
    private Classification classification;

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null)
                    present(result.getData().getData());
            });

    private final ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null)
                    present(result.getData().getData());
            });

    private final ActivityResultLauncher<String> cameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) capture();
            });

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
        if (checkSelfPermission(Manifest.permission.CAMERA) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
            return;
        }
        cameraLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
    }

    private void browse() {
        galleryLauncher.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
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