package com.agropredict.presentation.user_interface;

import android.app.DatePickerDialog;
import android.content.Context;
import java.util.Calendar;

public final class DateSelection implements DatePickerDialog.OnDateSetListener {
    private final IDateSelectionListener listener;

    public DateSelection(IDateSelectionListener listener) {
        this.listener = listener;
    }

    public void show(Context context) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(context, this,
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
        String formattedDate = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", day);
        listener.onSelected(formattedDate);
    }
}
