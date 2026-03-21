package com.agropredict.infrastructure.persistence;

import com.agropredict.application.repository.ICropImageRepository;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.request.SubmissionRequest;

public final class SubmissionRecorder {
    private final ICropRepository cropRepository;
    private final ICropImageRepository cropImageRepository;

    public SubmissionRecorder(ICropRepository cropRepository, ICropImageRepository cropImageRepository) {
        this.cropRepository = cropRepository;
        this.cropImageRepository = cropImageRepository;
    }

    public void record(SubmissionRequest request) {
        cropRepository.store(request.cultivate());
        cropImageRepository.store(request.capture());
    }
}
