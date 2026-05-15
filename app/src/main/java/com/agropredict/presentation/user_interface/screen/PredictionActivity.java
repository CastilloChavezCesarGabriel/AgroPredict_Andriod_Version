package com.agropredict.presentation.user_interface.screen;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.agropredict.R;
import com.agropredict.application.authentication.usecase.CheckSessionUseCase;
import com.agropredict.application.catalog.ListCatalogUseCase;
import com.agropredict.application.crop_management.usecase.ListCropUseCase;
import com.agropredict.application.diagnostic_submission.request.ImagePrediction;
import com.agropredict.application.diagnostic_submission.request.PhotographInput;
import com.agropredict.application.diagnostic_submission.rejection.IImageRejection;
import com.agropredict.application.diagnostic_submission.usecase.ClassifyImageUseCase;
import com.agropredict.application.diagnostic_submission.usecase.SubmitDiagnosticUseCase;
import com.agropredict.application.factory.ICatalogFactory;
import com.agropredict.application.factory.IDashboardFactory;
import com.agropredict.application.factory.IDiagnosticApiFactory;
import com.agropredict.application.factory.IDiagnosticWorkflowFactory;
import com.agropredict.application.factory.IImageClassificationFactory;
import com.agropredict.application.service.IImageClassifier;
import com.agropredict.application.service.IImageCompressor;
import com.agropredict.application.service.IImageValidator;
import com.agropredict.domain.crop.Crop;
import com.agropredict.presentation.user_interface.catalog_input.SoilTypeOption;
import com.agropredict.presentation.user_interface.catalog_input.StageOption;
import com.agropredict.presentation.user_interface.form.PredictionForm;
import com.agropredict.presentation.user_interface.selector.DateSelection;
import com.agropredict.presentation.viewmodel.diagnostic_history.AndroidSeverityFactory;
import com.agropredict.presentation.viewmodel.prediction_diagnosis.ClassificationResultPresenter;
import com.agropredict.presentation.viewmodel.prediction_diagnosis.IPredictionView;
import com.agropredict.presentation.viewmodel.prediction_diagnosis.PredictionViewModel;
import com.agropredict.presentation.viewmodel.prediction_diagnosis.PredictionWorkflow;
import java.util.List;

public final class PredictionActivity extends BaseActivity implements IPredictionView {
    private PredictionViewModel viewModel;
    private PredictionForm predictionForm;
    private IImageCompressor imageCompressor;
    private IImageValidator imageValidator;
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
        initialize();
        listen();
    }

    private void initialize() {
        IImageClassificationFactory imageFactory = (IImageClassificationFactory) getApplication();
        IDiagnosticApiFactory apiFactory = (IDiagnosticApiFactory) getApplication();
        IDiagnosticWorkflowFactory workflowFactory = (IDiagnosticWorkflowFactory) getApplication();
        ICatalogFactory catalogFactory = (ICatalogFactory) getApplication();
        IDashboardFactory dashboardFactory = (IDashboardFactory) getApplication();
        IImageClassifier classifier = imageFactory.createImageClassifier();
        imageCompressor = imageFactory.createImageCompressor();
        imageValidator = imageFactory.createImageValidator();
        predictionForm = new PredictionForm(this,
                catalogFactory.createSoilTypeCatalog(),
                catalogFactory.createStageCatalog());
        ClassifyImageUseCase classifyUseCase = new ClassifyImageUseCase(classifier);
        SubmitDiagnosticUseCase submitUseCase = new SubmitDiagnosticUseCase(
                apiFactory.createApiService(), workflowFactory.createDiagnosticWorkflow());
        ListCatalogUseCase soilTypes = new ListCatalogUseCase(catalogFactory.createSoilTypeCatalog());
        ListCatalogUseCase stages = new ListCatalogUseCase(catalogFactory.createStageCatalog());
        ListCropUseCase listCrops = new ListCropUseCase(catalogFactory.createCropRepository());
        viewModel = new PredictionViewModel(new PredictionWorkflow(classifyUseCase, submitUseCase), this);
        viewModel.populate(soilTypes, stages);
        new CheckSessionUseCase(dashboardFactory.createSessionRepository())
                .check((identifier, occupation) -> viewModel.load(listCrops, identifier));
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
        IImageRejection rejection = imageValidator.validate(imageUri.toString());
        if (rejection != null) {
            rejection.encode(new ClassificationResultPresenter(this));
            return;
        }
        try {
            selectedImagePath = imageCompressor.compress(imageUri.toString());
        } catch (RuntimeException compressionFailed) {
            notify(getString(R.string.image_processing_failed));
            return;
        }
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
        this.classification = new ImagePrediction(cropName, confidence,
                new AndroidSeverityFactory(this).createPending());
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
    public void furnish(SoilTypeOption soilTypeOption) {
        predictionForm.furnish(soilTypeOption);
    }

    @Override
    public void arrange(StageOption stageOption) {
        predictionForm.arrange(stageOption);
    }

    @Override
    public void offer(List<Crop> crops) {
        runOnUiThread(() -> predictionForm.offer(crops));
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
