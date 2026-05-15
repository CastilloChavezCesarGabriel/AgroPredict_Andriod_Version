package com.agropredict.application.diagnostic_submission.rejection;

import com.agropredict.application.service.IImageRejectionPhrase;
import com.agropredict.domain.diagnostic.visitor.IClassificationResult;
import java.util.Objects;

public final class PhraseRejection implements IImageRejection {
    private final IImageRejectionPhrase phrase;
    private final String detail;

    public PhraseRejection(IImageRejectionPhrase phrase, String detail) {
        this.phrase = Objects.requireNonNull(phrase, "phrase rejection requires a phrase");
        this.detail = detail == null ? "" : detail;
    }

    @Override
    public void encode(IClassificationResult visitor) {
        visitor.onReject(phrase.describe(detail));
    }
}
