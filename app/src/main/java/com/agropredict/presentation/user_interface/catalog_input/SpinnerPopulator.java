package com.agropredict.presentation.user_interface.catalog_input;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.List;

public final class SpinnerPopulator {
    public static void populate(Spinner spinner, List<String> items) {
        Context context = spinner.getContext();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}