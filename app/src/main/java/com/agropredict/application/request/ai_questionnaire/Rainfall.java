package com.agropredict.application.request.ai_questionnaire;

import com.agropredict.application.visitor.IQuestionnaireVisitor;

public final class Rainfall {
    private final String precipitation;

    public Rainfall(String precipitation) {
        this.precipitation = precipitation;
    }

    public void accept(IQuestionnaireVisitor visitor) {
        visitor.visitRain(precipitation);
    }
}