package com.agropredict.infrastructure.api_integration;

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
import com.agropredict.application.diagnostic_submission.ai_questionnaire.SoilAnswer;
import com.agropredict.application.diagnostic_submission.ai_questionnaire.Symptom;
import com.agropredict.application.diagnostic_submission.ai_questionnaire.Rainfall;
import com.agropredict.application.diagnostic_submission.ai_questionnaire.Weather;
import com.agropredict.application.diagnostic_submission.request.ImagePrediction;
import com.agropredict.application.diagnostic_submission.request.Cultivation;
import com.agropredict.application.diagnostic_submission.request.PhotographInput;
import com.agropredict.application.diagnostic_submission.request.Submission;
import com.agropredict.application.diagnostic_submission.request.DiagnosticSubject;
import com.agropredict.application.diagnostic_submission.request.SubmissionRequest;
import com.agropredict.domain.diagnostic.ISeverityFactory;
import com.agropredict.domain.diagnostic.Prediction;
import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.domain.diagnostic.Severity;
import com.agropredict.domain.diagnostic.SeverityClassifier;
import com.agropredict.visitor.SeverityCapturingVisitor;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33)
public final class RealDiagnosticApiServiceTest {
    private MockWebServer server;
    private DiagnosticApiService service;

    @Before
    public void setup() throws IOException {
        server = new MockWebServer();
        server.start();
        DiagnosticHTTPGateway gateway = new DiagnosticHTTPGateway(server.url("/diagnostic").toString());
        DiagnosticResponseReader reader = new DiagnosticResponseReader(build());
        service = new DiagnosticApiService(gateway, reader);
    }

    private static ISeverityFactory build() {
        SeverityClassifier healthy = new SeverityClassifier(List.of("bajo", "low"), new Severity("low", "Healthy", 0));
        SeverityClassifier moderate = new SeverityClassifier(List.of("moderado", "moderate"), new Severity("moderate", "Moderate issue", 1));
        SeverityClassifier severe = new SeverityClassifier(List.of("alto", "high", "critico", "critical"), new Severity("high", "Severe issue", 2));
        Severity unknown = new Severity(null, "Analysis complete", 0);
        return new GravitySeverityFactory(List.of(healthy, moderate, severe), unknown);
    }

    @After
    public void teardown() throws IOException {
        server.shutdown();
    }

    @Test
    public void testSubmitPostsToDiagnosticEndpointWithJsonContentType() throws Exception {
        server.enqueue(success("Bajo", "Mild leaf yellowing", "Maintain irrigation"));

        Diagnostic diagnostic = new Diagnostic("d_1", new Prediction("rice", 0.87));
        service.submit(diagnostic, build(0.87));

        RecordedRequest recorded = server.takeRequest(2, TimeUnit.SECONDS);
        assertEquals("POST", Objects.requireNonNull(recorded).getMethod());
        assertEquals("/diagnostic", recorded.getPath());
        assertEquals("application/json", recorded.getHeader("Content-Type"));
    }

    @Test
    public void testSubmitBodyHasDetectedCropAndConfidenceAtTopLevel() throws Exception {
        server.enqueue(success("Bajo", "Healthy crop", "Continue routine"));

        Diagnostic diagnostic = new Diagnostic("d_2", new Prediction("rice", 0.91));
        service.submit(diagnostic, build(0.91));

        JSONObject body = capture();
        assertEquals("rice", body.getString("cultivo_detectado"));
        assertEquals(0.91, body.getDouble("confianza"), 1e-9);
    }

    @Test
    public void testSubmitBodyHasAnswersNestedNotFlat() throws Exception {
        server.enqueue(success("Bajo", "OK", "OK"));

        Diagnostic diagnostic = new Diagnostic("d_3", new Prediction("rice", 0.8));
        service.submit(diagnostic, build(0.8));

        JSONObject body = capture();
        assertTrue(body.has("answers"));
        assertFalse("temperature must NOT be at top-level", body.has("temperature"));
        assertFalse("humidity must NOT be at top-level", body.has("humidity"));
        JSONObject answers = body.getJSONObject("answers");
        assertEquals("26-32C", answers.getString("temperature"));
        assertEquals("60-80%", answers.getString("humidity"));
        assertEquals("Today", answers.getString("rain"));
    }

    @Test
    public void testSubmitBodyCarriesEveryQuestionnaireKey() throws Exception {
        server.enqueue(success("Bajo", "OK", "OK"));

        Diagnostic diagnostic = new Diagnostic("d_4", new Prediction("rice", 0.8));
        service.submit(diagnostic, build(0.8));

        JSONObject answers = capture().getJSONObject("answers");
        assertEquals("26-32C", answers.getString("temperature"));
        assertEquals("60-80%", answers.getString("humidity"));
        assertEquals("Today", answers.getString("rain"));
        assertEquals("Moderate", answers.getString("soilMoisture"));
        assertEquals("5.5-7", answers.getString("ph"));
        assertEquals("Daily", answers.getString("irrigation"));
        assertEquals("1-2 weeks", answers.getString("fertilization"));
        assertEquals("No", answers.getString("spraying"));
        assertEquals("Light", answers.getString("weeds"));
        assertEquals("Yellow leaves", answers.getString("symptom"));
        assertEquals("Mild", answers.getString("severity"));
        assertEquals("Aphid", answers.getString("insects"));
        assertEquals("None", answers.getString("animals"));
    }

