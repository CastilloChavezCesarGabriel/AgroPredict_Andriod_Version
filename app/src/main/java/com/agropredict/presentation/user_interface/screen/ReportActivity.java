package com.agropredict.presentation.user_interface.screen;

import android.os.Bundle;
import android.widget.Button;
import com.agropredict.R;
import com.agropredict.application.factory.ICatalogFactory;
import com.agropredict.application.factory.IReportingFactory;
import com.agropredict.application.factory.IReviewFactory;
import com.agropredict.application.service.IReportServiceCatalog;
import com.agropredict.application.authentication.usecase.CheckSessionUseCase;
import com.agropredict.application.crop_management.usecase.FindCropUseCase;
import com.agropredict.application.crop_management.usecase.ListCropUseCase;
import com.agropredict.application.diagnostic_history.ResolveDiagnosticUseCase;
import com.agropredict.application.report_generation.usecase.StoreReportUseCase;
import com.agropredict.domain.crop.Crop;
import com.agropredict.presentation.user_interface.export.IExportedFile;
import com.agropredict.presentation.user_interface.export.IntentCsvSharer;
import com.agropredict.presentation.user_interface.export.LaunchedPdfOpener;
import com.agropredict.presentation.user_interface.form.ReportForm;
import com.agropredict.presentation.user_interface.selector.DateSelection;
import com.agropredict.presentation.viewmodel.report_generation.ExportPresentation;
import com.agropredict.presentation.viewmodel.report_generation.IReportView;
import com.agropredict.presentation.viewmodel.report_generation.ReportViewModel;
import com.agropredict.presentation.viewmodel.report_generation.ReportingData;
import com.agropredict.presentation.viewmodel.report_generation.ReportingSource;
import java.util.List;

public final class ReportActivity extends BaseActivity implements IReportView {
    private ReportViewModel viewModel;
    private ReportForm reportForm;
    private ListCropUseCase listCrops;
    private IExportedFile generatedArtifact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        bind();
        initialize();
        listen();
    }

    private void bind() {
        reportForm = new ReportForm(this);
    }

    private void initialize() {
        ICatalogFactory catalogFactory = (ICatalogFactory) getApplication();
        IReviewFactory reviewFactory = (IReviewFactory) getApplication();
        IReportingFactory reportingFactory = (IReportingFactory) getApplication();
        IReportServiceCatalog reportCatalog = (IReportServiceCatalog) getApplication();
        listCrops = new ListCropUseCase(catalogFactory.createCropRepository());
        CheckSessionUseCase sessionUseCase = new CheckSessionUseCase(reviewFactory.createSessionRepository());
        ReportingData data = new ReportingData(
                new FindCropUseCase(catalogFactory.createCropRepository()),
                new ResolveDiagnosticUseCase(reviewFactory.createDiagnosticRepository()),
                new StoreReportUseCase(reportingFactory.createReportRepository()));
        ReportingSource source = new ReportingSource(data, reportCatalog);
        ExportPresentation presentation = new ExportPresentation(
                new LaunchedPdfOpener(this),
                new IntentCsvSharer(this));
        viewModel = new ReportViewModel(source, presentation, this);
        sessionUseCase.check((identifier, occupation) -> start(identifier));
    }

    private void listen() {
        reportForm.listen(view -> share());
        Button startDateButton = findViewById(R.id.btnStartDate);
        Button endDateButton = findViewById(R.id.btnEndDate);
        DateSelection startPicker = new DateSelection(startDateButton::setText);
        DateSelection endPicker = new DateSelection(endDateButton::setText);
        startDateButton.setOnClickListener(view -> startPicker.show(this));
        endDateButton.setOnClickListener(view -> endPicker.show(this));
        findViewById(R.id.btnGenerate).setOnClickListener(view -> generate());
    }

    private void start(String userIdentifier) {
        if (userIdentifier == null) return;
        viewModel.load(listCrops, userIdentifier);
    }

    private void generate() {
        reportForm.generate(viewModel);
    }

    private void share() {
        if (generatedArtifact != null) generatedArtifact.present();
    }

    @Override
    public void load() {
        runOnUiThread(() -> reportForm.load());
    }

    @Override
    public void rest() {
        runOnUiThread(() -> reportForm.rest());
    }

    @Override
    public void populate(List<Crop> crops) {
        reportForm.populate(crops);
    }

    @Override
    public void offer(IExportedFile artifact) {
        generatedArtifact = artifact;
        runOnUiThread(() -> {
            reportForm.offer();
            artifact.present();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewModel != null) viewModel.release();
    }
}
