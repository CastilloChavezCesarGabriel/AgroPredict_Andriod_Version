package com.agropredict.presentation.user_interface;
import com.agropredict.presentation.user_interface.holder.ReportViewHolder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.content.FileProvider;
import com.agropredict.AgroPredictApplication;
import com.agropredict.R;
import com.agropredict.application.usecase.authentication.CheckSessionUseCase;
import com.agropredict.application.usecase.crop.ListCropUseCase;
import com.agropredict.domain.entity.Crop;
import com.agropredict.presentation.viewmodel.report.IReportView;
import com.agropredict.presentation.viewmodel.report.ReportViewModel;
import java.io.File;
import java.util.List;

public final class ReportActivity extends BaseActivity implements IReportView {
    private ReportViewModel viewModel;
    private ReportViewHolder holder;
    private ListCropUseCase listCrops;
    private String generatedFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        holder = new ReportViewHolder(this);
        holder.listen(view -> share());
        ((AgroPredictApplication) getApplication()).provide(factory -> {
            listCrops = new ListCropUseCase(factory.createCropRepository());
            CheckSessionUseCase sessionUseCase = new CheckSessionUseCase(factory.createSessionRepository());
            viewModel = new ReportViewModel(factory, this);
            sessionUseCase.check(this::start);
        });
        findViewById(R.id.btnGenerate).setOnClickListener(view -> generate());
    }

    private void start(boolean hasSession, String userIdentifier) {
        viewModel.load(listCrops, userIdentifier);
    }

    private void generate() {
        holder.generate(viewModel);
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
        runOnUiThread(() -> holder.load());
    }

    @Override
    public void idle() {
        runOnUiThread(() -> holder.idle());
    }

    @Override
    public void populate(List<Crop> crops) {
        holder.populate(crops);
    }

    @Override
    public void offer(String filePath) {
        generatedFilePath = filePath;
        runOnUiThread(() -> {
            holder.offer();
            if (filePath.endsWith(".pdf")) PdfLauncher.open(this, filePath);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewModel != null) viewModel.release();
    }
}
