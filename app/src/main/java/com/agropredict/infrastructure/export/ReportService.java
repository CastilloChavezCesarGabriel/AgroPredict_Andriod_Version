package com.agropredict.infrastructure.export;

import com.agropredict.application.service.IReportService;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class ReportService implements IReportService {
    private static final String FILE_FORMAT = "yyyy-MM-dd_HH-mm-ss";
    protected final File outputDirectory;

    protected ReportService(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    protected String stamp() {
        return new SimpleDateFormat(FILE_FORMAT, Locale.getDefault()).format(new Date());
    }
}
