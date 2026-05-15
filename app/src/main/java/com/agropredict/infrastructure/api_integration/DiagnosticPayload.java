package com.agropredict.infrastructure.api_integration;

import com.agropredict.application.diagnostic_submission.request.SubmissionRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class DiagnosticPayload {
    private final JSONObject body;

    public DiagnosticPayload(SubmissionRequest request) {
        Objects.requireNonNull(request,
                "diagnostic payload requires a submission request");
        this.body = new JSONObject();
        JSONObject answers = new JSONObject();
        put(body, "answers", answers);
        request.dispatch((predictedCrop, confidence) -> {
            put(body, "cultivo_detectado", predictedCrop);
            put(body, "confianza", confidence);
        });
        request.accept((key, value) -> put(answers, key, value));
    }

    public void write(OutputStream output) throws IOException {
        output.write(body.toString().getBytes(StandardCharsets.UTF_8));
    }

    private void put(JSONObject json, String key, Object value) {
        try {
            json.put(key, value);
        } catch (JSONException nonFiniteValue) {
            throw new IllegalStateException("invalid json key/value: " + key, nonFiniteValue);
        }
    }
}
