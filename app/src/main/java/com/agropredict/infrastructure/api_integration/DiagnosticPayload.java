package com.agropredict.infrastructure.api_integration;

import com.agropredict.application.IAnswerConsumer;
import com.agropredict.application.diagnostic_submission.request.SubmissionRequest;
import com.agropredict.domain.diagnostic.visitor.IPredictionConsumer;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class DiagnosticPayload implements IPredictionConsumer, IAnswerConsumer {
    private final JSONObject body;
    private final JSONObject answers;

    public DiagnosticPayload() {
        this.body = new JSONObject();
        this.answers = new JSONObject();
        store(body, "answers", answers);
    }

    public static DiagnosticPayload compose(SubmissionRequest request) {
        Objects.requireNonNull(request, "diagnostic payload requires a submission request");
        DiagnosticPayload payload = new DiagnosticPayload();
        request.dispatch(payload);
        request.accept(payload);
        return payload;
    }

    @Override
    public void classify(String predictedCrop, double confidence) {
        store(body, "cultivo_detectado", predictedCrop);
        store(body, "confianza", confidence);
    }

    @Override
    public void record(String key, String value) {
        store(answers, key, value);
    }

    public void write(OutputStream output) throws IOException {
        output.write(body.toString().getBytes(StandardCharsets.UTF_8));
    }

    private static void store(JSONObject json, String key, Object value) {
        try {
            json.put(key, value);
        } catch (JSONException impossible) {
            throw new RuntimeException(impossible);
        }
    }
}
