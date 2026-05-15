package com.agropredict.presentation.user_interface.screen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import androidx.core.content.FileProvider;
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
import com.agropredict.presentation.user_interface.form.ReportForm;
import com.agropredict.presentation.user_interface.navigation.PdfLauncher;
import com.agropredict.presentation.user_interface.selector.DateSelection;
import com.agropredict.presentation.viewmodel.report_generation.IReportView;
import com.agropredict.presentation.viewmodel.report_generation.ReportViewModel;
import com.agropredict.presentation.viewmodel.report_generation.ReportingData;
import java.io.File;
import java.util.List;

public final class ReportActivity extends BaseActivity implements IReportView {
    private ReportViewModel viewModel;
    private ReportForm reportForm;
    private ListCropUseCase listCrops;
    private String generatedFilePath;

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
        viewModel = new ReportViewModel(data, reportCatalog, this);
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
        if (generatedFilePath == null) return;
        Uri uri = FileProvider.getUriForFile(this,
                getPackageName() + ".fileprovider", new File(generatedFilePath));
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/octet-stream");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, getString(R.string.share_report)));
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
    public void offer(String filePath) {
        generatedFilePath = filePath;
        runOnUiThread(() -> present(filePath));
    }

    private void present(String filePath) {
        reportForm.offer();
        if (filePath.endsWith(".pdf")) PdfLauncher.open(this, filePath);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewModel != null) viewModel.release();
    }
}