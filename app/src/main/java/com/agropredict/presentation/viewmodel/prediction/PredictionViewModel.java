package com.agropredict.presentation.viewmodel.prediction;

import android.app.Activity;
import com.agropredict.application.result.ClassificationResult;
import com.agropredict.application.result.OperationResult;
import com.agropredict.application.service.IImageValidatorService;
import com.agropredict.application.usecase.catalog.ListCatalogUseCase;
import com.agropredict.application.usecase.diagnostic.ClassifyImageUseCase;
import com.agropredict.application.usecase.diagnostic.SubmitDiagnosticUseCase;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.CropImage;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.domain.value.crop.CropData;
import com.agropredict.domain.value.crop.CropDetail;
import com.agropredict.domain.value.SubmissionContext;
import com.agropredict.domain.value.diagnostic.DiagnosticConditions;
import com.agropredict.domain.value.diagnostic.DiagnosticContent;
import com.agropredict.domain.value.diagnostic.DiagnosticContext;
import com.agropredict.domain.value.diagnostic.DiagnosticData;
import com.agropredict.domain.value.diagnostic.Prediction;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class PredictionViewModel {

    private final ClassifyImageUseCase classifyUseCase;
    private final SubmitDiagnosticUseCase submitUseCase;
    private final ExecutorService executor;
    private IImageValidatorService imageValidator;
    private IPredictionView view;

    public PredictionViewModel(ClassifyImageUseCase classifyUseCase, SubmitDiagnosticUseCase submitUseCase) {
        this.classifyUseCase = classifyUseCase;
        this.submitUseCase = submitUseCase;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void bind(IPredictionView view) {
        this.view = view;
    }

    public void bind(IImageValidatorService imageValidator) {
        this.imageValidator = imageValidator;
    }

    public void populate(ListCatalogUseCase soilTypesUseCase, ListCatalogUseCase stagesUseCase) {
        if (view == null) return;
        view.populateSoilTypes(soilTypesUseCase.list());
        view.populateStages(stagesUseCase.list());
    }

    public void classify(String imagePath, Activity activity) {
        String error = validate(imagePath);
        if (error != null) {
            if (view != null) view.notify(error);
            return;
        }
        if (view != null) view.showLoading();
        executor.execute(() -> classifyInBackground(imagePath, activity));
    }

    public void submit(Map<String, String> formData, Activity activity) {
        if (view != null) view.showLoading();
        executor.execute(() -> submitInBackground(formData, activity));
    }

    public void release() {
        executor.shutdown();
    }

    private void classifyInBackground(String imagePath, Activity activity) {
        ClassificationResult result = classifyUseCase.classify(imagePath);
        activity.runOnUiThread(() -> present(result));
    }

    private void submitInBackground(Map<String, String> formData, Activity activity) {
        Diagnostic diagnostic = build(formData);
        SubmissionContext context = compose(formData);
        OperationResult result = submitUseCase.submit(diagnostic, context);
        activity.runOnUiThread(() -> present(result));
    }

    private String validate(String imagePath) {
        if (imageValidator == null) return null;
        return imageValidator.validate(imagePath);
    }

    private Diagnostic build(Map<String, String> formData) {
        String identifier = "diag_" + System.currentTimeMillis();
        Prediction prediction = buildPrediction(formData);
        DiagnosticContext context = buildContext(formData);
        DiagnosticConditions conditions = new DiagnosticConditions(context, null);
        DiagnosticContent content = new DiagnosticContent(conditions, null);
        DiagnosticData data = new DiagnosticData(prediction, content);
        return Diagnostic.create(identifier, data);
    }

    private Prediction buildPrediction(Map<String, String> formData) {
        String predictedCrop = formData.getOrDefault("predicted_crop", "");
        double confidence = Double.parseDouble(formData.getOrDefault("confidence", "0.0"));
        return new Prediction(predictedCrop, confidence);
    }

    private SubmissionContext compose(Map<String, String> formData) {
        String cropIdentifier = "crop_" + System.currentTimeMillis();
        String predictedCrop = formData.getOrDefault("predicted_crop", "");
        String stage = formData.getOrDefault("stage", "");
        CropDetail detail = new CropDetail(predictedCrop, stage);
        Crop crop = Crop.create(cropIdentifier, new CropData(detail, null));
        String imagePath = formData.getOrDefault("image_path", "");
        String imageIdentifier = "img_" + System.currentTimeMillis();
        CropImage image = CropImage.create(imageIdentifier, imagePath);
        return new SubmissionContext(crop, image);
    }

    private DiagnosticContext buildContext(Map<String, String> formData) {
        String soilType = formData.getOrDefault("soil_type", "");
        String stage = formData.getOrDefault("stage", "");
        return new DiagnosticContext(soilType, stage);
    }

    private void present(ClassificationResult result) {
        if (view != null) {
            view.hideLoading();
            result.accept(new ClassificationResultStrategy(view));
        }
    }

    private void present(OperationResult result) {
        if (view != null) {
            view.hideLoading();
            result.accept(new DiagnosticResultStrategy(view));
        }
    }
}
