package com.agropredict.domain.diagnostic;

import com.agropredict.domain.diagnostic.visitor.IClassificationResult;
import com.agropredict.domain.guard.ArgumentPrecondition;

public final class UnconfidentClassification implements IImageClassifierResult {
    private final String reason;

    public UnconfidentClassification(String reason) {
        this.reason = ArgumentPrecondition.validate(reason, "unconfident classification reason");
    }

    @Override
    public void accept(IClassificationResult visitor) {
        visitor.onReject(reason);
    }
}
