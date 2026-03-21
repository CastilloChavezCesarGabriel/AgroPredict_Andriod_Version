package com.agropredict.presentation.user_interface.spinner;

import android.app.Activity;
import android.widget.Spinner;
import com.agropredict.R;
import java.util.List;

public final class CatalogSpinnerGroup {
    private final Spinner soilTypeSpinner;
    private final Spinner stageSpinner;

    public CatalogSpinnerGroup(Activity activity) {
        this.soilTypeSpinner = activity.findViewById(R.id.spnSoilType);
        this.stageSpinner = activity.findViewById(R.id.spnStage);
    }

    public void populateSoilTypes(List<String> soilTypes) {
        SpinnerPopulator.populate(soilTypeSpinner, soilTypes);
    }

    public void populateStages(List<String> stages) {
        SpinnerPopulator.populate(stageSpinner, stages);
    }

    public String stage() {
        return stageSpinner.getSelectedItem().toString();
    }
}
