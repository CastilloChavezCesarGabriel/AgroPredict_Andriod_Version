package com.agropredict.presentation.user_interface.catalog_input;

import android.app.Activity;
import android.widget.Spinner;
import com.agropredict.R;

public final class CatalogInput {
    private final Spinner soilTypeSpinner;
    private final Spinner stageSpinner;

    public CatalogInput(Activity activity) {
        this.soilTypeSpinner = activity.findViewById(R.id.spnSoilType);
        this.stageSpinner = activity.findViewById(R.id.spnStage);
    }

    public void populate(SoilTypeCatalog soilTypeCatalog) {
        soilTypeCatalog.populate(soilTypeSpinner);
    }

    public void populate(StageCatalog stageCatalog) {
        stageCatalog.populate(stageSpinner);
    }

    public String extract() {
        return stageSpinner.getSelectedItem().toString();
    }
}