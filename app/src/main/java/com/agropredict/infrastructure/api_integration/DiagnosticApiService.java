package com.agropredict.infrastructure.api_integration;

import android.util.Log;
import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;
import com.agropredict.application.service.IDiagnosticApiService;
import com.agropredict.application.visitor.IAnswerConsumer;
import com.agropredict.domain.diagnostic.visitor.IPredictionConsumer;
import com.agropredict.domain.diagnostic.Advice;
import com.agropredict.domain.diagnostic.ISeverityFactory;
import com.agropredict.domain.diagnostic.Recommendation;
import com.agropredict.domain.diagnostic.Severity;
import com.agropredict.domain.diagnostic.Summary;
import com.agropredict.domain.diagnostic.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public final class DiagnosticApiService implements IDiagnosticApiService, IPredictionConsumer, IAnswerConsumer {
    private static final String TAG = "DiagnosticApiService";
    private static final int CONNECTION_TIMEOUT = 30000;
    private static final int READ_TIMEOUT = 30000;
    private final String endpoint;
    private final ISeverityFactory severityFactory;
    private final JSONObject payload;
    private final JSONObject answers;

    public DiagnosticApiService(String endpoint, ISeverityFactory severityFactory) {
        this.endpoint = endpoint;
        this.severityFactory = severityFactory;
        this.payload = new JSONObject();
        this.answers = new JSONObject();
        try {
            payload.put("answers", answers);
        } catch (JSONException ignored) {
        }
    }

    @Override
    public Diagnostic submit(Diagnostic diagnostic, SubmissionRequest request) {
        try {
            request.dispatch(this);
            request.accept(this);
            JSONObject response = new JSONObject(send(payload));
            conclude(diagnostic, response);
            return diagnostic;
        } catch (IOException | JSONException exception) {
            Log.e(TAG, "Diagnostic API call failed for " + endpoint
                    + ". Diagnostic will be stored without API recommendations.", exception);
            return diagnostic;
        }
    }

    @Override
    public void classify(String predictedCrop, double confidence) {
        try {
            payload.put("cultivo_detectado", predictedCrop);
            payload.put("confianza", confidence);
        } catch (JSONException ignored) {
        }
    }

    @Override
    public void record(String key, String value) {
        try {
            answers.put(key, value);
        } catch (JSONException ignored) {
        }
    }

    private void conclude(Diagnostic diagnostic, JSONObject response) {
        JSONObject summaryJson = response.optJSONObject("reporte_resumido");
        String gravity = summaryJson == null ? "" : summaryJson.optString("gravedad", "");
        Severity severity = severityFactory.classify(gravity);
        String summaryText = summarize(summaryJson);
        Summary summary = summaryText.isEmpty() ? null : new Summary(summaryText);
        String adviceText = recommend(response);
        Advice advice = adviceText.isEmpty() ? null : new Advice(adviceText);
        diagnostic.conclude(severity, new Recommendation(summary, advice));
    }

    private String summarize(JSONObject summary) {
        if (summary == null) return "";
        String mainProblem = summary.optString("problema_principal", "");
        String shortDesc = summary.optString("descripcion_corta", "");
        if (mainProblem.isEmpty()) return shortDesc;
        if (shortDesc.isEmpty() || mainProblem.equals(shortDesc)) return mainProblem;
        return mainProblem + ". " + shortDesc;
    }

    private String recommend(JSONObject response) {
        StringBuilder builder = new StringBuilder();
        append(builder, collect(response, "Recomendaciones combinadas"));
        append(builder, collect(response, "Acciones para hoy"));
        if (builder.length() > 0) return builder.toString().trim();
        return response.optString("texto_largo", "");
    }

    private String collect(JSONObject response, String sectionTitle) {
        JSONArray sections = response.optJSONArray("secciones");
        if (sections == null) return "";
        for (int index = 0; index < sections.length(); index++) {
            JSONObject section = sections.optJSONObject(index);
            if (section == null) continue;
            if (!sectionTitle.equals(section.optString("titulo", ""))) continue;
            return format(sectionTitle, section.optJSONArray("items"));
        }
        return "";
    }

    private String format(String title, JSONArray items) {
        if (items == null || items.length() == 0) return "";
        StringBuilder builder = new StringBuilder(title).append(":\n");
        for (int index = 0; index < items.length(); index++) {
            String item = items.optString(index, "").trim();
            if (item.isEmpty()) continue;
            builder.append("• ").append(item).append('\n');
        }
        return builder.toString();
    }

    private void append(StringBuilder builder, String block) {
        if (block.isEmpty()) return;
        if (builder.length() > 0) builder.append('\n');
        builder.append(block);
    }

    private String send(JSONObject body) throws IOException {
        HttpURLConnection connection = open();
        try {
            write(connection, body);
            return read(connection);
        } finally {
            connection.disconnect();
        }
    }

    private HttpURLConnection open() throws IOException {
        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);
        connection.setDoOutput(true);
        return connection;
    }

    private void write(HttpURLConnection connection, JSONObject body) throws IOException {
        byte[] bodyBytes = body.toString().getBytes(StandardCharsets.UTF_8);
        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(bodyBytes);
            outputStream.flush();
        }
    }

    private String read(HttpURLConnection connection) throws IOException {
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("Server returned " + connection.getResponseCode());
        }
        return collect(connection);
    }

    private String collect(HttpURLConnection connection) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        }
        return builder.toString();
    }
}