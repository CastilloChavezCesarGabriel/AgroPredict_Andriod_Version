package com.agropredict.domain.crop;

import java.util.Objects;
import com.agropredict.domain.identifier.IIdentifierConsumer;
import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.crop.visitor.ICropDescriptionConsumer;
import com.agropredict.domain.crop.visitor.ICropIdentityConsumer;
import com.agropredict.domain.crop.visitor.IFieldConsumer;
import com.agropredict.domain.crop.visitor.IPlantingConsumer;
import com.agropredict.domain.crop.visitor.ISoilConsumer;

public final class Crop {
    private final String identifier;
    private final String type;
    private final CropProfile profile;

    public Crop(String identifier, String type, CropProfile profile) {
        this.identifier = ArgumentPrecondition.validate(identifier, "crop identifier");
        this.type = ArgumentPrecondition.validate(type, "crop type");
        this.profile = Objects.requireNonNull(profile, "crop requires a profile");
    }

    public void describe(ICropIdentityConsumer consumer) {
        consumer.describe(identifier, type);
    }

    public void represent(ICropDescriptionConsumer consumer) {
        profile.track((date, stageIdentifier)
                -> consumer.describe(identifier, type, date));
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