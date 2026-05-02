package com.agropredict.presentation.user_interface.screen;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.agropredict.R;
import com.agropredict.application.factory.IReviewFactory;
import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.application.usecase.crop.RemoveCropUseCase;
import com.agropredict.application.usecase.crop.TraceCropHistoryUseCase;
import com.agropredict.application.usecase.diagnostic.FindDiagnosticUseCase;
import com.agropredict.application.visitor.IOperationResultVisitor;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.domain.entity.Photograph;
import com.agropredict.domain.history.HistoryRecord;
import com.agropredict.presentation.user_interface.display.FieldDetailDisplay;
import com.agropredict.presentation.viewmodel.crop_management.FieldDetailViewModel;
import com.agropredict.presentation.viewmodel.crop_management.IFieldDetailView;
import java.util.List;

public final class FieldDetailActivity extends BaseActivity implements IFieldDetailView, IOperationResultVisitor {
    private FieldDetailViewModel viewModel;
    private FieldDetailDisplay fieldDetail;
    private RemoveCropUseCase removeUseCase;
    private TraceCropHistoryUseCase traceUseCase;
    private IPhotographRepository photographRepository;
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
        cropIdentifier = IntentExtra.DIAGNOSTIC_IDENTIFIER.read(getIntent());
        fieldDetail = new FieldDetailDisplay(this);
    }

    private void initialize() {
        IReviewFactory factory = (IReviewFactory) getApplication();
        FindDiagnosticUseCase findUseCase = new FindDiagnosticUseCase(factory.createDiagnosticRepository());
        removeUseCase = new RemoveCropUseCase(factory.createCropRepository(), factory.createCropCleanup());
        traceUseCase = new TraceCropHistoryUseCase(factory.createCropRepository());
        photographRepository = factory.createPhotographRepository();
        viewModel = new FieldDetailViewModel(findUseCase, this);
    }

    private void listen() {
        findViewById(R.id.btnViewHistory).setOnClickListener(view -> trace());
        findViewById(R.id.btnEditField).setOnClickListener(view -> navigate(cropIdentifier));
        findViewById(R.id.btnDeleteField).setOnClickListener(view -> confirm());
    }

    private void load() {
        if (cropIdentifier == null) return;
        viewModel.load(cropIdentifier);
        Photograph photograph = photographRepository.find(cropIdentifier);
        if (photograph != null) display(photograph);
    }

    private void trace() {
        List<HistoryRecord> records = traceUseCase.trace(cropIdentifier);
        if (records.isEmpty()) {
            Toast.makeText(this, R.string.no_modifications, Toast.LENGTH_SHORT).show();
            return;
        }
        present(records);
    }

    private void present(List<HistoryRecord> records) {
        StringBuilder builder = new StringBuilder();
        for (HistoryRecord record : records) {
            record.summarize(builder);
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.view_change_history)
                .setMessage(builder.toString().trim())
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
        removeUseCase.remove(cropIdentifier).accept(this);
    }

    @Override
    public void visit(boolean completed, String resultIdentifier) {
        if (completed) {
            Toast.makeText(this, R.string.field_deleted, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, R.string.error_general, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void display(Diagnostic diagnostic) {
        fieldDetail.display(diagnostic);
    }

    @Override
    public void display(Photograph photograph) {
        fieldDetail.display(photograph);
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