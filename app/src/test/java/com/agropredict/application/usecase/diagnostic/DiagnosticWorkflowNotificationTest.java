package com.agropredict.application.usecase.diagnostic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
import com.agropredict.application.diagnostic_submission.request.Cultivation;
import com.agropredict.application.diagnostic_submission.request.DiagnosticSubject;
import com.agropredict.application.diagnostic_submission.request.ImagePrediction;
import com.agropredict.application.diagnostic_submission.request.PhotographInput;
import com.agropredict.application.diagnostic_submission.request.Submission;
import com.agropredict.application.diagnostic_submission.request.SubmissionRequest;
import com.agropredict.application.diagnostic_submission.workflow.CropDossier;
import com.agropredict.application.diagnostic_submission.workflow.CropRegistry;
import com.agropredict.application.diagnostic_submission.workflow.DiagnosticArchive;
import com.agropredict.application.diagnostic_submission.workflow.DiagnosticWorkflow;
import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.domain.diagnostic.Prediction;
import com.agropredict.domain.diagnostic.Recommendation;
import com.agropredict.domain.diagnostic.Severity;
import com.agropredict.notification.CapturingNotificationService;
import com.agropredict.repository.CapturingCropRepository;
import com.agropredict.repository.CapturingDiagnosticRepository;
import com.agropredict.repository.CapturingPhotographRepository;
import com.agropredict.repository.FixedCatalogRepository;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.junit.Test;

public final class DiagnosticWorkflowNotificationTest {
    private DiagnosticWorkflow workflow(CapturingNotificationService service) {
        CropDossier dossier = new CropDossier(new CapturingCropRepository(), new CapturingPhotographRepository());
        CropRegistry registry = new CropRegistry(dossier, new FixedCatalogRepository(new HashMap<>()));
        DiagnosticArchive archive = new DiagnosticArchive(new CapturingDiagnosticRepository(), (id, q) -> {});
        return new DiagnosticWorkflow(registry, archive, service);
    }

    private Diagnostic severe() {
        Diagnostic diagnostic = new Diagnostic("d_severe", new Prediction("rice", 0.9));
        diagnostic.conclude(new Severity("high", "Severe issue", 2), new Recommendation(null, null));
        return diagnostic;
    }

    private Diagnostic moderate() {
        Diagnostic diagnostic = new Diagnostic("d_moderate", new Prediction("rice", 0.9));
        diagnostic.conclude(new Severity("moderate", "Moderate issue", 1), new Recommendation(null, null));
        return diagnostic;
    }

    private Diagnostic healthy() {
        Diagnostic diagnostic = new Diagnostic("d_healthy", new Prediction("rice", 0.9));
        diagnostic.conclude(new Severity("low", "Healthy", 0), new Recommendation(null, null));
        return diagnostic;
    }

    private Diagnostic pending() {
        return new Diagnostic("d_pending", new Prediction("rice", 0.9));
    }

    private SubmissionRequest request() {
        ImagePrediction prediction = new ImagePrediction("rice", 0.9);
        Cultivation cultivation = new Cultivation("rice", null);
        PhotographInput photograph = new PhotographInput("/tmp/test.jpg");
        Submission submission = new Submission(prediction, new DiagnosticSubject(cultivation, photograph));
        Questionnaire questionnaire = new Questionnaire(
                new Condition(new Weather("", "", new Rainfall("")), new SoilAnswer("", "")),
                new CropCare(
                        new FarmManagement(new Irrigation("", ""), new PestControl("", "")),
                        new Observation(new Symptom("", ""), new Pest("", ""))));
        return new SubmissionRequest(submission, questionnaire);
    }

    @Test
    public void testPersistSevereDiagnosticTriggersAlert() {
        CapturingNotificationService service = new CapturingNotificationService();
        workflow(service).persist(request(), severe());
        assertTrue(service.wasAlerted());
    }

    @Test
    public void testPersistModerateDiagnosticDoesNotTriggerAlert() {
        CapturingNotificationService service = new CapturingNotificationService();
        workflow(service).persist(request(), moderate());
        assertFalse(service.wasAlerted());
    }

    @Test
    public void testPersistHealthyDiagnosticDoesNotTriggerAlert() {
        CapturingNotificationService service = new CapturingNotificationService();
        workflow(service).persist(request(), healthy());
        assertFalse(service.wasAlerted());
    }

    @Test
    public void testPersistPendingDiagnosticDoesNotTriggerAlert() {
        CapturingNotificationService service = new CapturingNotificationService();
        workflow(service).persist(request(), pending());
        assertFalse(service.wasAlerted());
    }

