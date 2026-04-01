package com.agropredict.infrastructure.persistence.diagnostic_submission;

import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;

public final class Submission {
    private final ICropRepository cropRepository;
    private final IPhotographRepository photographRepository;

    public Submission(ICropRepository cropRepository, IPhotographRepository photographRepository) {
        this.cropRepository = cropRepository;
        this.photographRepository = photographRepository;
    }

    public void record(SubmissionRequest request) {
        request.store(cropRepository, photographRepository);
    }
}
