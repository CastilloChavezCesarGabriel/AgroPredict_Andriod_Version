package com.agropredict.presentation.viewmodel.prediction_diagnosis;

import android.app.Activity;
import android.widget.Toast;
import java.util.Objects;

public final class DiagnosePrecheckPresenter implements IPendingDiagnosisOutcome {
    private final Activity activity;

    public DiagnosePrecheckPresenter(Activity activity) {
        this.activity = Objects.requireNonNull(activity, "diagnose precheck presenter requires an activity");
    }

    @Override
    public void warn(int messageResId) {
        Toast.makeText(activity, messageResId, Toast.LENGTH_SHORT).show();
    }
}
