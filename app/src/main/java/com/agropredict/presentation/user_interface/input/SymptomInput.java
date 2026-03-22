package com.agropredict.presentation.user_interface.input;

import android.app.Activity;
import android.widget.Spinner;
import com.agropredict.R;
import com.agropredict.application.request.data.Observation;
import com.agropredict.application.request.data.Pest;
import com.agropredict.application.request.data.Symptom;
import java.util.Arrays;

public final class SymptomInput {
    private final Spinner symptomTypeSpinner;
    private final Spinner severitySpinner;
    private final Spinner insectsSpinner;
    private final Spinner animalsSpinner;

    public SymptomInput(Activity activity) {
        this.symptomTypeSpinner = activity.findViewById(R.id.spnSymptomType);
        this.severitySpinner = activity.findViewById(R.id.spnSeverity);
        this.insectsSpinner = activity.findViewById(R.id.spnInsects);
        this.animalsSpinner = activity.findViewById(R.id.spnAnimals);
        populate();
    }

    public Observation collect() {
        Symptom symptom = new Symptom(selected(symptomTypeSpinner), selected(severitySpinner));
        Pest pest = new Pest(selected(insectsSpinner), selected(animalsSpinner));
        return new Observation(symptom, pest);
    }

    private void populate() {
        fill(symptomTypeSpinner,
                "Hojas amarillas", "Mancha marrón",
                "Puntas secas", "Tallos débiles",
                "Hongos visibles", "Presencia de insectos",
                "Se ve normal");
        fill(severitySpinner, "Leve", "Moderada", "Fuerte", "Muy fuerte");
        fill(insectsSpinner,
                "Mosquita blanca", "Pulgón", "Minador",
                "Trips", "Oruga", "Ninguno", "No sé");
        fill(animalsSpinner, "Roedores", "Aves", "Animales grandes", "Ninguno", "No sé");
    }

    private String selected(Spinner spinner) {
        return spinner.getSelectedItem().toString();
    }

    private void fill(Spinner spinner, String... options) {
        SpinnerPopulator.populate(spinner, Arrays.asList(options));
    }
}
