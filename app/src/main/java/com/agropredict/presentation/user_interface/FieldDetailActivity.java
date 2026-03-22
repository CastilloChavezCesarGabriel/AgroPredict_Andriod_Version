package com.agropredict.presentation.user_interface;

import android.content.Intent;
import android.os.Bundle;
import com.agropredict.AgroPredictApplication;
import com.agropredict.R;
import com.agropredict.application.usecase.diagnostic.FindDiagnosticUseCase;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.presentation.user_interface.component.FieldDetail;
import com.agropredict.presentation.viewmodel.field.FieldDetailViewModel;
import com.agropredict.presentation.viewmodel.field.IFieldDetailView;

public final class FieldDetailActivity extends BaseActivity implements IFieldDetailView {
    private FieldDetailViewModel viewModel;
    private FieldDetail fieldDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_detail);
        findViewById(R.id.btnEditField).setOnClickListener(view -> {
            String cropIdentifier = getIntent().getStringExtra("diagnostic_identifier");
            if (cropIdentifier != null) navigate(cropIdentifier);
        });
        ((AgroPredictApplication) getApplication()).provide(factory -> {
            fieldDetail = new FieldDetail(this, factory.createCropImageRepository());
            FindDiagnosticUseCase useCase = new FindDiagnosticUseCase(factory.createDiagnosticRepository());
            viewModel = new FieldDetailViewModel(useCase, this);
        });
        String identifier = getIntent().getStringExtra("diagnostic_identifier");
        if (identifier != null) viewModel.load(identifier);
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