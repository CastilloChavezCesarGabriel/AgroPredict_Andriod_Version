package com.agropredict.domain.crop;

import com.agropredict.domain.crop.visitor.IFieldConsumer;
import com.agropredict.domain.crop.visitor.IPlantingConsumer;
import com.agropredict.domain.crop.visitor.ISoilConsumer;
import java.util.Objects;

public final class CropProfile {
    private final Plot plot;
    private final GrowthCycle growthCycle;

    public CropProfile(Plot plot, GrowthCycle growthCycle) {
        this.plot = Objects.requireNonNull(plot, "crop profile requires a plot");
        this.growthCycle = Objects.requireNonNull(growthCycle, "crop profile requires a growth cycle");
    }

    public void locate(IFieldConsumer consumer) {
        plot.locate(consumer);
    }

    public void analyze(ISoilConsumer consumer) {
        plot.analyze(consumer);
    }

    public void track(IPlantingConsumer consumer) {
        growthCycle.track(consumer);
    }
}