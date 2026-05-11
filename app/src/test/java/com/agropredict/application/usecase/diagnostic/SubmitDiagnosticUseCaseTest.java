package com.agropredict.application.usecase.diagnostic;

import com.agropredict.application.diagnostic_submission.workflow.CropDossier;
import com.agropredict.application.diagnostic_submission.workflow.CropRegistry;
import com.agropredict.application.diagnostic_submission.workflow.DiagnosticArchive;
import com.agropredict.application.diagnostic_submission.workflow.DiagnosticWorkflow;
import com.agropredict.application.diagnostic_submission.usecase.SubmitDiagnosticUseCase;
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
import com.agropredict.application.service.IDiagnosticApiService;
import com.agropredict.repository.CapturingCropRepository;
import com.agropredict.repository.CapturingDiagnosticRepository;
import com.agropredict.repository.CapturingPhotographRepository;
import com.agropredict.repository.FixedCatalogRepository;
import com.agropredict.visitor.FailExpecter;

import java.util.HashMap;
import org.junit.Test;

public final class SubmitDiagnosticUseCaseTest {
    private IDiagnosticApiService failingApi() {
        return (diagnostic, request) -> { throw new RuntimeException("API down"); };
    }

    private DiagnosticWorkflow inertWorkflow() {
        CropDossier dossier = new CropDossier(new CapturingCropRepository(), new CapturingPhotographRepository());
        CropRegistry registry = new CropRegistry(dossier, new FixedCatalogRepository(new HashMap<>()));
        DiagnosticArchive archive = new DiagnosticArchive(new CapturingDiagnosticRepository(), (id, q) -> {});
        return new DiagnosticWorkflow(registry, archive);
    }

    private SubmissionRequest assemble() {
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

    @Test(expected = NullPointerException.class)
    public void testNullSubmissionIsRejectedAtConstruction() {
        new SubmissionRequest(null, null);
    }

    @Test
    public void testSubmitApiFailure() {
        new SubmitDiagnosticUseCase(failingApi(), inertWorkflow())
            .submit(assemble()).accept(new FailExpecter());
    }
}