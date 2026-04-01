package com.agropredict.infrastructure.report_export;

import com.agropredict.application.service.IReportWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public final class CsvReport implements IReportWriter {
    private static final String DELIMITER = ",";
    private final StringBuilder headerLine = new StringBuilder();
    private final StringBuilder valueLine = new StringBuilder();
    private boolean started;

    @Override
    public void write(String label, String value) {
        if (started) {
            headerLine.append(DELIMITER);
            valueLine.append(DELIMITER);
        }
        headerLine.append(label);
        valueLine.append(escape(value));
        started = true;
    }

    public void export(File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println(headerLine);
            writer.println(valueLine);
        }
    }

    private String escape(String value) {
        if (value == null) return "N/A";
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}