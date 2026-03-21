package com.agropredict.infrastructure.network;

import com.agropredict.application.request.SubmissionRequest;
import com.agropredict.application.service.IDiagnosticApiService;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.domain.component.diagnostic.DiagnosticAssessment;
import com.agropredict.domain.component.diagnostic.DiagnosticContent;
import com.agropredict.domain.component.diagnostic.DiagnosticData;
import com.agropredict.domain.component.diagnostic.DiagnosticOwnership;
import com.agropredict.domain.component.diagnostic.DiagnosticSummary;
import com.agropredict.domain.component.diagnostic.Prediction;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticVisitor;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public final class DiagnosticApiService implements IDiagnosticApiService,
        IDiagnosticVisitor {

    private static final String ENDPOINT = "https://proyecto-diagnostico.onrender.com/diagnostic";
    private static final int CONNECTION_TIMEOUT = 30000;
    private static final int READ_TIMEOUT = 30000;
    private JSONObject pendingResponse;
    private Diagnostic enrichedDiagnostic;

    @Override
    public Diagnostic submit(Diagnostic diagnostic, SubmissionRequest request) {
        try {
            RequestExtractor extractor = new RequestExtractor();
            String response = send(extractor.extract(request));
            this.pendingResponse = new JSONObject(response);
            diagnostic.accept(this);
            return enrichedDiagnostic;
        } catch (IOException | org.json.JSONException exception) {
            return diagnostic;
        }
    }

    @Override
    public void visit(String identifier, DiagnosticData data) {
        Prediction updated = predict();
        DiagnosticAssessment assessment = assess();
        DiagnosticContent content = new DiagnosticContent(null, assessment);
        DiagnosticData enrichedData = new DiagnosticData(updated, content);
        enrichedDiagnostic = Diagnostic.create(identifier, enrichedData);
    }

    private DiagnosticAssessment assess() {
        DiagnosticSummary summary = summarize();
        String recommendation = pendingResponse.optString("texto_largo");
        DiagnosticOwnership ownership = new DiagnosticOwnership(null, recommendation);
        return new DiagnosticAssessment(summary, ownership);
    }

    private Prediction predict() {
        String predictedCrop = pendingResponse.optString("cultivo_detectado");
        double confidence = pendingResponse.optDouble("confianza");
        return new Prediction(predictedCrop, confidence);
    }

    private DiagnosticSummary summarize() {
        String summary = pendingResponse.optString("reporte_resumido");
        return new DiagnosticSummary("moderate", summary);
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
