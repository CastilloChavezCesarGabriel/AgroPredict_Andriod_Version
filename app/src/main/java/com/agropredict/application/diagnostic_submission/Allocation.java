package com.agropredict.application.diagnostic_submission;

public final class Allocation {
    private final String cropIdentifier;
    private final String imageIdentifier;

    public Allocation(String cropIdentifier, String imageIdentifier) {
        this.cropIdentifier = cropIdentifier;
        this.imageIdentifier = imageIdentifier;
    }

    public void expose(IIdentityConsumer consumer) {
        consumer.accept(cropIdentifier, imageIdentifier);
    }
}
