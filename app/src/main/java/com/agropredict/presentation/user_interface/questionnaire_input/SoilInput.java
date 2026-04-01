package com.agropredict.presentation.user_interface.questionnaire_input;

import android.app.Activity;
import android.widget.Spinner;
import com.agropredict.R;
import com.agropredict.application.request.ai_questionnaire.Soil;
import com.agropredict.presentation.user_interface.catalog_input.SpinnerInput;

public final class SoilInput extends SpinnerInput {
    private final Spinner soilMoistureSpinner;
    private final Spinner soilPhSpinner;

    public SoilInput(Activity activity) {
        this.soilMoistureSpinner = activity.findViewById(R.id.spnSoilMoisture);
        this.soilPhSpinner = activity.findViewById(R.id.spnSoilPH);
        populate();
    }

    public Soil collect() {
        String moisture = extract(soilMoistureSpinner);
        String acidity = extract(soilPhSpinner);
        return new Soil(moisture, acidity);
    }

    private void populate() {
        fill(soilMoistureSpinner, "Very dry", "Dry", "Moderate", "Moist", "Waterlogged");
        fill(soilPhSpinner, "<5.5", "5.5–7", "7–8", ">8", "Don't know");
    }

}