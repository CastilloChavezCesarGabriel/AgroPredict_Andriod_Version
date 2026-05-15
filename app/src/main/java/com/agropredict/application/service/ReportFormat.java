package com.agropredict.application.service;

import java.util.Locale;

public enum ReportFormat {
    CSV, PDF;

    public String serialize() {
        return name().toLowerCase(Locale.ROOT);
    }
}
