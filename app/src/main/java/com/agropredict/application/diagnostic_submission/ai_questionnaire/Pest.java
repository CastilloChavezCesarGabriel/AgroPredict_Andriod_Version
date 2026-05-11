package com.agropredict.application.diagnostic_submission.ai_questionnaire;

import com.agropredict.application.IAnswerConsumer;
import java.util.Objects;

public final class Pest {
    private final String insects;
    private final String animals;

    public Pest(String insects, String animals) {
        this.insects = Objects.requireNonNull(insects, "pest requires an insects answer");
        this.animals = Objects.requireNonNull(animals, "pest requires an animals answer");
    }

    public void accept(IAnswerConsumer consumer) {
        AnswerKey.INSECTS.pair(consumer, insects);
        AnswerKey.ANIMALS.pair(consumer, animals);
    }
}