    @Test
    public void testNullNotificationServiceDoesNotThrowOnSevereDiagnostic() {
        CropDossier dossier = new CropDossier(new CapturingCropRepository(), new CapturingPhotographRepository());
        CropRegistry registry = new CropRegistry(dossier, new FixedCatalogRepository(new HashMap<>()));
        DiagnosticArchive archive = new DiagnosticArchive(new CapturingDiagnosticRepository(), (id, q) -> {});
        new DiagnosticWorkflow(registry, archive).persist(request(), severe());
    }

    @Test
    public void testAlertFiresExactlyOncePerSevereDiagnostic() {
        CapturingNotificationService service = new CapturingNotificationService();
        workflow(service).persist(request(), severe());
        assertEquals(1, service.alertCount());
    }

    @Test
    public void testPersistStoresDiagnosticEvenWhenAlertIsTriggered() {
        CapturingNotificationService service = new CapturingNotificationService();
        int[] storeCount = {0};
        IDiagnosticRepository trackingRepository = new IDiagnosticRepository() {
            @Override public void store(Diagnostic d) { storeCount[0]++; }
            @Override public List<Diagnostic> list(String id) { return Collections.emptyList(); }
            @Override public List<Diagnostic> filter(String uid, String cid) { return Collections.emptyList(); }
            @Override public Diagnostic find(String id) { return null; }
            @Override public Diagnostic resolve(String uid, String cid) { return null; }
        };
        CropDossier dossier = new CropDossier(new CapturingCropRepository(), new CapturingPhotographRepository());
        CropRegistry registry = new CropRegistry(dossier, new FixedCatalogRepository(new HashMap<>()));
        DiagnosticArchive archive = new DiagnosticArchive(trackingRepository, (id, q) -> {});
        DiagnosticWorkflow workflow = new DiagnosticWorkflow(registry, archive, service);

        workflow.persist(request(), severe());

        assertTrue(service.wasAlerted());
        assertEquals(1, storeCount[0]);
    }

    @Test
    public void testTwoConsecutiveSeverePersistsFireTwoAlerts() {
        CapturingNotificationService service = new CapturingNotificationService();
        DiagnosticWorkflow workflow = workflow(service);

        workflow.persist(request(), severe());
        workflow.persist(request(), severe());

        assertEquals(2, service.alertCount());
    }

    @Test
    public void testSevereDiagnosticAfterHealthyDiagnosticTriggersExactlyOneAlert() {
        CapturingNotificationService service = new CapturingNotificationService();
        DiagnosticWorkflow workflow = workflow(service);

        workflow.persist(request(), healthy());
        workflow.persist(request(), severe());

        assertEquals(1, service.alertCount());
    }

    @Test
    public void testCustomUrgencyAboveThresholdTriggersAlert() {
        CapturingNotificationService service = new CapturingNotificationService();
        Diagnostic aboveThreshold = new Diagnostic("d_above", new Prediction("rice", 0.9));
        aboveThreshold.conclude(new Severity("critical", "Critical issue", 3), new Recommendation(null, null));

        workflow(service).persist(request(), aboveThreshold);

        assertTrue(service.wasAlerted());
    }

    @Test
    public void testAlertDoesNotFireWhenDiagnosticStorageThrows() {
        CapturingNotificationService service = new CapturingNotificationService();
        IDiagnosticRepository throwingRepository = new IDiagnosticRepository() {
            @Override public void store(Diagnostic d) { throw new RuntimeException("storage failed"); }
            @Override public List<Diagnostic> list(String id) { return Collections.emptyList(); }
            @Override public List<Diagnostic> filter(String uid, String cid) { return Collections.emptyList(); }
            @Override public Diagnostic find(String id) { return null; }
            @Override public Diagnostic resolve(String uid, String cid) { return null; }
        };
        CropDossier dossier = new CropDossier(new CapturingCropRepository(), new CapturingPhotographRepository());
        CropRegistry registry = new CropRegistry(dossier, new FixedCatalogRepository(new HashMap<>()));
        DiagnosticArchive archive = new DiagnosticArchive(throwingRepository, (id, q) -> {});
        DiagnosticWorkflow workflow = new DiagnosticWorkflow(registry, archive, service);

        try {
            workflow.persist(request(), severe());
        } catch (RuntimeException ignored) {}

        assertFalse(service.wasAlerted());
    }

    @Test
    public void testNoAlertsForMixOfNonSevereDiagnostics() {
        CapturingNotificationService service = new CapturingNotificationService();
        DiagnosticWorkflow workflow = workflow(service);

        workflow.persist(request(), healthy());
        workflow.persist(request(), moderate());
        workflow.persist(request(), pending());

        assertEquals(0, service.alertCount());
    }
}
