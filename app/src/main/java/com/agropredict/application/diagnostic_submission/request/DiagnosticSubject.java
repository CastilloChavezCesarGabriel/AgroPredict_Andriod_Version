package com.agropredict.application.diagnostic_submission.request;

import com.agropredict.application.diagnostic_submission.workflow.CropRegistry;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.photograph.Photograph;
import java.util.Objects;

public final class DiagnosticSubject {
    private final Cultivation cultivation;
    private final PhotographInput image;

    public DiagnosticSubject(Cultivation cultivation, PhotographInput image) {
        this.cultivation = Objects.requireNonNull(cultivation, "subject requires a cultivation");
        this.image = Objects.requireNonNull(image, "subject requires a photograph input");
    }

    public Crop produce(String identifier, CropRegistry registry) {
        return cultivation.produce(identifier, registry);
    }

    public Photograph capture(String identifier) {
        return image.produce(identifier);
    }
}