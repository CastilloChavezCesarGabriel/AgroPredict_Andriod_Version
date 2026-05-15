package com.agropredict.infrastructure.factory;

import com.agropredict.application.factory.IDiagnosticApiFactory;
import com.agropredict.application.service.IDiagnosticApiService;
import com.agropredict.domain.diagnostic.severity.ISeverityResolver;
import com.agropredict.infrastructure.api_integration.DiagnosticHttpGateway;
import com.agropredict.infrastructure.api_integration.RemoteDiagnosticAdapter;
import com.agropredict.infrastructure.api_integration.RemoteDiagnosticReceiver;
import java.util.List;
import java.util.Objects;

public final class AndroidDiagnosticApi implements IDiagnosticApiFactory {
    private static final String DIAGNOSTIC_ENDPOINT = "https://proyecto-diagnostico.onrender.com/diagnostic";
    private static final List<String> RECOMMENDATION_SECTION_TITLES = List.of("Recomendaciones combinadas",
            "Acciones para hoy");
    private final ISeverityResolver severityResolver;

    public AndroidDiagnosticApi(ISeverityResolver severityResolver) {
        this.severityResolver = Objects.requireNonNull(severityResolver,
                "android diagnostic api requires a severity resolver");
    }

    @Override
    public IDiagnosticApiService createApiService() {
        DiagnosticHttpGateway gateway = new DiagnosticHttpGateway(DIAGNOSTIC_ENDPOINT);
        RemoteDiagnosticReceiver receiver = new RemoteDiagnosticReceiver(severityResolver, RECOMMENDATION_SECTION_TITLES);
        return new RemoteDiagnosticAdapter(gateway, receiver);
    }
}