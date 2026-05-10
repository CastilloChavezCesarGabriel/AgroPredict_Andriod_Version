package com.agropredict.domain.crop;

import com.agropredict.domain.crop.visitor.IFieldConsumer;
import com.agropredict.domain.crop.visitor.IPlantingConsumer;
import com.agropredict.domain.crop.visitor.ISoilConsumer;

public final class CropProfile {
    private final Plot plot;
    private final GrowthCycle growthCycle;

    public CropProfile(Plot plot, GrowthCycle growthCycle) {
        this.plot = plot;
        this.growthCycle = growthCycle;
    }

    public static CropProfile compose(String stageIdentifier) {
        return new CropProfile(new Plot(null, null), new GrowthCycle(null, stageIdentifier));
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