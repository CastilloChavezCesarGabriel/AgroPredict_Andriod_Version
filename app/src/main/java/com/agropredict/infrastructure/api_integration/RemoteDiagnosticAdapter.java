package com.agropredict.infrastructure.api_integration;

import android.util.Log;
import com.agropredict.application.diagnostic_submission.request.SubmissionRequest;
import com.agropredict.application.service.IDiagnosticApiService;
import com.agropredict.domain.diagnostic.Diagnostic;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Objects;

public final class RemoteDiagnosticAdapter implements IDiagnosticApiService {
    private static final String TAG = "RemoteDiagnosticAdapter";
    private final DiagnosticHttpGateway gateway;
    private final RemoteDiagnosticReceiver receiver;

    public RemoteDiagnosticAdapter(DiagnosticHttpGateway gateway, RemoteDiagnosticReceiver receiver) {
        this.gateway = Objects.requireNonNull(gateway,
                "remote diagnostic adapter requires a gateway");
        this.receiver = Objects.requireNonNull(receiver,
                "remote diagnostic adapter requires a remote diagnostic receiver");
    }

    @Override
    public Diagnostic submit(Diagnostic diagnostic, SubmissionRequest request) {
        try {
            DiagnosticPayload payload = new DiagnosticPayload(request);
            JSONObject response = gateway.post(payload);
            return receiver.receive(diagnostic, response);
        } catch (IOException | JSONException exception) {
            Log.e(TAG, "Remote diagnostic call failed. "
                    + "Diagnostic will be stored without API recommendations.", exception);
            return diagnostic;
        }
    }
}
