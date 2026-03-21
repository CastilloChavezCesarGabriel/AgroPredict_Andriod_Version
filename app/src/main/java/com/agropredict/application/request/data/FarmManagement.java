package com.agropredict.application.request.data;

import com.agropredict.application.visitor.IQuestionnaireVisitor;

public final class FarmManagement {
    private final Irrigation irrigation;
    private final PestControl pestControl;

    public FarmManagement(Irrigation irrigation, PestControl pestControl) {
        this.irrigation = irrigation;
        this.pestControl = pestControl;
    }

    public void accept(IQuestionnaireVisitor visitor) {
        irrigation.accept(visitor);
        pestControl.accept(visitor);
    }
}