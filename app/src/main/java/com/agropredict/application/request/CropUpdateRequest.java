package com.agropredict.application.request;

import com.agropredict.domain.identifier.IIdentifierConsumer;
import com.agropredict.domain.crop.CropProfile;
import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.crop.visitor.IFieldConsumer;
import com.agropredict.domain.crop.visitor.IPlantingConsumer;
import com.agropredict.domain.crop.visitor.ISoilConsumer;
import java.util.Objects;

public final class CropUpdateRequest {
    private final String identifier;
    private final CropProfile profile;

    public CropUpdateRequest(String identifier, CropProfile profile) {
        this.identifier = ArgumentPrecondition.validate(identifier, "crop update identifier");
        this.profile = Objects.requireNonNull(profile, "crop update requires a profile");
    }

    public void locate(IFieldConsumer consumer) {
        profile.locate(consumer);
    }

    public void analyze(ISoilConsumer consumer) {
        profile.analyze(consumer);
    }

    public void track(IPlantingConsumer consumer) {
        profile.track(consumer);
    }

    public void identify(IIdentifierConsumer consumer) {
        consumer.identify(identifier);
    }
}
