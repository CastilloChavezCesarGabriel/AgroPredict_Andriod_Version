package com.agropredict.presentation.user_interface.questionnaire_input;

import android.app.Activity;
import android.widget.Spinner;
import com.agropredict.R;
import com.agropredict.application.request.ai_questionnaire.Irrigation;
import com.agropredict.application.request.ai_questionnaire.FarmManagement;
import com.agropredict.application.request.ai_questionnaire.PestControl;
import com.agropredict.presentation.user_interface.catalog_input.SpinnerInput;

public final class ManagementInput extends SpinnerInput {
    private final Spinner irrigationSpinner;
    private final Spinner fertilizationSpinner;
    private final Spinner sprayingSpinner;
    private final Spinner weedsSpinner;

    public ManagementInput(Activity activity) {
        this.irrigationSpinner = activity.findViewById(R.id.spnIrrigation);
        this.fertilizationSpinner = activity.findViewById(R.id.spnFertilization);
        this.sprayingSpinner = activity.findViewById(R.id.spnSpraying);
        this.weedsSpinner = activity.findViewById(R.id.spnWeeds);
        populate();
    }

    public FarmManagement collect() {
        Irrigation irrigation = new Irrigation(extract(irrigationSpinner), extract(fertilizationSpinner));
        PestControl pestControl = new PestControl(extract(sprayingSpinner), extract(weedsSpinner));
        return new FarmManagement(irrigation, pestControl);
    }

    private void populate() {
        fill(irrigationSpinner, "Daily", "Every 2–3 days", "Weekly", "Very little", "No recent irrigation");
        fill(fertilizationSpinner, "<1 week", "1–2 weeks", "3 weeks", "Not fertilized", "Don't know");
        fill(sprayingSpinner, "Yes (last 7 days)", "Yes (last 14 days)", "No", "Don't know");
        fill(weedsSpinner, "Heavy", "Moderate", "Light", "None");
    }

}