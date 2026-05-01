package com.agropredict.application.request.ai_questionnaire;

import com.agropredict.application.visitor.IQuestionnaireVisitor;

public final class SoilAnswer {
    private final String moisture;
    private final String acidity;

    public SoilAnswer(String moisture, String acidity) {
        this.moisture = moisture;
        this.acidity = acidity;
    }

    public void accept(IQuestionnaireVisitor visitor) {
        visitor.visitSoil(moisture, acidity);
    }
}