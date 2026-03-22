package com.agropredict.presentation.user_interface.input;

import android.app.Activity;
import android.widget.Spinner;
import com.agropredict.R;
import com.agropredict.application.request.data.Irrigation;
import com.agropredict.application.request.data.FarmManagement;
import com.agropredict.application.request.data.PestControl;
import java.util.Arrays;

public final class ManagementInput {
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
        Irrigation irrigation = new Irrigation(selected(irrigationSpinner), selected(fertilizationSpinner));
        PestControl pestControl = new PestControl(selected(sprayingSpinner), selected(weedsSpinner));
        return new FarmManagement(irrigation, pestControl);
    }

    private void populate() {
        fill(irrigationSpinner, "Diario", "Cada 2–3 días", "Semanal", "Muy poco", "Sin riego reciente");
        fill(fertilizationSpinner, "<1 semana", "1–2 semanas", "3 semanas", "No fertilizado", "No sé");
        fill(sprayingSpinner, "Sí (últimos 7 días)", "Sí (últimos 14 días)", "No", "No sé");
        fill(weedsSpinner, "Mucha", "Regular", "Poca", "Ninguna");
    }

    private String selected(Spinner spinner) {
        return spinner.getSelectedItem().toString();
    }

    private void fill(Spinner spinner, String... options) {
        SpinnerPopulator.populate(spinner, Arrays.asList(options));
    }
}
