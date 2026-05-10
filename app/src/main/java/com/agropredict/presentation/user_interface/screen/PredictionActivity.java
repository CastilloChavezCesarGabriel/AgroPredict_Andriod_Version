package com.agropredict.presentation.user_interface.screen;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.agropredict.R;
import com.agropredict.application.factory.IPredictionFactory;
import com.agropredict.application.request.diagnostic_submission.ImagePrediction;
import com.agropredict.application.request.diagnostic_submission.PhotographInput;
import com.agropredict.application.service.IImageClassifier;
import com.agropredict.application.service.IImageCompressor;
import com.agropredict.application.usecase.catalog.ListCatalogUseCase;
import com.agropredict.application.usecase.diagnostic.ClassifyImageUseCase;
import com.agropredict.application.usecase.diagnostic.SubmitDiagnosticUseCase;
import com.agropredict.presentation.user_interface.catalog_input.SoilTypeOption;
import com.agropredict.presentation.user_interface.catalog_input.StageOption;
import com.agropredict.presentation.user_interface.form.PredictionForm;
import com.agropredict.presentation.user_interface.selector.DateSelection;
import com.agropredict.presentation.viewmodel.prediction_diagnosis.IPredictionView;
import com.agropredict.presentation.viewmodel.prediction_diagnosis.PredictionViewModel;
import com.agropredict.presentation.viewmodel.prediction_diagnosis.PredictionWorkflow;

public final class PredictionActivity extends BaseActivity implements IPredictionView {
    private PredictionViewModel viewModel;
    private PredictionForm predictionForm;
    private IImageCompressor imageCompressor;
    private String selectedImagePath;
    private ImagePrediction classification;

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
        bind();
        initialize();
        listen();
    }

    private void bind() {
        predictionForm = new PredictionForm(this);
    }

    private void initialize() {
        IPredictionFactory factory = (IPredictionFactory) getApplication();
        IImageClassifier classifier = factory.createImageClassifier();
        imageCompressor = factory.createImageCompressor();
        ClassifyImageUseCase classifyUseCase = new ClassifyImageUseCase(classifier);
        SubmitDiagnosticUseCase submitUseCase = new SubmitDiagnosticUseCase(
                factory.createApiService(), factory.createDiagnosticWorkflow());
        ListCatalogUseCase soilTypes = new ListCatalogUseCase(factory.createSoilTypeCatalog());
        ListCatalogUseCase stages = new ListCatalogUseCase(factory.createStageCatalog());
        viewModel = new PredictionViewModel(new PredictionWorkflow(classifyUseCase, submitUseCase), this);
        viewModel.populate(soilTypes, stages);
    }

    private void listen() {
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
        android.util.Log.i("PredictionActivity", "Submit tapped. imagePath=" + selectedImagePath
                        + " classification=" + (classification == null ? "NULL" : "set"));
        if (selectedImagePath == null || selectedImagePath.isEmpty()) {
            notify(getString(R.string.image_invalid));
            return;
        }
        if (classification == null) {
            notify(getString(R.string.classification_low_confidence));
            return;
        }
        PhotographInput photograph = new PhotographInput(selectedImagePath);
        viewModel.submit(predictionForm.collect(classification, photograph));
    }

    private void present(Uri imageUri) {
        if (imageUri == null) return;
        selectedImagePath = imageCompressor.compress(imageUri.toString());
        predictionForm.preview(imageUri);
        viewModel.classify(selectedImagePath);
    }

    private void inspect(String diagnosticIdentifier) {
        Intent intent = new Intent(this, PredictionResultActivity.class);
        IntentExtra.DIAGNOSTIC_IDENTIFIER.attach(intent, diagnosticIdentifier);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoading() {
        runOnUiThread(() -> predictionForm.load());
    }

    @Override
    public void onIdle() {
        runOnUiThread(() -> predictionForm.rest());
    }

    @Override
    public void onClassified(String cropName, double confidence) {
        this.classification = new ImagePrediction(cropName, confidence);
        runOnUiThread(() -> predictionForm.classify(cropName, confidence));
    }

    @Override
    public void onDiagnosed(String diagnosticIdentifier) {
        runOnUiThread(() -> inspect(diagnosticIdentifier));
    }

    @Override
    public void onFailed() {
        runOnUiThread(() -> notify(getString(R.string.diagnosis_failure)));
    }

    @Override
    public void populate(SoilTypeOption soilTypeOption) {
        predictionForm.populate(soilTypeOption);
    }

    @Override
    public void populate(StageOption stageOption) {
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