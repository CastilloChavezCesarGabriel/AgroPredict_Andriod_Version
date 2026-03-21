package com.agropredict.presentation.user_interface;
import com.agropredict.presentation.user_interface.holder.HistoryViewHolder;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import com.agropredict.AgroPredictApplication;
import com.agropredict.application.DiagnosticHistoryFacade;
import com.agropredict.R;
import com.agropredict.application.usecase.authentication.CheckSessionUseCase;
import com.agropredict.application.usecase.diagnostic.DeleteDiagnosticUseCase;
import com.agropredict.application.usecase.diagnostic.ListDiagnosticsUseCase;
import com.agropredict.presentation.viewmodel.history.HistoryViewModel;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.presentation.viewmodel.history.IHistoryView;
import java.util.List;

public final class HistoryActivity extends BaseActivity implements IHistoryView {
    private HistoryViewModel viewModel;
    private HistoryViewHolder holder;
    private String userIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        holder = new HistoryViewHolder(this);
        holder.listen((parent, itemView, position, identifier) -> select(position));
        holder.observe((parent, itemView, position, identifier) -> remove(position));
        ((AgroPredictApplication) getApplication()).provide(factory -> {
            ListDiagnosticsUseCase listUseCase = new ListDiagnosticsUseCase(factory.createDiagnosticRepository());
            DeleteDiagnosticUseCase deleteUseCase = new DeleteDiagnosticUseCase(factory.createDiagnosticRepository());
            CheckSessionUseCase sessionUseCase = new CheckSessionUseCase(factory.createSessionRepository());
            DiagnosticHistoryFacade facade = new DiagnosticHistoryFacade(listUseCase, deleteUseCase);
            viewModel = new HistoryViewModel(facade, this);
            sessionUseCase.check(this::start);
        });
    }

    private void start(boolean hasSession, String identifier) {
        this.userIdentifier = identifier;
        viewModel.load(userIdentifier);
    }

    private void select(int position) {
        String identifier = holder.identifierAt(position);
        if (identifier != null) inspect(identifier);
    }

    private boolean remove(int position) {
        String identifier = holder.identifierAt(position);
        if (identifier == null) return false;
        confirm(identifier);
        return true;
    }

    private void confirm(String diagnosticIdentifier) {
        new AlertDialog.Builder(this)
            .setTitle(R.string.delete_diagnostic)
            .setMessage(R.string.delete_confirmation)
            .setPositiveButton(R.string.confirm,
                (dialog, which) -> viewModel.delete(diagnosticIdentifier))
            .setNegativeButton(R.string.cancel, null)
            .show();
    }

    @Override
    public void display(List<Diagnostic> diagnostics) {
        holder.display(diagnostics);
    }

    @Override
    public void inspect(String diagnosticIdentifier) {
        Intent intent = new Intent(this, FieldDetailActivity.class);
        intent.putExtra("diagnostic_identifier", diagnosticIdentifier);
        startActivity(intent);
    }

    @Override
    public void empty() {
        holder.empty();
    }
}
