package com.agropredict.integration;

import static org.junit.Assert.assertTrue;

import com.agropredict.application.diagnostic_submission.Allocation;
import com.agropredict.application.diagnostic_submission.Cropland;
import com.agropredict.application.diagnostic_submission.FieldRecorder;
import com.agropredict.application.diagnostic_submission.FieldStorage;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.request.diagnostic_submission.Classification;
import com.agropredict.application.request.diagnostic_submission.Cultivation;
import com.agropredict.application.request.diagnostic_submission.PhotographInput;
import com.agropredict.application.request.diagnostic_submission.Submission;
import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;
import com.agropredict.application.request.diagnostic_submission.Subject;
import com.agropredict.repository.CapturingCropRepository;
import com.agropredict.repository.CapturingPhotographRepository;
import com.agropredict.repository.FixedCatalogRepository;
import com.agropredict.visitor.StageCapturingVisitor;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public final class SubmissionStoreFlowTest {
    private SubmissionRequest assemble(String predictedCrop, double confidence, String stageName) {
        Classification prediction = new Classification(predictedCrop, confidence);
        Cultivation cultivation = new Cultivation(predictedCrop, stageName);
        PhotographInput photograph = new PhotographInput("/tmp/test.jpg");
        Subject subject = new Subject(cultivation, photograph);
        Submission submission = new Submission(prediction, subject);
        return new SubmissionRequest(submission, null);
    }

    private Allocation allocate() {
        return new Allocation("crop_test_id", "image_test_id");
    }

    @Test
    public void testStoreResolvesStageNameToCatalogIdentifierBeforePersist() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("Vegetative", "stage_id_42");
        ICatalogRepository stageCatalog = new FixedCatalogRepository(mapping);
        CapturingCropRepository cropRepository = new CapturingCropRepository();
        FieldStorage storage = new FieldStorage(cropRepository, new CapturingPhotographRepository());
        Cropland cropland = new Cropland(storage, stageCatalog);

        assemble("rice", 0.91, "Vegetative").store(cropland, allocate());

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
        FieldStorage storage = new FieldStorage(cropRepository, new CapturingPhotographRepository());
        Cropland cropland = new Cropland(storage, stageCatalog);
        FieldRecorder recorder = new FieldRecorder(cropland);

        recorder.record(assemble("tomato", 0.88, "Flowering"), allocate());

        StageCapturingVisitor visitor = new StageCapturingVisitor();
        cropRepository.walk(visitor);
        assertTrue(visitor.recordedStage("stage_id_77"));
    }

    @Test
    public void testFieldRecorderUnknownStageWritesNoIdentifier() {
        ICatalogRepository emptyCatalog = new FixedCatalogRepository(new HashMap<>());
        CapturingCropRepository cropRepository = new CapturingCropRepository();
        FieldStorage storage = new FieldStorage(cropRepository, new CapturingPhotographRepository());
        Cropland cropland = new Cropland(storage, emptyCatalog);
        FieldRecorder recorder = new FieldRecorder(cropland);

        recorder.record(assemble("rice", 0.91, "Vegetative"), allocate());

        StageCapturingVisitor visitor = new StageCapturingVisitor();
        cropRepository.walk(visitor);
        assertTrue(cropRepository.storedExactly(1));
        org.junit.Assert.assertFalse(visitor.recordedStage("Vegetative"));
    }
}
