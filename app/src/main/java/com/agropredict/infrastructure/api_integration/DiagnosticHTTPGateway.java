package com.agropredict.infrastructure.api_integration;

import com.agropredict.domain.guard.ArgumentPrecondition;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public final class DiagnosticHTTPGateway {
    private static final int CONNECTION_TIMEOUT = 30000;
    private static final int READ_TIMEOUT = 60000;
    private final String endpoint;

    public DiagnosticHTTPGateway(String endpoint) {
        this.endpoint = ArgumentPrecondition.validate(endpoint, "diagnostic http endpoint");
    }

    public JSONObject exchange(DiagnosticPayload payload) throws IOException, JSONException {
        HttpURLConnection connection = open();
        try {
            try (OutputStream output = connection.getOutputStream()) {
                payload.write(output);
                output.flush();
            }
            return new JSONObject(receive(connection));
        } finally {
            connection.disconnect();
        }
    }

    private HttpURLConnection open() throws IOException {
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URI(endpoint).toURL().openConnection();
        } catch (URISyntaxException invalid) {
            throw new IOException("Invalid diagnostic api endpoint: " + endpoint, invalid);
        }
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);
        connection.setDoOutput(true);
        return connection;
    }

    private String receive(HttpURLConnection connection) throws IOException {
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("Server returned " + connection.getResponseCode());
        }
        return drain(connection);
    }

    private String drain(HttpURLConnection connection) throws IOException {
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