    @Test
    public void testSubmitParsesHighSeverityResponse() throws Exception {
        server.enqueue(success("Alto", "Severe leaf blight", "Apply fungicide weekly"));

        Diagnostic diagnostic = new Diagnostic("d_5", new Prediction("rice", 0.9));
        Diagnostic enriched = service.submit(diagnostic, build(0.9));

        SeverityCapturingVisitor visitor = new SeverityCapturingVisitor();
        enriched.label(visitor);
        assertTrue(visitor.recordedSevere());
    }

    @Test
    public void testSubmitParsesLowSeverityResponse() throws Exception {
        server.enqueue(success("Bajo", "Healthy", "Continue routine"));

        Diagnostic diagnostic = new Diagnostic("d_6", new Prediction("rice", 0.9));
        Diagnostic enriched = service.submit(diagnostic, build(0.9));

        SeverityCapturingVisitor visitor = new SeverityCapturingVisitor();
        enriched.label(visitor);
        assertTrue(visitor.recordedHealthy());
    }

    @Test
    public void testSubmitWithServer500ReturnsDiagnosticWithoutAssessment() {
        server.enqueue(new MockResponse().setResponseCode(500).setBody("{}"));

        Diagnostic diagnostic = new Diagnostic("d_7", new Prediction("rice", 0.9));
        Diagnostic enriched = service.submit(diagnostic, build(0.9));

        SeverityCapturingVisitor visitor = new SeverityCapturingVisitor();
        enriched.label(visitor);
        assertTrue("server failure must yield Pending assessment", visitor.recordedPending());
    }

    @Test
    public void testSubmitWithMalformedJsonReturnsDiagnosticWithoutAssessment() {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("not json"));

        Diagnostic diagnostic = new Diagnostic("d_8", new Prediction("rice", 0.9));
        Diagnostic enriched = service.submit(diagnostic, build(0.9));

        SeverityCapturingVisitor visitor = new SeverityCapturingVisitor();
        enriched.label(visitor);
        assertTrue(visitor.recordedPending());
    }

    @Test
    public void testSubmitDoesNotAttachQueryStringOrExtraHeaders() throws Exception {
        server.enqueue(success("Bajo", "OK", "OK"));

        Diagnostic diagnostic = new Diagnostic("d_9", new Prediction("rice", 0.8));
        service.submit(diagnostic, build(0.8));

        RecordedRequest recorded = server.takeRequest(2, TimeUnit.SECONDS);
        assertFalse("path must not include query string", Objects.requireNonNull(Objects.requireNonNull(recorded).getPath()).contains("?"));
    }

    private MockResponse success(String gravity, String shortDescription, String text) throws Exception {
        JSONObject report = new JSONObject()
                .put("gravedad", gravity)
                .put("descripcion_corta", shortDescription);
        JSONObject body = new JSONObject()
                .put("reporte_resumido", report)
                .put("texto_largo", text);
        return new MockResponse().setResponseCode(200).setBody(body.toString());
    }

    private JSONObject capture() throws Exception {
        RecordedRequest recorded = server.takeRequest(2, TimeUnit.SECONDS);
        return new JSONObject(Objects.requireNonNull(recorded).getBody().readUtf8());
    }

    private SubmissionRequest build(double confidence) {
        ImagePrediction prediction = new ImagePrediction("rice", confidence);
        Cultivation cultivation = new Cultivation("rice", "Vegetative");
        PhotographInput photograph = new PhotographInput("/tmp/test.jpg");
        DiagnosticSubject subject = new DiagnosticSubject(cultivation, photograph);
        Submission submission = new Submission(prediction, subject);
        return new SubmissionRequest(submission, survey());
    }

    private Questionnaire survey() {
        Weather weather = new Weather("26-32C", "60-80%", new Rainfall("Today"));
        SoilAnswer soil = new SoilAnswer("Moderate", "5.5-7");
        Condition condition = new Condition(weather, soil);

        Irrigation irrigation = new Irrigation("Daily", "1-2 weeks");
        PestControl pestControl = new PestControl("No", "Light");
        FarmManagement management = new FarmManagement(irrigation, pestControl);

        Symptom symptom = new Symptom("Yellow leaves", "Mild");
        Pest pest = new Pest("Aphid", "None");
        Observation observation = new Observation(symptom, pest);

        CropCare cropCare = new CropCare(management, observation);
        return new Questionnaire(condition, cropCare);
    }
}