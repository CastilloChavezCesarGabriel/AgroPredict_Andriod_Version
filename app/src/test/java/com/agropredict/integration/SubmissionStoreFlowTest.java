package com.agropredict.integration;

import static org.junit.Assert.assertTrue;

import com.agropredict.application.diagnostic_submission.FieldRecorder;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.application.request.diagnostic_submission.Classification;
import com.agropredict.application.request.diagnostic_submission.Cultivation;
import com.agropredict.application.request.diagnostic_submission.PhotographInput;
import com.agropredict.application.request.diagnostic_submission.Submission;
import com.agropredict.application.request.diagnostic_submission.SubmissionField;
import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;
import com.agropredict.repository.CapturingCropRepository;
import com.agropredict.repository.FixedCatalogRepository;
import com.agropredict.visitor.StageCapturingVisitor;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public final class SubmissionStoreFlowTest {
    private IPhotographRepository noopPhotographRepository() {
        return new IPhotographRepository() {
            @Override public void store(com.agropredict.domain.entity.Photograph photograph, com.agropredict.domain.entity.Crop crop) {}
            @Override public com.agropredict.domain.entity.Photograph find(String diagnosticIdentifier) { return null; }
            @Override public void clear(String cropIdentifier) {}
        };
    }

    private SubmissionRequest buildRequest(String predictedCrop, double confidence, String stageName) {
        Classification prediction = new Classification(predictedCrop, confidence);
        Cultivation cultivation = new Cultivation(predictedCrop, stageName);
        PhotographInput photograph = new PhotographInput("/tmp/test.jpg");
        SubmissionField field = new SubmissionField(cultivation, photograph);
        Submission submission = new Submission(prediction, field);
        return new SubmissionRequest(submission, null);
    }

    @Test
    public void testStoreResolvesStageNameToCatalogIdentifierBeforePersist() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("Vegetative", "stage_id_42");
        ICatalogRepository stageCatalog = new FixedCatalogRepository(mapping);
        CapturingCropRepository cropRepository = new CapturingCropRepository();

        buildRequest("rice", 0.91, "Vegetative")
                .store(cropRepository, noopPhotographRepository(), stageCatalog);

        StageCapturingVisitor visitor = new StageCapturingVisitor();
        cropRepository.walk(visitor);
        assertTrue(visitor.recordedStage("stage_id_42"));
        assertTrue(cropRepository.storedExactly(1));
    }

    @Test
    public void testFieldRecorderRoutesCatalogThroughToCropEntity() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("Flowering", "stage_id_77");
        ICatalogRepository stageCatalog = new FixedCatalogRepository(mapping);
        CapturingCropRepository cropRepository = new CapturingCropRepository();
        FieldRecorder recorder = new FieldRecorder(cropRepository, noopPhotographRepository(), stageCatalog);

        recorder.record(buildRequest("tomato", 0.88, "Flowering"));

        StageCapturingVisitor visitor = new StageCapturingVisitor();
        cropRepository.walk(visitor);
        assertTrue(visitor.recordedStage("stage_id_77"));
    }

    @Test
    public void testFieldRecorderUnknownStageWritesNoIdentifier() {
        ICatalogRepository emptyCatalog = new FixedCatalogRepository(new HashMap<>());
        CapturingCropRepository cropRepository = new CapturingCropRepository();
        FieldRecorder recorder = new FieldRecorder(cropRepository, noopPhotographRepository(), emptyCatalog);

        recorder.record(buildRequest("rice", 0.91, "Vegetative"));

        StageCapturingVisitor visitor = new StageCapturingVisitor();
        cropRepository.walk(visitor);
        assertTrue(cropRepository.storedExactly(1));
        org.junit.Assert.assertFalse(visitor.recordedStage("Vegetative"));
    }
}