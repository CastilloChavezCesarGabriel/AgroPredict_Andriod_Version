package com.agropredict.domain.crop;

import com.agropredict.domain.crop.visitor.IFieldConsumer;
import com.agropredict.domain.crop.visitor.ISoilConsumer;

public final class Plot {
    private final Field field;
    private final Soil soil;

    public Plot(Field field, Soil soil) {
        this.field = field;
        this.soil = soil;
    }

    public void locate(IFieldConsumer consumer) {
        if (field != null) {
            field.locate(consumer);
        }
    }

    public void analyze(ISoilConsumer consumer) {
        if (soil != null) {
            soil.analyze(consumer);
        }
    }
}