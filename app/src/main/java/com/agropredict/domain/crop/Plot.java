package com.agropredict.domain.crop;

import com.agropredict.domain.crop.visitor.IFieldConsumer;
import com.agropredict.domain.crop.visitor.ISoilConsumer;
import java.util.Objects;

public final class Plot {
    private final Field field;
    private final Soil soil;

    public Plot(Field field, Soil soil) {
        this.field = Objects.requireNonNull(field, "plot requires a field");
        this.soil = Objects.requireNonNull(soil, "plot requires a soil");
    }

    public void locate(IFieldConsumer consumer) {
        field.locate(consumer);
    }

    public void analyze(ISoilConsumer consumer) {
        soil.analyze(consumer);
    }
}