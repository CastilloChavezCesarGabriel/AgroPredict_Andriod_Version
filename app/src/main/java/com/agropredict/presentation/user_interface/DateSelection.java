package com.agropredict.presentation.user_interface;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import java.util.Calendar;
import java.util.Locale;

public final class DateSelection implements OnDateSetListener {
    private final IDateSelectionListener listener;

    public DateSelection(IDateSelectionListener listener) {
        this.listener = listener;
    }

    public void show(Context context) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(context, this,
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
        String formattedDate = String.format(Locale.US, "%04d-%02d-%02d", year,
                month + 1, day);
        listener.onSelected(formattedDate);
    }
}