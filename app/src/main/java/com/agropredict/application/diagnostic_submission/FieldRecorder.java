package com.agropredict.application.diagnostic_submission;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.application.request.diagnostic_submission.SubmissionRequest;

public final class FieldRecorder {
    private final ICropRepository cropRepository;
    private final IPhotographRepository photographRepository;
    private final ICatalogRepository stageCatalog;

    public FieldRecorder(ICropRepository cropRepository, IPhotographRepository photographRepository, ICatalogRepository stageCatalog) {
        this.cropRepository = cropRepository;
        this.photographRepository = photographRepository;
        this.stageCatalog = stageCatalog;
    }

    public void record(SubmissionRequest request) {
        request.store(cropRepository, photographRepository, stageCatalog);
    }
}