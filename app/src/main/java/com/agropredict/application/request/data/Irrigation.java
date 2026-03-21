package com.agropredict.application.request.data;

import com.agropredict.application.visitor.IQuestionnaireVisitor;

public final class Irrigation {
    private final String irrigation;
    private final String fertilization;

    public Irrigation(String irrigation, String fertilization) {
        this.irrigation = irrigation;
        this.fertilization = fertilization;
    }

    public void accept(IQuestionnaireVisitor visitor) {
        visitor.visitIrrigation(irrigation, fertilization);
    }
}