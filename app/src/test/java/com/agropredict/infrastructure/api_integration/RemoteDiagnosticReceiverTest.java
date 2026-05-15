package com.agropredict.infrastructure.api_integration;

import static org.junit.Assert.assertTrue;
import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.domain.diagnostic.classification.Prediction;
import com.agropredict.domain.diagnostic.severity.GravitySeverityResolver;
import com.agropredict.domain.diagnostic.severity.HealthySeverity;
import com.agropredict.domain.diagnostic.severity.ISeverity;
import com.agropredict.domain.diagnostic.severity.ISeverityResolver;
import com.agropredict.domain.diagnostic.severity.ModerateSeverity;
import com.agropredict.domain.diagnostic.severity.PendingSeverity;
import com.agropredict.domain.diagnostic.severity.SevereSeverity;
import com.agropredict.domain.diagnostic.severity.SeverityClassifier;
import com.agropredict.domain.diagnostic.severity.UnknownSeverity;
import com.agropredict.visitor.AssessmentCapturingVisitor;
import com.agropredict.visitor.SeverityCapturingVisitor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import java.util.List;

public final class RemoteDiagnosticReceiverTest {
    private static final List<String> RECOMMENDATION_SECTION_TITLES = List.of(
            "Recomendaciones combinadas",
            "Acciones para hoy");
    private final ISeverityResolver severityResolver = build();

    private static ISeverityResolver build() {
        SeverityClassifier healthy = new SeverityClassifier(List.of("bajo", "low"),
                new HealthySeverity(() -> "Healthy"));
        SeverityClassifier moderate = new SeverityClassifier(List.of("moderado", "moderate"),
                new ModerateSeverity(() -> "Moderate issue"));
        SeverityClassifier severe = new SeverityClassifier(List.of("alto", "high", "critico", "critical"),
                new SevereSeverity(() -> "Severe issue"));
        ISeverity unknown = new UnknownSeverity(() -> "Analysis complete");
        return new GravitySeverityResolver(List.of(healthy, moderate, severe), unknown);
    }

    private RemoteDiagnosticReceiver newReceiver() {
        return new RemoteDiagnosticReceiver(severityResolver, RECOMMENDATION_SECTION_TITLES);
    }

    private Diagnostic newDiagnostic(String identifier, String crop, double confidence) {
        return Diagnostic.begin(identifier, new Prediction(crop, confidence), new PendingSeverity(() -> "Pending"));
    }

    @Test
    public void testReceiveExtractsHighSeverityFromSummaryReport() throws Exception {
        RemoteDiagnosticReceiver receiver = newReceiver();
        Diagnostic diagnostic = newDiagnostic("d_1", "rice", 0.9);

        JSONObject response = new JSONObject()
                .put("reporte_resumido", new JSONObject()
                        .put("gravedad", "Alto")
                        .put("descripcion_corta", "Severe leaf blight"))
                .put("texto_largo", "Apply fungicide weekly");
        Diagnostic enriched = receiver.receive(diagnostic, response);

        SeverityCapturingVisitor visitor = new SeverityCapturingVisitor();
        enriched.label(visitor);
        assertTrue(visitor.recordedSevere());
    }

    @Test
    public void testReceiveWithMissingSummaryReportYieldsUnknown() throws Exception {
        RemoteDiagnosticReceiver receiver = newReceiver();
        Diagnostic diagnostic = newDiagnostic("d_2", "rice", 0.9);
        JSONObject response = new JSONObject().put("texto_largo", "");
        Diagnostic enriched = receiver.receive(diagnostic, response);
        SeverityCapturingVisitor visitor = new SeverityCapturingVisitor();
        enriched.label(visitor);
        assertTrue(visitor.recordedUnknown());
    }

    @Test
    public void testReceiveJoinsMainProblemAndShortDescriptionForRicherSummary() throws Exception {
        RemoteDiagnosticReceiver receiver = newReceiver();
        Diagnostic diagnostic = newDiagnostic("d_rich_1", "tomato", 0.91);
        JSONObject response = new JSONObject()
                .put("reporte_resumido", new JSONObject()
                        .put("gravedad", "Alto")
                        .put("problema_principal", "Pulgon detectado en hojas")
                        .put("descripcion_corta", "Aplicar control localizado y vigilar follaje"));
        Diagnostic enriched = receiver.receive(diagnostic, response);

        AssessmentCapturingVisitor visitor = new AssessmentCapturingVisitor();
        enriched.summarize(visitor);
        enriched.recommend(visitor);
        assertTrue(visitor.summaryMentions("Pulgon detectado en hojas"));
        assertTrue(visitor.summaryMentions("Aplicar control localizado"));
    }

    @Test
    public void testReceiveOmitsDuplicateSummaryFragments() throws Exception {
        RemoteDiagnosticReceiver receiver = newReceiver();
        Diagnostic diagnostic = newDiagnostic("d_rich_2", "rice", 0.9);
        JSONObject response = new JSONObject()
                .put("reporte_resumido", new JSONObject()
                        .put("gravedad", "Bajo")
                        .put("problema_principal", "Sin problemas")
                        .put("descripcion_corta", "Sin problemas"));
        Diagnostic enriched = receiver.receive(diagnostic, response);

        AssessmentCapturingVisitor visitor = new AssessmentCapturingVisitor();
        enriched.summarize(visitor);
        enriched.recommend(visitor);
        assertTrue(visitor.recordedSummary("Sin problemas"));
    }

    @Test
    public void testReceiveBuildsRecommendationFromSeccionesBullets() throws Exception {
        RemoteDiagnosticReceiver receiver = newReceiver();
        Diagnostic diagnostic = newDiagnostic("d_rich_3", "tomato", 0.91);
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
        Diagnostic enriched = receiver.receive(diagnostic, response);

        AssessmentCapturingVisitor visitor = new AssessmentCapturingVisitor();
        enriched.summarize(visitor);
        enriched.recommend(visitor);
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
    public void testReceiveFallsBackToTextoLargoWhenSeccionesMissing() throws Exception {
        RemoteDiagnosticReceiver receiver = newReceiver();
        Diagnostic diagnostic = newDiagnostic("d_rich_4", "rice", 0.9);
        JSONObject response = new JSONObject()
                .put("reporte_resumido", new JSONObject()
                        .put("gravedad", "Bajo")
                        .put("descripcion_corta", "OK"))
                .put("texto_largo", "Diagnostico detallado para Arroz. Mantener practicas.");
        Diagnostic enriched = receiver.receive(diagnostic, response);

        AssessmentCapturingVisitor visitor = new AssessmentCapturingVisitor();
        enriched.summarize(visitor);
        enriched.recommend(visitor);
        assertTrue(visitor.recommendationMentions("Diagnostico detallado para Arroz"));
    }

    @Test
    public void testReceiveIgnoresUnrelatedSeccionesEntries() throws Exception {
        RemoteDiagnosticReceiver receiver = newReceiver();
        Diagnostic diagnostic = newDiagnostic("d_rich_5", "rice", 0.9);
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
        Diagnostic enriched = receiver.receive(diagnostic, response);

        AssessmentCapturingVisitor visitor = new AssessmentCapturingVisitor();
        enriched.summarize(visitor);
        enriched.recommend(visitor);
        assertTrue(visitor.recommendationMentions("Mantener riego"));
        assertTrue("Problemas section must not pollute recommendations",
                visitor.recommendationLacks("UNUSED PROBLEM ENTRY"));
    }
}
