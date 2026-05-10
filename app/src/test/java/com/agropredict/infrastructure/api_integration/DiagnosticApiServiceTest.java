package com.agropredict.infrastructure.api_integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.agropredict.domain.diagnostic.ISeverityFactory;
import com.agropredict.domain.diagnostic.Prediction;
import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.visitor.AssessmentCapturingVisitor;
import com.agropredict.visitor.SeverityCapturingVisitor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class DiagnosticApiServiceTest {
    private final ISeverityFactory severityFactory = new GravitySeverityFactory();

    private JSONObject read(DiagnosticApiService service, String fieldName) throws Exception {
        Field field = DiagnosticApiService.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (JSONObject) field.get(service);
    }

    private void conclude(DiagnosticApiService service, Diagnostic diagnostic, JSONObject response) throws Exception {
        Method method = DiagnosticApiService.class.getDeclaredMethod("conclude", Diagnostic.class, JSONObject.class);
        method.setAccessible(true);
        method.invoke(service, diagnostic, response);
    }

    @Test
    public void testPayloadCarriesPredictionAtTopLevel() throws Exception {
        DiagnosticApiService service = new DiagnosticApiService("http://unused.test", severityFactory);
        service.classify("rice", 0.87);

        JSONObject payload = read(service, "payload");
        assertEquals("rice", payload.getString("cultivo_detectado"));
        assertEquals(0.87, payload.getDouble("confianza"), 1e-9);
    }

    @Test
    public void testConfidenceIsNumericNotString() throws Exception {
        DiagnosticApiService service = new DiagnosticApiService("http://unused.test", severityFactory);
        service.classify("tomato", 0.5);

        JSONObject payload = read(service, "payload");
        Object confidence = payload.get("confianza");
        assertTrue("Confidence must be numeric", confidence instanceof Number);
    }

    @Test
    public void testAnswersAreNestedNotFlat() throws Exception {
        DiagnosticApiService service = new DiagnosticApiService("http://unused.test", severityFactory);
        service.record("temperature", "hot");
        service.record("humidity", "humid");
        JSONObject payload = read(service, "payload");
        assertTrue(payload.has("answers"));
        assertFalse("environment keys must NOT be on top-level", payload.has("temperature"));
        JSONObject answers = payload.getJSONObject("answers");
        assertEquals("hot", answers.getString("temperature"));
        assertEquals("humid", answers.getString("humidity"));
    }

    @Test
    public void testEveryQuestionnaireSectionFlowsIntoAnswers() throws Exception {
        DiagnosticApiService service = new DiagnosticApiService("http://unused.test", severityFactory);
        service.record("temperature", "26-32C");
        service.record("humidity", "60-80%");
        service.record("rain", "Today");
        service.record("soilMoisture", "Moderate");
        service.record("ph", "5.5-7");
        service.record("irrigation", "Daily");
        service.record("fertilization", "1-2 weeks");
        service.record("spraying", "No");
        service.record("weeds", "Light");
        service.record("symptom", "Yellow leaves");
        service.record("severity", "Mild");
        service.record("insects", "Aphid");
        service.record("animals", "None");

        JSONObject answers = read(service, "answers");
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
    public void testConcludeExtractsHighSeverityFromSummaryReport() throws Exception {
        DiagnosticApiService service = new DiagnosticApiService("http://unused.test", severityFactory);
        Diagnostic diagnostic = new Diagnostic("d_1", new Prediction("rice", 0.9));

        JSONObject response = new JSONObject()
                .put("reporte_resumido", new JSONObject()
                        .put("gravedad", "Alto")
                        .put("descripcion_corta", "Severe leaf blight"))
                .put("texto_largo", "Apply fungicide weekly");
        conclude(service, diagnostic, response);

        SeverityCapturingVisitor visitor = new SeverityCapturingVisitor();
        diagnostic.label(visitor);
        assertTrue(visitor.recordedSevere());
    }

    @Test
    public void testConcludeWithMissingSummaryReportYieldsUnknown() throws Exception {
        DiagnosticApiService service = new DiagnosticApiService("http://unused.test", severityFactory);
        Diagnostic diagnostic = new Diagnostic("d_2", new Prediction("rice", 0.9));
        JSONObject response = new JSONObject().put("texto_largo", "");
        conclude(service, diagnostic, response);
        SeverityCapturingVisitor visitor = new SeverityCapturingVisitor();
        diagnostic.label(visitor);
        assertTrue(visitor.recordedUnknown());
    }

    @Test
    public void testPayloadStructureSatisfiesServerContract() throws Exception {
        DiagnosticApiService service = new DiagnosticApiService("http://unused.test", severityFactory);
        service.classify("wheat", 0.72);
        service.record("temperature", "15-25C");
        service.record("humidity", "40-60%");
        service.record("rain", "This week");

        JSONObject payload = read(service, "payload");
        assertTrue(payload.has("cultivo_detectado"));
        assertTrue(payload.has("confianza"));
        assertTrue(payload.has("answers"));
        assertFalse(payload.has("temperature"));
        assertFalse(payload.has("humidity"));
        assertFalse(payload.has("rain"));
    }

    @Test
    public void testConcludeJoinsMainProblemAndShortDescriptionForRicherSummary() throws Exception {
        DiagnosticApiService service = new DiagnosticApiService("http://unused.test", severityFactory);
        Diagnostic diagnostic = new Diagnostic("d_rich_1", new Prediction("tomato", 0.91));
        JSONObject response = new JSONObject()
                .put("reporte_resumido", new JSONObject()
                        .put("gravedad", "Alto")
                        .put("problema_principal", "Pulgon detectado en hojas")
                        .put("descripcion_corta", "Aplicar control localizado y vigilar follaje"));
        conclude(service, diagnostic, response);

        AssessmentCapturingVisitor visitor = new AssessmentCapturingVisitor();
        diagnostic.summarize(visitor);
        diagnostic.recommend(visitor);
        assertTrue(visitor.summaryMentions("Pulgon detectado en hojas"));
        assertTrue(visitor.summaryMentions("Aplicar control localizado"));
    }

    @Test
    public void testConcludeOmitsDuplicateSummaryFragments() throws Exception {
        DiagnosticApiService service = new DiagnosticApiService("http://unused.test", severityFactory);
        Diagnostic diagnostic = new Diagnostic("d_rich_2", new Prediction("rice", 0.9));
        JSONObject response = new JSONObject()
                .put("reporte_resumido", new JSONObject()
                        .put("gravedad", "Bajo")
                        .put("problema_principal", "Sin problemas")
                        .put("descripcion_corta", "Sin problemas"));
        conclude(service, diagnostic, response);

        AssessmentCapturingVisitor visitor = new AssessmentCapturingVisitor();
        diagnostic.summarize(visitor);
        diagnostic.recommend(visitor);
        assertTrue(visitor.recordedSummary("Sin problemas"));
    }

    @Test
    public void testConcludeBuildsRecommendationFromSeccionesBullets() throws Exception {
        DiagnosticApiService service = new DiagnosticApiService("http://unused.test", severityFactory);
        Diagnostic diagnostic = new Diagnostic("d_rich_3", new Prediction("tomato", 0.91));
        JSONObject response = new JSONObject()
                .put("reporte_resumido", new JSONObject()
                        .put("gravedad", "Alto")
                        .put("descripcion_corta", "Manchas en hojas"))
                .put("secciones", new JSONArray()
                        .put(new JSONObject()
                                .put("titulo", "Recomendaciones combinadas")
                                .put("items", new JSONArray()
                                        .put("Regar a nivel de suelo para no mojar follaje.")
                                        .put("Aplicar fungicida y mejorar ventilacion.")
                                        .put("Usar trampas cromaticas para trips.")))
                        .put(new JSONObject()
                                .put("titulo", "Acciones para hoy")
                                .put("items", new JSONArray()
                                        .put("Regar a nivel de suelo para no mojar follaje.")
                                        .put("Aplicar fungicida y mejorar ventilacion."))))
                .put("texto_largo", "BOILERPLATE THAT MUST NOT BE USED");
        conclude(service, diagnostic, response);

        AssessmentCapturingVisitor visitor = new AssessmentCapturingVisitor();
        diagnostic.summarize(visitor);
        diagnostic.recommend(visitor);
        assertTrue(visitor.recommendationMentions("Recomendaciones combinadas"));
        assertTrue(visitor.recommendationMentions("Acciones para hoy"));
        assertTrue(visitor.recommendationMentions("Regar a nivel de suelo"));
        assertTrue(visitor.recommendationMentions("Aplicar fungicida"));
        assertTrue(visitor.recommendationMentions("Usar trampas cromaticas"));
        assertTrue(visitor.recommendationMentions("• "));
        assertTrue("texto_largo must NOT leak when secciones is present",
                visitor.recommendationLacks("BOILERPLATE"));
    }

    @Test
    public void testConcludeFallsBackToTextoLargoWhenSeccionesMissing() throws Exception {
        DiagnosticApiService service = new DiagnosticApiService("http://unused.test", severityFactory);
        Diagnostic diagnostic = new Diagnostic("d_rich_4", new Prediction("rice", 0.9));
        JSONObject response = new JSONObject()
                .put("reporte_resumido", new JSONObject()
                        .put("gravedad", "Bajo")
                        .put("descripcion_corta", "OK"))
                .put("texto_largo", "Diagnostico detallado para Arroz. Mantener practicas.");
        conclude(service, diagnostic, response);

        AssessmentCapturingVisitor visitor = new AssessmentCapturingVisitor();
        diagnostic.summarize(visitor);
        diagnostic.recommend(visitor);
        assertTrue(visitor.recommendationMentions("Diagnostico detallado para Arroz"));
    }

    @Test
    public void testConcludeIgnoresUnrelatedSeccionesEntries() throws Exception {
        DiagnosticApiService service = new DiagnosticApiService("http://unused.test", severityFactory);
        Diagnostic diagnostic = new Diagnostic("d_rich_5", new Prediction("rice", 0.9));
        JSONObject response = new JSONObject()
                .put("reporte_resumido", new JSONObject()
                        .put("gravedad", "Bajo")
                        .put("descripcion_corta", "OK"))
                .put("secciones", new JSONArray()
                        .put(new JSONObject()
                                .put("titulo", "Problemas detectados")
                                .put("items", new JSONArray().put("UNUSED PROBLEM ENTRY")))
                        .put(new JSONObject()
                                .put("titulo", "Recomendaciones combinadas")
                                .put("items", new JSONArray().put("Mantener riego."))));
        conclude(service, diagnostic, response);

        AssessmentCapturingVisitor visitor = new AssessmentCapturingVisitor();
        diagnostic.summarize(visitor);
        diagnostic.recommend(visitor);
        assertTrue(visitor.recommendationMentions("Mantener riego"));
        assertTrue("Problemas section must not pollute recommendations",
                visitor.recommendationLacks("UNUSED PROBLEM ENTRY"));
    }
}