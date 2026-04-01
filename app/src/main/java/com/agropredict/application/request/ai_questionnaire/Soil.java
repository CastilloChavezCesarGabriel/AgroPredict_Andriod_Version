package com.agropredict.application.request.ai_questionnaire;

import com.agropredict.application.visitor.IQuestionnaireVisitor;

public final class Soil {
    private final String moisture;
    private final String acidity;

    public Soil(String moisture, String acidity) {
        this.moisture = moisture;
        this.acidity = acidity;
    }

    public void accept(IQuestionnaireVisitor visitor) {
        visitor.visitSoil(moisture, acidity);
    }
}