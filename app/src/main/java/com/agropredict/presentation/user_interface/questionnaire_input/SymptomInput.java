package com.agropredict.presentation.user_interface.questionnaire_input;

import android.app.Activity;
import android.widget.Spinner;
import com.agropredict.R;
import com.agropredict.application.diagnostic_submission.ai_questionnaire.Observation;
import com.agropredict.application.diagnostic_submission.ai_questionnaire.Pest;
import com.agropredict.application.diagnostic_submission.ai_questionnaire.Symptom;
import com.agropredict.presentation.user_interface.catalog_input.SpinnerInput;

public final class SymptomInput extends SpinnerInput {
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
        Symptom symptom = new Symptom(extract(symptomTypeSpinner), extract(severitySpinner));
        Pest pest = new Pest(extract(insectsSpinner), extract(animalsSpinner));
        return new Observation(symptom, pest);
    }

    private void populate() {
        fill(symptomTypeSpinner,
                "Yellow leaves", "Brown spots",
                "Dry tips", "Weak stems",
                "Visible fungi", "Insect presence",
                "Looks normal");
        fill(severitySpinner, "Mild", "Moderate", "Severe", "Very severe");
        fill(insectsSpinner,
                "Whitefly", "Aphid", "Leafminer",
                "Thrips", "Caterpillar", "None", "Don't know");
        fill(animalsSpinner, "Rodents", "Birds", "Large animals", "None", "Don't know");
    }

}