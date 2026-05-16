package com.agropredict.presentation.user_interface.catalog_input;

import android.widget.Spinner;

import java.util.Arrays;

public abstract class SpinnerInput {
    protected String extract(Spinner spinner) {
        return spinner.getSelectedItem().toString();
    }

    protected void fill(Spinner spinner, String... options) {
        new SpinnerPopulator().populate(spinner, Arrays.asList(options));
    }
}