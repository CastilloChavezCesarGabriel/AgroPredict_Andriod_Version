package com.agropredict.application.request.ai_questionnaire;

import com.agropredict.application.visitor.IQuestionnaireVisitor;

public final class Pest {
    private final String insects;
    private final String animals;

    public Pest(String insects, String animals) {
        this.insects = insects;
        this.animals = animals;
    }

    public void accept(IQuestionnaireVisitor visitor) {
        visitor.visitPest(insects, animals);
    }
}