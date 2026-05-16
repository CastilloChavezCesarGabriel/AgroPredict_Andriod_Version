package com.agropredict.infrastructure.api_integration;

import com.agropredict.domain.diagnostic.recommendation.Advice;
import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.domain.diagnostic.recommendation.IAdvice;
import com.agropredict.domain.diagnostic.severity.ISeverityResolver;
import com.agropredict.domain.diagnostic.recommendation.ISummary;
import com.agropredict.domain.diagnostic.recommendation.NoAdvice;
import com.agropredict.domain.diagnostic.recommendation.NoSummary;
import com.agropredict.domain.diagnostic.recommendation.Recommendation;
import com.agropredict.domain.diagnostic.severity.ISeverity;
import com.agropredict.domain.diagnostic.recommendation.SummaryDraft;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class RemoteDiagnosticReceiver {
    private static final String FIELD_SUMMARY = "reporte_resumido";
    private static final String FIELD_GRAVITY = "gravedad";
    private static final String FIELD_MAIN_PROBLEM = "problema_principal";
    private static final String FIELD_SHORT_DESCRIPTION = "descripcion_corta";
    private static final String FIELD_LONG_TEXT = "texto_largo";
    private static final String FIELD_SECTIONS = "secciones";
    private static final String FIELD_SECTION_TITLE = "titulo";
    private static final String FIELD_SECTION_ITEMS = "items";
    private final ISeverityResolver severityResolver;
    private final List<String> recommendationSectionTitles;

    public RemoteDiagnosticReceiver(ISeverityResolver severityResolver, List<String> recommendationSectionTitles) {
        this.severityResolver = Objects.requireNonNull(severityResolver,
                "remote diagnostic receiver requires a severity resolver");
        this.recommendationSectionTitles = List.copyOf(Objects.requireNonNull(recommendationSectionTitles,
                "remote diagnostic receiver requires recommendation section titles"));
    }

    public Diagnostic receive(Diagnostic diagnostic, JSONObject response) {
        JSONObject summaryJson = response.optJSONObject(FIELD_SUMMARY);
        String gravity = summaryJson == null ? "" : summaryJson.optString(FIELD_GRAVITY, "");
        ISeverity severity = severityResolver.classify(gravity);
        ISummary summary = draft(summaryJson);
        String adviceText = compose(response);
        IAdvice advice = adviceText.isEmpty() ? new NoAdvice() : new Advice(adviceText);
        return diagnostic.conclude(severity, new Recommendation(summary, advice));
    }

    private ISummary draft(JSONObject summaryJson) {
        if (summaryJson == null) return new NoSummary();
        String mainProblem = summaryJson.optString(FIELD_MAIN_PROBLEM, "");
        String shortDescription = summaryJson.optString(FIELD_SHORT_DESCRIPTION, "");
        return new SummaryDraft(mainProblem, shortDescription).fold();
    }

    private String compose(JSONObject response) {
        StringBuilder builder = new StringBuilder();
        for (String title : recommendationSectionTitles) {
            append(builder, pick(response, title));
        }
        if (builder.length() > 0) return builder.toString().trim();
        return response.optString(FIELD_LONG_TEXT, "");
    }

    private String pick(JSONObject response, String sectionTitle) {
        JSONArray sections = response.optJSONArray(FIELD_SECTIONS);
        if (sections == null) return "";
        for (int index = 0; index < sections.length(); index++) {
            JSONObject section = sections.optJSONObject(index);
            if (section == null) continue;
            if (!sectionTitle.equals(section.optString(FIELD_SECTION_TITLE, ""))) continue;
            return new AdviceSection(sectionTitle, extract(section.optJSONArray(FIELD_SECTION_ITEMS))).compose();
        }
        return "";
    }

    private List<String> extract(JSONArray jsonItems) {
        if (jsonItems == null) return List.of();
        List<String> items = new ArrayList<>();
        for (int index = 0; index < jsonItems.length(); index++) {
            items.add(jsonItems.optString(index, ""));
        }
        return items;
    }

    private void append(StringBuilder builder, String formattedSection) {
        if (formattedSection.isEmpty()) return;
        if (builder.length() > 0) builder.append('\n');
        builder.append(formattedSection);
    }
}