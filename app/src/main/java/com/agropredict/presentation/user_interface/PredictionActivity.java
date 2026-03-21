package com.agropredict.presentation.user_interface;
import com.agropredict.presentation.user_interface.holder.PredictionViewHolder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import com.agropredict.AgroPredictApplication;
import com.agropredict.R;
import com.agropredict.application.PredictionFacade;
import com.agropredict.application.request.input.Photograph;
import com.agropredict.application.request.data.Classification;
import com.agropredict.application.request.SubmissionRequest;
import com.agropredict.application.service.IImageService;
import com.agropredict.application.usecase.catalog.ListCatalogUseCase;
import com.agropredict.application.usecase.diagnostic.ClassifyImageUseCase;
import com.agropredict.application.usecase.diagnostic.SubmitDiagnosticUseCase;
import com.agropredict.presentation.viewmodel.prediction.IPredictionView;
import com.agropredict.presentation.viewmodel.prediction.PredictionViewModel;
import java.util.List;

public final class PredictionActivity extends BaseActivity implements IPredictionView {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private PredictionViewModel viewModel;
    private PredictionViewHolder holder;
    private IImageService imageService;
    private String selectedImagePath;
    private String predictedCrop;
    private double confidence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);
        holder = new PredictionViewHolder(this);
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
        new DateSelection(date -> holder.date(date)).show(this);
    }

    private void diagnose() {
        if (selectedImagePath == null || selectedImagePath.isEmpty()) {
            notify(getString(R.string.image_invalid));
            return;
        }
        Classification classification = new Classification(predictedCrop, confidence);
        Photograph photograph = new Photograph(selectedImagePath);
        viewModel.submit(holder.collect(classification, photograph));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) return;
        present(data.getData());
    }

    private void present(Uri imageUri) {
        if (imageUri == null) return;
        selectedImagePath = imageService.compress(imageUri.toString());
        holder.preview(imageUri);
        viewModel.classify(selectedImagePath);
    }

    @Override
    public void load() {
        runOnUiThread(() -> holder.load());
    }

    @Override
    public void idle() {
        runOnUiThread(() -> holder.idle());
    }

    @Override
    public void classify(String cropName, String classificationConfidence) {
        this.predictedCrop = cropName;
        this.confidence = Double.parseDouble(classificationConfidence.replace("%", "")) / 100;
        runOnUiThread(() -> holder.classify(cropName, classificationConfidence));
    }

    @Override
    public void reveal(String diagnosticIdentifier) {
        runOnUiThread(() -> {
            Intent intent = new Intent(this, PredictionResultActivity.class);
            intent.putExtra("diagnostic_identifier", diagnosticIdentifier);
            startActivity(intent);
            finish();
        });
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
    public void notify(String message) {
        runOnUiThread(() -> super.notify(message));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewModel != null) viewModel.release();
    }
}
