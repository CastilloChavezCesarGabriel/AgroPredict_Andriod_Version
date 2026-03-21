package com.agropredict.application.request.data;

import com.agropredict.application.visitor.IQuestionnaireVisitor;

public final class PestControl {
    private final String spraying;
    private final String weeds;

    public PestControl(String spraying, String weeds) {
        this.spraying = spraying;
        this.weeds = weeds;
    }

    public void accept(IQuestionnaireVisitor visitor) {
        visitor.visitPestControl(spraying, weeds);
    }
}