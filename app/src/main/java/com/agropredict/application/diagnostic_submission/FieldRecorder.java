package com.agropredict.application.diagnostic_submission;

import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;

public final class FieldRecorder {
    private final Cropland cropland;

    public FieldRecorder(Cropland cropland) {
        this.cropland = cropland;
    }

    public void record(SubmissionRequest request, Allocation allocation) {
        request.store(cropland, allocation);
    }
}
