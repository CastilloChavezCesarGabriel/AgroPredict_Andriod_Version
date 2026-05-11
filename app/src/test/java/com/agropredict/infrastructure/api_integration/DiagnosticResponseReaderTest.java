package com.agropredict.infrastructure.api_integration;

import static org.junit.Assert.assertTrue;
import com.agropredict.domain.diagnostic.ISeverityFactory;
import com.agropredict.domain.diagnostic.Prediction;
import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.domain.diagnostic.Severity;
import com.agropredict.domain.diagnostic.SeverityClassifier;
import com.agropredict.visitor.AssessmentCapturingVisitor;
import com.agropredict.visitor.SeverityCapturingVisitor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import java.util.List;

public final class DiagnosticResponseReaderTest {
    private final ISeverityFactory severityFactory = build();

    private static ISeverityFactory build() {
        SeverityClassifier healthy = new SeverityClassifier(List.of("bajo", "low"), new Severity("low", "Healthy", 0));
        SeverityClassifier moderate = new SeverityClassifier(List.of("moderado", "moderate"), new Severity("moderate", "Moderate issue", 1));
        SeverityClassifier severe = new SeverityClassifier(List.of("alto", "high", "critico", "critical"), new Severity("high", "Severe issue", 2));
        Severity unknown = new Severity(null, "Analysis complete", 0);
        return new GravitySeverityFactory(List.of(healthy, moderate, severe), unknown);
    }

    @Test
    public void testReadExtractsHighSeverityFromSummaryReport() throws Exception {
        DiagnosticResponseReader reader = new DiagnosticResponseReader(severityFactory);
        Diagnostic diagnostic = new Diagnostic("d_1", new Prediction("rice", 0.9));

        JSONObject response = new JSONObject()
                .put("reporte_resumido", new JSONObject()
                        .put("gravedad", "Alto")
                        .put("descripcion_corta", "Severe leaf blight"))
                .put("texto_largo", "Apply fungicide weekly");
        reader.read(diagnostic, response);

        SeverityCapturingVisitor visitor = new SeverityCapturingVisitor();
        diagnostic.label(visitor);
        assertTrue(visitor.recordedSevere());
    }

    @Test
    public void testReadWithMissingSummaryReportYieldsUnknown() throws Exception {
        DiagnosticResponseReader reader = new DiagnosticResponseReader(severityFactory);
        Diagnostic diagnostic = new Diagnostic("d_2", new Prediction("rice", 0.9));
        JSONObject response = new JSONObject().put("texto_largo", "");
        reader.read(diagnostic, response);
        SeverityCapturingVisitor visitor = new SeverityCapturingVisitor();
        diagnostic.label(visitor);
        assertTrue(visitor.recordedUnknown());
    }

    @Test
    public void testReadJoinsMainProblemAndShortDescriptionForRicherSummary() throws Exception {
        DiagnosticResponseReader reader = new DiagnosticResponseReader(severityFactory);
        Diagnostic diagnostic = new Diagnostic("d_rich_1", new Prediction("tomato", 0.91));
        JSONObject response = new JSONObject()
                .put("reporte_resumido", new JSONObject()
                        .put("gravedad", "Alto")
                        .put("problema_principal", "Pulgon detectado en hojas")
                        .put("descripcion_corta", "Aplicar control localizado y vigilar follaje"));
        reader.read(diagnostic, response);

        AssessmentCapturingVisitor visitor = new AssessmentCapturingVisitor();
        diagnostic.summarize(visitor);
        diagnostic.recommend(visitor);
        assertTrue(visitor.summaryMentions("Pulgon detectado en hojas"));
        assertTrue(visitor.summaryMentions("Aplicar control localizado"));
    }

    @Test
    public void testReadOmitsDuplicateSummaryFragments() throws Exception {
        DiagnosticResponseReader reader = new DiagnosticResponseReader(severityFactory);
        Diagnostic diagnostic = new Diagnostic("d_rich_2", new Prediction("rice", 0.9));
        JSONObject response = new JSONObject()
                .put("reporte_resumido", new JSONObject()
                        .put("gravedad", "Bajo")
                        .put("problema_principal", "Sin problemas")
                        .put("descripcion_corta", "Sin problemas"));
        reader.read(diagnostic, response);

        AssessmentCapturingVisitor visitor = new AssessmentCapturingVisitor();
        diagnostic.summarize(visitor);
        diagnostic.recommend(visitor);
        assertTrue(visitor.recordedSummary("Sin problemas"));
    }

    @Test
    public void testReadBuildsRecommendationFromSeccionesBullets() throws Exception {
        DiagnosticResponseReader reader = new DiagnosticResponseReader(severityFactory);
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
        reader.read(diagnostic, response);

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
    public void testReadFallsBackToTextoLargoWhenSeccionesMissing() throws Exception {
        DiagnosticResponseReader reader = new DiagnosticResponseReader(severityFactory);
        Diagnostic diagnostic = new Diagnostic("d_rich_4", new Prediction("rice", 0.9));
        JSONObject response = new JSONObject()
                .put("reporte_resumido", new JSONObject()
                        .put("gravedad", "Bajo")
                        .put("descripcion_corta", "OK"))
                .put("texto_largo", "Diagnostico detallado para Arroz. Mantener practicas.");
        reader.read(diagnostic, response);

        AssessmentCapturingVisitor visitor = new AssessmentCapturingVisitor();
        diagnostic.summarize(visitor);
        diagnostic.recommend(visitor);
        assertTrue(visitor.recommendationMentions("Diagnostico detallado para Arroz"));
    }

    @Test
    public void testReadIgnoresUnrelatedSeccionesEntries() throws Exception {
        DiagnosticResponseReader reader = new DiagnosticResponseReader(severityFactory);
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
        reader.read(diagnostic, response);

        AssessmentCapturingVisitor visitor = new AssessmentCapturingVisitor();
        diagnostic.summarize(visitor);
        diagnostic.recommend(visitor);
        assertTrue(visitor.recommendationMentions("Mantener riego"));
        assertTrue("Problemas section must not pollute recommendations",
                visitor.recommendationLacks("UNUSED PROBLEM ENTRY"));
    }
}
