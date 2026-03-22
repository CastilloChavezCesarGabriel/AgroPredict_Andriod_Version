package com.agropredict.presentation.user_interface.component.input;

import android.widget.Spinner;
import com.agropredict.presentation.utilities.SpinnerPopulator;
import java.util.Arrays;

public abstract class SpinnerInput {

    protected String extract(Spinner spinner) {
        return spinner.getSelectedItem().toString();
    }

    protected void fill(Spinner spinner, String... options) {
        SpinnerPopulator.populate(spinner, Arrays.asList(options));
    }
}
