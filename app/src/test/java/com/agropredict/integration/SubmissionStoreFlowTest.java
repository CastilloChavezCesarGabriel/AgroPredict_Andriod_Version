package com.agropredict.integration;

import static org.junit.Assert.assertTrue;

import com.agropredict.application.diagnostic_submission.workflow.CropDossier;
import com.agropredict.application.diagnostic_submission.workflow.CropRegistry;
import com.agropredict.application.diagnostic_submission.workflow.SubmissionIdentity;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.diagnostic_submission.ai_questionnaire.Condition;
import com.agropredict.application.diagnostic_submission.ai_questionnaire.CropCare;
import com.agropredict.application.diagnostic_submission.ai_questionnaire.FarmManagement;
import com.agropredict.application.diagnostic_submission.ai_questionnaire.Irrigation;
import com.agropredict.application.diagnostic_submission.ai_questionnaire.Observation;
import com.agropredict.application.diagnostic_submission.ai_questionnaire.Pest;
import com.agropredict.application.diagnostic_submission.ai_questionnaire.PestControl;
import com.agropredict.application.diagnostic_submission.ai_questionnaire.Questionnaire;
import com.agropredict.application.diagnostic_submission.ai_questionnaire.Rainfall;
import com.agropredict.application.diagnostic_submission.ai_questionnaire.SoilAnswer;
import com.agropredict.application.diagnostic_submission.ai_questionnaire.Symptom;
import com.agropredict.application.diagnostic_submission.ai_questionnaire.Weather;
import com.agropredict.application.diagnostic_submission.request.ImagePrediction;
import com.agropredict.application.diagnostic_submission.request.Cultivation;
import com.agropredict.application.diagnostic_submission.request.PhotographInput;
import com.agropredict.application.diagnostic_submission.request.Submission;
import com.agropredict.application.diagnostic_submission.request.SubmissionRequest;
import com.agropredict.application.diagnostic_submission.request.DiagnosticSubject;
import com.agropredict.repository.CapturingCropRepository;
import com.agropredict.repository.CapturingPhotographRepository;
import com.agropredict.repository.FixedCatalogRepository;
import com.agropredict.visitor.StageCapturingVisitor;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public final class SubmissionStoreFlowTest {
    private SubmissionRequest assemble(String predictedCrop, double confidence, String stageName) {
        ImagePrediction prediction = new ImagePrediction(predictedCrop, confidence);
        Cultivation cultivation = new Cultivation(predictedCrop, stageName);
        PhotographInput photograph = new PhotographInput("/tmp/test.jpg");
        DiagnosticSubject subject = new DiagnosticSubject(cultivation, photograph);
        Submission submission = new Submission(prediction, subject);
        Questionnaire questionnaire = new Questionnaire(
                new Condition(new Weather("", "", new Rainfall("")), new SoilAnswer("", "")),
                new CropCare(
                        new FarmManagement(new Irrigation("", ""), new PestControl("", "")),
                        new Observation(new Symptom("", ""), new Pest("", ""))));
        return new SubmissionRequest(submission, questionnaire);
    }

    private SubmissionIdentity allocate() {
        return new SubmissionIdentity("crop_test_id", "image_test_id");
    }

    @Test
    public void testStoreResolvesStageNameToCatalogIdentifierBeforePersist() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("Vegetative", "stage_id_42");
        ICatalogRepository stageCatalog = new FixedCatalogRepository(mapping);
        CapturingCropRepository cropRepository = new CapturingCropRepository();
        CropDossier dossier = new CropDossier(cropRepository, new CapturingPhotographRepository());
        CropRegistry registry = new CropRegistry(dossier, stageCatalog);

        assemble("rice", 0.91, "Vegetative").store(registry, allocate());

        StageCapturingVisitor visitor = new StageCapturingVisitor();
        cropRepository.walk(visitor);
        assertTrue(visitor.recordedStage("stage_id_42"));
        assertTrue(cropRepository.storedExactly(1));
    }

    @Test
    public void testRegistryRoutesCatalogThroughToCropEntity() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("Flowering", "stage_id_77");
        ICatalogRepository stageCatalog = new FixedCatalogRepository(mapping);
        CapturingCropRepository cropRepository = new CapturingCropRepository();
        CropDossier dossier = new CropDossier(cropRepository, new CapturingPhotographRepository());
        CropRegistry registry = new CropRegistry(dossier, stageCatalog);

        assemble("tomato", 0.88, "Flowering").store(registry, allocate());

        StageCapturingVisitor visitor = new StageCapturingVisitor();
        cropRepository.walk(visitor);
        assertTrue(visitor.recordedStage("stage_id_77"));
    }

    @Test
    public void testRegistryUnknownStageWritesNoIdentifier() {
        ICatalogRepository emptyCatalog = new FixedCatalogRepository(new HashMap<>());
        CapturingCropRepository cropRepository = new CapturingCropRepository();
        CropDossier dossier = new CropDossier(cropRepository, new CapturingPhotographRepository());
        CropRegistry registry = new CropRegistry(dossier, emptyCatalog);

        assemble("rice", 0.91, "Vegetative").store(registry, allocate());

        StageCapturingVisitor visitor = new StageCapturingVisitor();
        cropRepository.walk(visitor);
        assertTrue(cropRepository.storedExactly(1));
        org.junit.Assert.assertFalse(visitor.recordedStage("Vegetative"));
    }
}
