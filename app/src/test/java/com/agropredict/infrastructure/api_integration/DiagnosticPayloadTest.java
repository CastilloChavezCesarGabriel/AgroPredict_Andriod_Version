package com.agropredict.infrastructure.api_integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.json.JSONObject;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public final class DiagnosticPayloadTest {
    private JSONObject serialize(DiagnosticPayload payload) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        payload.write(output);
        return new JSONObject(output.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void testPayloadCarriesPredictionAtTopLevel() throws Exception {
        DiagnosticPayload payload = new DiagnosticPayload();
        payload.classify("rice", 0.87);

        JSONObject body = serialize(payload);
        assertEquals("rice", body.getString("cultivo_detectado"));
        assertEquals(0.87, body.getDouble("confianza"), 1e-9);
    }

    @Test
    public void testConfidenceIsNumericNotString() throws Exception {
        DiagnosticPayload payload = new DiagnosticPayload();
        payload.classify("tomato", 0.5);

        JSONObject body = serialize(payload);
        Object confidence = body.get("confianza");
        assertTrue("Confidence must be numeric", confidence instanceof Number);
    }

    @Test
    public void testAnswersAreNestedNotFlat() throws Exception {
        DiagnosticPayload payload = new DiagnosticPayload();
        payload.record("temperature", "hot");
        payload.record("humidity", "humid");

        JSONObject body = serialize(payload);
        assertTrue(body.has("answers"));
        assertFalse("environment keys must NOT be on top-level", body.has("temperature"));
        JSONObject answers = body.getJSONObject("answers");
        assertEquals("hot", answers.getString("temperature"));
        assertEquals("humid", answers.getString("humidity"));
    }

    @Test
    public void testEveryQuestionnaireSectionFlowsIntoAnswers() throws Exception {
        DiagnosticPayload payload = new DiagnosticPayload();
        payload.record("temperature", "26-32C");
        payload.record("humidity", "60-80%");
        payload.record("rain", "Today");
        payload.record("soilMoisture", "Moderate");
        payload.record("ph", "5.5-7");
        payload.record("irrigation", "Daily");
        payload.record("fertilization", "1-2 weeks");
        payload.record("spraying", "No");
        payload.record("weeds", "Light");
        payload.record("symptom", "Yellow leaves");
        payload.record("severity", "Mild");
        payload.record("insects", "Aphid");
        payload.record("animals", "None");

        JSONObject answers = serialize(payload).getJSONObject("answers");
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
    public void testPayloadStructureSatisfiesServerContract() throws Exception {
        DiagnosticPayload payload = new DiagnosticPayload();
        payload.classify("wheat", 0.72);
        payload.record("temperature", "15-25C");
        payload.record("humidity", "40-60%");
        payload.record("rain", "This week");

        JSONObject body = serialize(payload);
        assertTrue(body.has("cultivo_detectado"));
        assertTrue(body.has("confianza"));
        assertTrue(body.has("answers"));
        assertFalse(body.has("temperature"));
        assertFalse(body.has("humidity"));
        assertFalse(body.has("rain"));
    }
}
