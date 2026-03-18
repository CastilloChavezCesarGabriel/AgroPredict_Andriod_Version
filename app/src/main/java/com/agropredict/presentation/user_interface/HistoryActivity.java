package com.agropredict.presentation.user_interface;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import com.agropredict.AgroPredictApplication;
import com.agropredict.R;
import com.agropredict.application.usecase.authentication.CheckSessionUseCase;
import com.agropredict.application.usecase.diagnostic.DeleteDiagnosticUseCase;
import com.agropredict.application.usecase.diagnostic.ListDiagnosticsUseCase;
import com.agropredict.presentation.viewmodel.history.HistoryViewModel;
import com.agropredict.presentation.viewmodel.history.IHistoryView;
import java.util.List;
import java.util.Map;

public final class HistoryActivity extends BaseActivity implements IHistoryView {

    private HistoryViewModel viewModel;
    private HistoryViewHolder holder;
    private String userIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        compose();
        bind();
    }

    private void compose() {
        AgroPredictApplication application = (AgroPredictApplication) getApplication();
        application.provide(factory -> {
            ListDiagnosticsUseCase listDiagnostics = new ListDiagnosticsUseCase(factory.createDiagnosticRepository());
            DeleteDiagnosticUseCase deleteDiagnostic = new DeleteDiagnosticUseCase(factory.createDiagnosticRepository());
            CheckSessionUseCase checkSession = new CheckSessionUseCase(factory.createSessionRepository());
            viewModel = new HistoryViewModel(listDiagnostics, deleteDiagnostic);
            viewModel.bind(this);
            checkSession.check(this::start);
        });
    }

    private void start(boolean hasSession, String identifier) {
        this.userIdentifier = identifier;
        viewModel.load(userIdentifier);
    }

    private void bind() {
        holder = new HistoryViewHolder(this);
        holder.attachClickListener((parent, itemView, position, identifier) -> onItemClicked(position));
        holder.attachLongClickListener((parent, itemView, position, identifier) -> onItemLongClicked(position));
    }

    private void onItemClicked(int position) {
        String identifier = holder.identifierAt(position);
        if (identifier != null) navigateToDetail(identifier);
    }

    private boolean onItemLongClicked(int position) {
        String identifier = holder.identifierAt(position);
        if (identifier == null) return false;
        confirm(identifier);
        return true;
    }

    private void confirm(String diagnosticIdentifier) {
        new AlertDialog.Builder(this)
            .setTitle(R.string.delete_diagnostic)
            .setMessage(R.string.delete_confirmation)
            .setPositiveButton(R.string.confirm, (dialog, which) -> viewModel.delete(diagnosticIdentifier, userIdentifier))
            .setNegativeButton(R.string.cancel, null)
            .show();
    }

    @Override
    public void display(List<Map<String, Object>> diagnostics) {
        holder.display(diagnostics);
    }

    @Override
    public void navigateToDetail(String diagnosticIdentifier) {
        Intent intent = new Intent(this, FieldDetailActivity.class);
        intent.putExtra("diagnostic_identifier", diagnosticIdentifier);
        startActivity(intent);
    }

    @Override
    public void showEmpty() {
        holder.showEmpty();
    }
}
