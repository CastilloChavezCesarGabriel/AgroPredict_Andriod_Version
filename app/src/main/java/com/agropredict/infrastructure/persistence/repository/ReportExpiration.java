package com.agropredict.infrastructure.persistence.repository;

import java.util.Calendar;

public final class ReportExpiration {
    private static final int DURATION_DAYS = 30;

    public long compute() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, DURATION_DAYS);
        return calendar.getTimeInMillis();
    }
}