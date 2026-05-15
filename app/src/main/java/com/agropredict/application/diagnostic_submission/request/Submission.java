package com.agropredict.application.diagnostic_submission.request;

import com.agropredict.application.diagnostic_submission.workflow.SubmissionIdentity;
import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.domain.diagnostic.visitor.IPredictionConsumer;
import com.agropredict.domain.diagnostic.Diagnostic;
import java.util.Objects;

public final class Submission {
    private final ImagePrediction prediction;
    private final PhotographInput image;

    public Submission(ImagePrediction prediction, PhotographInput image) {
        this.prediction = Objects.requireNonNull(prediction, "submission requires a classification");
        this.image = Objects.requireNonNull(image, "submission requires a photograph input");
    }

    public Diagnostic diagnose(String identifier) {
        return prediction.diagnose(identifier);
    }

    public void archive(IPhotographRepository repository, SubmissionIdentity identity) {
        identity.enroll(image, repository);
    }

    public void dispatch(IPredictionConsumer consumer) {
        prediction.accept(consumer);
    }
}
