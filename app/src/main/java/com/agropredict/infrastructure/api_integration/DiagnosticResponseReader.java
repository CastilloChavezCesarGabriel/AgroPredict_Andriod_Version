package com.agropredict.infrastructure.api_integration;

import com.agropredict.domain.diagnostic.Advice;
import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.domain.diagnostic.ISeverityFactory;
import com.agropredict.domain.diagnostic.Recommendation;
import com.agropredict.domain.diagnostic.Severity;
import com.agropredict.domain.diagnostic.Summary;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Objects;

public final class DiagnosticResponseReader {
    private final ISeverityFactory severityFactory;

    public DiagnosticResponseReader(ISeverityFactory severityFactory) {
        this.severityFactory = Objects.requireNonNull(severityFactory, "diagnostic response reader requires a severity factory");
    }

    public void read(Diagnostic diagnostic, JSONObject response) {
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
        append(builder, pick(response, "Recomendaciones combinadas"));
        append(builder, pick(response, "Acciones para hoy"));
        if (builder.length() > 0) return builder.toString().trim();
        return response.optString("texto_largo", "");
    }

    private String pick(JSONObject response, String sectionTitle) {
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
}