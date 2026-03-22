package com.agropredict.infrastructure.network;

import com.agropredict.application.request.SubmissionRequest;
import com.agropredict.application.service.IDiagnosticApiService;
import com.agropredict.application.visitor.ISubmissionVisitor;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.domain.component.diagnostic.DiagnosticAssessment;
import com.agropredict.domain.component.diagnostic.DiagnosticContent;
import com.agropredict.domain.component.diagnostic.DiagnosticData;
import com.agropredict.domain.component.diagnostic.DiagnosticOwnership;
import com.agropredict.domain.component.diagnostic.DiagnosticSummary;
import com.agropredict.domain.component.diagnostic.Prediction;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public final class DiagnosticApiService implements IDiagnosticApiService, ISubmissionVisitor {

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
            String[] holder = new String[1];
            diagnostic.accept((identifier, data) -> holder[0] = identifier);
            return parse(holder[0], response);
        } catch (IOException | JSONException exception) {
            return diagnostic;
        }
    }

    @Override
    public void visitPrediction(String predictedCrop, double confidence) {
        append("cultivo_detectado", predictedCrop);
        append("confianza", String.valueOf(confidence));
    }

    @Override
    public void visitEnvironment(String temperature, String humidity) {
        append("temperature", temperature);
        append("humidity", humidity);
    }

    @Override
    public void visitRain(String precipitation) {
        append("rain", precipitation);
    }

    @Override
    public void visitSoil(String moisture, String acidity) {
        append("soilMoisture", moisture);
        append("ph", acidity);
    }

    @Override
    public void visitIrrigation(String irrigation, String fertilization) {
        append("irrigation", irrigation);
        append("fertilization", fertilization);
    }

    @Override
    public void visitPestControl(String spraying, String weeds) {
        append("spraying", spraying);
        append("weeds", weeds);
    }

    @Override
    public void visitSymptom(String symptomType, String severity) {
        append("symptom", symptomType);
        append("severity", severity);
    }

    @Override
    public void visitPest(String insects, String animals) {
        append("insects", insects);
        append("animals", animals);
    }

    private void append(String key, String value) {
        try {
            payload.put(key, value);
        } catch (JSONException ignored) {
        }
    }

    private Diagnostic parse(String identifier, JSONObject response) {
        String crop = response.optString("cultivo_detectado");
        double confidence = response.optDouble("confianza");
        String severity = response.optString("severidad", "moderate");
        String summary = response.optString("reporte_resumido");
        String recommendation = response.optString("texto_largo");
        Prediction prediction = new Prediction(crop, confidence);
        DiagnosticSummary diagnosticSummary = new DiagnosticSummary(severity, summary);
        DiagnosticOwnership ownership = new DiagnosticOwnership(null, recommendation);
        DiagnosticAssessment assessment = new DiagnosticAssessment(diagnosticSummary, ownership);
        DiagnosticContent content = new DiagnosticContent(null, assessment);
        DiagnosticData data = new DiagnosticData(prediction, content);
        return Diagnostic.create(identifier, data);
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