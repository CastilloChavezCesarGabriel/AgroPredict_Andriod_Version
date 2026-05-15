package com.agropredict.infrastructure.report_export;

import android.content.Context;
import com.agropredict.application.service.IReportService;
import com.agropredict.application.service.IReportServiceBuilder;
import com.agropredict.application.service.IReportServiceCatalog;
import com.agropredict.application.service.ReportFormat;
import java.io.File;
import java.util.List;
import java.util.Objects;

public final class AndroidReportServiceCatalog implements IReportServiceCatalog {
    private static final String REPORTS_SUBDIRECTORY = "reports";
    private final File reportsDirectory;
    private final List<IReportServiceBuilder> builders;

    public AndroidReportServiceCatalog(Context context, List<IReportServiceBuilder> builders) {
        Objects.requireNonNull(context, "android report service catalog requires a context");
        this.builders = List.copyOf(Objects.requireNonNull(builders,
                "android report service catalog requires builders"));
        this.reportsDirectory = new File(context.getExternalFilesDir(null), REPORTS_SUBDIRECTORY);
        if (!reportsDirectory.exists() && !reportsDirectory.mkdirs()) {
            throw new IllegalStateException("cannot create reports directory: " + reportsDirectory.getAbsolutePath());
        }
    }

    @Override
    public IReportService select(ReportFormat format) {
        String directoryPath = reportsDirectory.getAbsolutePath();
        for (IReportServiceBuilder builder : builders) {
            IReportService service = builder.build(format, directoryPath);
            if (service != null) return service;
        }
        throw new IllegalStateException("no report service builder registered for format " + format);
    }
}
