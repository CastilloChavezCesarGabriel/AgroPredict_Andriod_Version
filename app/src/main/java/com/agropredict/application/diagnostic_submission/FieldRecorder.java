package com.agropredict.application.diagnostic_submission;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;

public final class FieldRecorder {
    private final ICropRepository cropRepository;
    private final IPhotographRepository photographRepository;

    public FieldRecorder(ICropRepository cropRepository, IPhotographRepository photographRepository) {
        this.cropRepository = cropRepository;
        this.photographRepository = photographRepository;
    }

    public void record(SubmissionRequest request) {
        request.store(cropRepository, photographRepository);
    }
}