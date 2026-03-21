package com.agropredict.infrastructure.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public final class CsvReport implements IReportWriter {
    private static final String DELIMITER = ",";
    private final List<String> headers = new ArrayList<>();
    private final List<String> values = new ArrayList<>();

    @Override
    public void write(String label, String value) {
        headers.add(label);
        values.add(escape(value));
    }

    public void export(File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println(String.join(DELIMITER, headers));
            writer.println(String.join(DELIMITER, values));
        }
    }

    private String escape(String value) {
        if (value == null) return "N/A";
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}