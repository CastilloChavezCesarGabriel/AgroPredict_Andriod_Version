package com.agropredict.application.diagnostic_submission.ai_questionnaire;

import com.agropredict.application.IAnswerConsumer;
import java.util.Objects;

public final class PestControl {
    private final String spraying;
    private final String weeds;

    public PestControl(String spraying, String weeds) {
        this.spraying = Objects.requireNonNull(spraying, "pest control requires a spraying answer");
        this.weeds = Objects.requireNonNull(weeds, "pest control requires a weeds answer");
    }

    public void accept(IAnswerConsumer consumer) {
        AnswerKey.SPRAYING.pair(consumer, spraying);
        AnswerKey.WEEDS.pair(consumer, weeds);
    }
}