package com.agropredict.presentation.user_interface.screens;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.agropredict.AgroPredictApplication;
import com.agropredict.R;
import com.agropredict.application.operation_result.HistoryRecord;
import com.agropredict.application.usecase.DeleteUseCase;
import com.agropredict.application.usecase.crop.TraceCropHistoryUseCase;
import com.agropredict.application.usecase.diagnostic.FindDiagnosticUseCase;
import com.agropredict.application.visitor.IOperationResultVisitor;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.presentation.user_interface.component.FieldDetail;
import com.agropredict.presentation.viewmodel.field.FieldDetailViewModel;
import com.agropredict.presentation.viewmodel.field.IFieldDetailView;
import java.util.List;

public final class FieldDetailActivity extends BaseActivity implements IFieldDetailView, IOperationResultVisitor {
    private FieldDetailViewModel viewModel;
    private FieldDetail fieldDetail;
    private DeleteUseCase deleteUseCase;
    private TraceCropHistoryUseCase traceUseCase;
    private String cropIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_detail);
        cropIdentifier = getIntent().getStringExtra("diagnostic_identifier");
        ((AgroPredictApplication) getApplication()).provide(factory -> {
            fieldDetail = new FieldDetail(this);
            FindDiagnosticUseCase findUseCase = new FindDiagnosticUseCase(factory.createDiagnosticRepository());
            deleteUseCase = new DeleteUseCase(factory.createCropRepository());
            traceUseCase = new TraceCropHistoryUseCase(factory.createCropRepository());
            viewModel = new FieldDetailViewModel(findUseCase, this);
        });
        findViewById(R.id.btnViewHistory).setOnClickListener(view -> trace());
        findViewById(R.id.btnEditField).setOnClickListener(view -> navigate(cropIdentifier));
        findViewById(R.id.btnDeleteField).setOnClickListener(view -> confirm());
        if (cropIdentifier != null) viewModel.load(cropIdentifier);
    }

    private void trace() {
        List<HistoryRecord> records = traceUseCase.trace(cropIdentifier);
        if (records.isEmpty()) {
            Toast.makeText(this, R.string.no_modifications, Toast.LENGTH_SHORT).show();
            return;
        }

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
        deleteUseCase.delete(cropIdentifier).accept(this);
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
    public void warn() {
        notify(getString(R.string.severity_high));
    }

    @Override
    public void navigate(String cropIdentifier) {
        Intent intent = new Intent(this, EditFieldActivity.class);
        intent.putExtra("crop_identifier", cropIdentifier);
        startActivity(intent);
    }
}