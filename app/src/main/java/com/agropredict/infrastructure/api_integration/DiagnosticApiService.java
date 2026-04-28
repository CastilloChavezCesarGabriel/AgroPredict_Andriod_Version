package com.agropredict.infrastructure.api_integration;

import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;
import com.agropredict.application.service.IDiagnosticApiService;
import com.agropredict.application.visitor.ISubmissionVisitor;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.infrastructure.persistence.schema.IKeyConsumer;
import com.agropredict.infrastructure.persistence.schema.QuestionKey;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public final class DiagnosticApiService implements IDiagnosticApiService, ISubmissionVisitor, IKeyConsumer {
    private static final String ENDPOINT = "https://proyecto-diagnostico.onrender.com/diagnostic";
    private static final int CONNECTION_TIMEOUT = 30000;
    private static final int READ_TIMEOUT = 30000;
    private final JSONObject payload;

    public DiagnosticApiService() {
        this.payload = new JSONObject();
    }

    @Override
    public Diagnostic submit(Diagnostic diagnostic, SubmissionRequest request) {
        try {
            request.accept(this);
            JSONObject response = new JSONObject(send(payload));
            conclude(diagnostic, response);
            return diagnostic;
        } catch (IOException | JSONException exception) {
            return diagnostic;
        }
    }

    @Override
    public void visitPrediction(String predictedCrop, double confidence) {
        accept("cultivo_detectado", predictedCrop);
        accept("confianza", String.valueOf(confidence));
    }

    @Override
    public void visitEnvironment(String temperature, String humidity) {
        QuestionKey.TEMPERATURE.pair(this, temperature);
        QuestionKey.HUMIDITY.pair(this, humidity);
    }

    @Override
    public void visitRain(String precipitation) {
        QuestionKey.RAIN.pair(this, precipitation);
    }

    @Override
    public void visitSoil(String moisture, String acidity) {
        QuestionKey.SOIL_MOISTURE.pair(this, moisture);
        QuestionKey.PH.pair(this, acidity);
    }

    @Override
    public void visitIrrigation(String irrigation, String fertilization) {
        QuestionKey.IRRIGATION.pair(this, irrigation);
        QuestionKey.FERTILIZATION.pair(this, fertilization);
    }

    @Override
    public void visitPestControl(String spraying, String weeds) {
        QuestionKey.SPRAYING.pair(this, spraying);
        QuestionKey.WEEDS.pair(this, weeds);
    }

    @Override
    public void visitSymptom(String symptomType, String severity) {
        QuestionKey.SYMPTOM.pair(this, symptomType);
        QuestionKey.SEVERITY.pair(this, severity);
    }

    @Override
    public void visitPest(String insects, String animals) {
        QuestionKey.INSECTS.pair(this, insects);
        QuestionKey.ANIMALS.pair(this, animals);
    }

    @Override
    public void accept(String key, String value) {
        try {
            payload.put(key, value);
        } catch (JSONException ignored) {
        }
    }

    private void conclude(Diagnostic diagnostic, JSONObject response) {
        String severity = response.optString("severidad", "moderate");
        String summary = response.optString("reporte_resumido");
        String recommendation = response.optString("texto_largo");
        diagnostic.conclude(severity, summary, recommendation);
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
        URL url = new URL(ENDPOINT);
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