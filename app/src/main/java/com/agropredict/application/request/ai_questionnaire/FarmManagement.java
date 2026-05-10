package com.agropredict.application.request.ai_questionnaire;

import com.agropredict.application.visitor.IAnswerConsumer;
import java.util.Objects;

public final class FarmManagement {
    private final Irrigation irrigation;
    private final PestControl pestControl;

    public FarmManagement(Irrigation irrigation, PestControl pestControl) {
        this.irrigation = Objects.requireNonNull(irrigation, "farm management requires an irrigation answer");
        this.pestControl = Objects.requireNonNull(pestControl, "farm management requires a pest control answer");
    }

    public void accept(IAnswerConsumer consumer) {
        irrigation.accept(consumer);
        pestControl.accept(consumer);
    }
}