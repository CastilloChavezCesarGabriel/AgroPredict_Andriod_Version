package com.agropredict.infrastructure.export;

import com.agropredict.application.result.OperationResult;
import com.agropredict.application.service.IReportGeneratorService;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public final class CsvReportGeneratorService implements IReportGeneratorService {
    private static final String DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss";
    private static final String DELIMITER = ",";
    private final File outputDirectory;

    public CsvReportGeneratorService(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public OperationResult generate(Map<String, Object> reportData) {
        try {
            String filePath = compose(reportData);
            return OperationResult.succeed(filePath);
        } catch (IOException exception) {
            return OperationResult.fail();
        }
    }

    private String compose(Map<String, Object> reportData) throws IOException {
        File reportFile = createReportFile();
        try (PrintWriter writer = new PrintWriter(new FileWriter(reportFile))) {
            writeHeaders(writer);
            writeDataRow(writer, reportData);
        }
        return reportFile.getAbsolutePath();
    }

    private File createReportFile() {
        String timestamp = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(new Date());
        String fileName = "reporte_" + timestamp + ".csv";
        return new File(outputDirectory, fileName);
    }

    private void writeHeaders(PrintWriter writer) {
        writer.println(joinColumns(
                "cultivo_nombre",
                "cultivo_detectado",
                "confianza",
                "reporte_resumido",
                "texto_largo",
                "fecha_generacion"
        ));
    }

    private void writeDataRow(PrintWriter writer, Map<String, Object> reportData) {
        String generationDate = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(new Date());
        writer.println(joinColumns(
                sanitize(reportData, "cultivo_nombre"),
                sanitize(reportData, "cultivo_detectado"),
                sanitize(reportData, "confianza"),
                sanitize(reportData, "reporte_resumido"),
                sanitize(reportData, "texto_largo"),
                generationDate
        ));
    }

    private String sanitize(Map<String, Object> data, String key) {
        Object value = data.get(key);
        if (value == null) return "N/A";
        String text = value.toString().replace("\"", "\"\"");
        return "\"" + text + "\"";
    }

    private String joinColumns(String... columns) {
        return String.join(DELIMITER, columns);
    }
}