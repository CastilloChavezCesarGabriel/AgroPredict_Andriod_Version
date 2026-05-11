package com.agropredict.infrastructure.api_integration;

import android.util.Log;
import com.agropredict.application.diagnostic_submission.request.SubmissionRequest;
import com.agropredict.application.service.IDiagnosticApiService;
import com.agropredict.domain.diagnostic.Diagnostic;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Objects;

public final class DiagnosticApiService implements IDiagnosticApiService {
    private static final String TAG = "DiagnosticApiService";
    private final DiagnosticHTTPGateway gateway;
    private final DiagnosticResponseReader responseReader;

    public DiagnosticApiService(DiagnosticHTTPGateway gateway, DiagnosticResponseReader responseReader) {
        this.gateway = Objects.requireNonNull(gateway, "diagnostic api service requires a gateway");
        this.responseReader = Objects.requireNonNull(responseReader, "diagnostic api service requires a response reader");
    }

    @Override
    public Diagnostic submit(Diagnostic diagnostic, SubmissionRequest request) {
        try {
            DiagnosticPayload payload = DiagnosticPayload.compose(request);
            JSONObject response = gateway.exchange(payload);
            responseReader.read(diagnostic, response);
            return diagnostic;
        } catch (IOException | JSONException exception) {
            Log.e(TAG, "Diagnostic API call failed. " +
                    "Diagnostic will be stored without API recommendations.", exception);
            return diagnostic;
        }
    }
}
