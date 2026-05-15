package com.agropredict.domain.diagnostic.recommendation;

public final class SummaryDraft {
    private final String mainProblem;
    private final String shortDescription;

    public SummaryDraft(String mainProblem, String shortDescription) {
        this.mainProblem = mainProblem == null ? "" : mainProblem;
        this.shortDescription = shortDescription == null ? "" : shortDescription;
    }

    public ISummary fold() {
        String text = combine();
        return text.isEmpty() ? new NoSummary() : new Summary(text);
    }

    private String combine() {
        if (mainProblem.isEmpty()) return shortDescription;
        if (shortDescription.isEmpty() || mainProblem.equals(shortDescription)) return mainProblem;
        return mainProblem + ". " + shortDescription;
    }
}
