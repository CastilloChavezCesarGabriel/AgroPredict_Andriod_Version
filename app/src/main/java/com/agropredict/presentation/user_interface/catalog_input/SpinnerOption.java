package com.agropredict.presentation.user_interface.catalog_input;

import android.widget.Spinner;
import java.util.List;

public abstract class SpinnerOption {
    private final List<String> options;

    protected SpinnerOption(List<String> options) {
        this.options = options;
    }

    public void populate(Spinner spinner) {
        SpinnerPopulator.populate(spinner, options);
    }
}