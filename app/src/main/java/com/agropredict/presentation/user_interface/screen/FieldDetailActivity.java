package com.agropredict.presentation.user_interface.screen;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.agropredict.R;
import com.agropredict.application.factory.IReviewFactory;
import com.agropredict.application.photograph.LoadPhotographUseCase;
import com.agropredict.application.repository.IRecordEraser;
import com.agropredict.application.DeleteUseCase;
import com.agropredict.application.crop_management.usecase.TraceCropHistoryUseCase;
import com.agropredict.application.diagnostic_history.FindDiagnosticUseCase;
import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.domain.photograph.Photograph;
import com.agropredict.domain.history.HistoryRecord;
import com.agropredict.presentation.user_interface.display.FieldDetailDisplay;
import com.agropredict.presentation.user_interface.display.HistoryDialogRenderer;
import com.agropredict.presentation.viewmodel.crop_management.DeleteFieldOutcome;
import com.agropredict.presentation.viewmodel.crop_management.FieldDetailViewModel;
import com.agropredict.presentation.viewmodel.crop_management.FieldInspection;
import com.agropredict.presentation.viewmodel.crop_management.IFieldDetailView;
import java.util.List;

public final class FieldDetailActivity extends BaseActivity implements IFieldDetailView {
    private FieldDetailViewModel viewModel;
    private FieldDetailDisplay fieldDetail;
    private FieldInspection inspection;
    private String diagnosticIdentifier;
    private String cropIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_detail);
        bind();
        initialize();
        listen();
        load();
    }

    private void bind() {
        diagnosticIdentifier = IntentExtra.DIAGNOSTIC_IDENTIFIER.read(getIntent());
        fieldDetail = new FieldDetailDisplay(this);
    }

    private void initialize() {
        IReviewFactory factory = (IReviewFactory) getApplication();
        FindDiagnosticUseCase findUseCase = new FindDiagnosticUseCase(factory.createDiagnosticRepository());
        DeleteUseCase deleteUseCase = new DeleteUseCase((IRecordEraser) factory.createCropRepository());
        TraceCropHistoryUseCase traceUseCase = new TraceCropHistoryUseCase(factory.createCropRepository());
        LoadPhotographUseCase loadPhotograph = new LoadPhotographUseCase(factory.createPhotographRepository());
        inspection = new FieldInspection(deleteUseCase, traceUseCase, loadPhotograph);
        viewModel = new FieldDetailViewModel(findUseCase, this);
    }

    private void listen() {
        findViewById(R.id.btnViewHistory).setOnClickListener(view -> trace());
        findViewById(R.id.btnEditField).setOnClickListener(view -> edit());
        findViewById(R.id.btnDeleteField).setOnClickListener(view -> confirm());
    }

    private void load() {
        if (diagnosticIdentifier == null) return;
        viewModel.load(diagnosticIdentifier);
        Photograph photograph = inspection.find(diagnosticIdentifier);
        if (photograph != null) show(photograph);
    }

    private void trace() {
        List<HistoryRecord> records = inspection.trace(cropIdentifier);
        if (records.isEmpty()) {
            Toast.makeText(this, R.string.no_modifications, Toast.LENGTH_SHORT).show();
            return;
        }
        unfold(records);
    }

    private void edit() {
        if (lacks()) return;
        navigate(cropIdentifier);
    }

    private boolean lacks() {
        if (cropIdentifier != null) return false;
        notify(getString(R.string.error_general));
        return true;
    }

    private void unfold(List<HistoryRecord> records) {
        HistoryDialogRenderer renderer = new HistoryDialogRenderer();
        renderer.render(records);
        new AlertDialog.Builder(this)
                .setTitle(R.string.view_change_history)
                .setMessage(renderer.reveal())
                .setPositiveButton(R.string.confirm, null)
                .show();
    }

    private void confirm() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_field)
                .setMessage(R.string.delete_field_confirmation)
                .setPositiveButton(R.string.confirm, (dialog, which) -> delete())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void delete() {
        if (lacks()) return;
        inspection.delete(cropIdentifier, new DeleteFieldOutcome(this));
    }

    @Override
    public void present(Diagnostic diagnostic) {
        diagnostic.bind((cropId, imageId) -> this.cropIdentifier = cropId);
        fieldDetail.display(diagnostic);
    }

    @Override
    public void show(Photograph photograph) {
        fieldDetail.show(photograph);
    }

    @Override
    public void warn() {
        notify(getString(R.string.severity_high));
    }

    @Override
    public void navigate(String cropIdentifier) {
        Intent intent = new Intent(this, EditFieldActivity.class);
        IntentExtra.CROP_IDENTIFIER.attach(intent, cropIdentifier);
        startActivity(intent);
    }
}