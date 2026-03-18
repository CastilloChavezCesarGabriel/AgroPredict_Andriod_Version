package com.agropredict.presentation.user_interface;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.agropredict.AgroPredictApplication;
import com.agropredict.R;
import com.agropredict.application.usecase.authentication.CheckSessionUseCase;
import com.agropredict.application.usecase.crop.ListCropUseCase;
import com.agropredict.presentation.viewmodel.report.IReportView;
import com.agropredict.presentation.viewmodel.report.ReportViewModel;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ReportActivity extends BaseActivity implements IReportView {

    private ReportViewModel viewModel;
    private ReportViewHolder holder;
    private String generatedFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        compose();
        bind();
    }

    private void compose() {
        AgroPredictApplication application = (AgroPredictApplication) getApplication();
        application.provide(factory -> {
            ListCropUseCase listCrops = new ListCropUseCase(factory.createCropRepository());
            CheckSessionUseCase checkSession = new CheckSessionUseCase(factory.createSessionRepository());
            viewModel = new ReportViewModel(factory, listCrops);
            viewModel.bind(this);
            checkSession.check(this::start);
        });
    }

    private void start(boolean hasSession, String userIdentifier) {
        viewModel.load(userIdentifier);
    }

    private void bind() {
        holder = new ReportViewHolder(this);
        holder.attachShareListener(clickedView -> onShareClicked());
        findViewById(R.id.btnGenerate).setOnClickListener(clickedView -> onGenerateClicked());
    }

    private void onGenerateClicked() {
        if (!holder.hasCropSelected()) {
            notify(getString(R.string.select_crop));
            return;
        }
        viewModel.generate(collect(), this);
    }

    private Map<String, String> collect() {
        Map<String, String> options = new HashMap<>();
        options.put("crop_identifier", holder.selectedCropIdentifier());
        options.put("format", holder.selectedFormat());
        return options;
    }

    private void onShareClicked() {
        if (generatedFilePath == null) return;
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/octet-stream");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(generatedFilePath)));
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_report)));
    }

    @Override
    public void showLoading() {
        holder.showLoading();
    }

    @Override
    public void hideLoading() {
        holder.hideLoading();
    }

    @Override
    public void populateCrops(List<Map<String, String>> crops) {
        holder.populateCrops(crops);
    }

    @Override
    public void showShareOption(String filePath) {
        generatedFilePath = filePath;
        holder.showShareOption(filePath);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewModel != null) viewModel.release();
    }
}
