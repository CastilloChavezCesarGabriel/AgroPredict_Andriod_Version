package com.agropredict.presentation.user_interface.input;

import android.app.Activity;
import android.widget.Spinner;
import com.agropredict.R;
import com.agropredict.application.request.data.Soil;
import java.util.Arrays;

public final class SoilInput {
    private final Spinner soilMoistureSpinner;
    private final Spinner soilPhSpinner;

    public SoilInput(Activity activity) {
        this.soilMoistureSpinner = activity.findViewById(R.id.spnSoilMoisture);
        this.soilPhSpinner = activity.findViewById(R.id.spnSoilPH);
        populate();
    }

    public Soil collect() {
        String moisture = selected(soilMoistureSpinner);
        String acidity = selected(soilPhSpinner);
        return new Soil(moisture, acidity);
    }

    private void populate() {
        fill(soilMoistureSpinner, "Muy seco", "Seco", "Moderado", "Húmedo", "Encharcado");
        fill(soilPhSpinner, "<5.5", "5.5–7", "7–8", ">8", "No sé");
    }

    private String selected(Spinner spinner) {
        return spinner.getSelectedItem().toString();
    }

    private void fill(Spinner spinner, String... options) {
        SpinnerPopulator.populate(spinner, Arrays.asList(options));
    }
}