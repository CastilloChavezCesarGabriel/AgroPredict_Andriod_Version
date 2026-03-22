package com.agropredict.presentation.user_interface.component.input;

import android.widget.Spinner;
import com.agropredict.presentation.utilities.SpinnerPopulator;
import java.util.List;

public abstract class Catalog {
    private final List<String> options;

    protected Catalog(List<String> options) {
        this.options = options;
    }

    public void populate(Spinner spinner) {
        SpinnerPopulator.populate(spinner, options);
    }
}