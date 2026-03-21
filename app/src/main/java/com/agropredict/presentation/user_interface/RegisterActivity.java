package com.agropredict.presentation.user_interface;
import com.agropredict.presentation.user_interface.holder.RegisterViewHolder;

import android.os.Bundle;
import com.agropredict.AgroPredictApplication;
import com.agropredict.R;
import com.agropredict.application.usecase.authentication.RegisterUseCase;
import com.agropredict.application.usecase.catalog.ListCatalogUseCase;
import com.agropredict.presentation.viewmodel.authentication.IRegisterView;
import com.agropredict.presentation.viewmodel.authentication.RegisterViewModel;
import java.util.List;

public final class RegisterActivity extends BaseActivity implements IRegisterView {
    private RegisterViewModel viewModel;
    private RegisterViewHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        holder = new RegisterViewHolder(this);
        ((AgroPredictApplication) getApplication()).provide(factory -> {
            RegisterUseCase useCase = new RegisterUseCase(
                    factory.createUserRepository(), factory.createOccupationCatalog());
            ListCatalogUseCase occupations = new ListCatalogUseCase(factory.createOccupationCatalog());
            viewModel = new RegisterViewModel(useCase, this);
            viewModel.populate(occupations);
        });
        findViewById(R.id.btnRegister).setOnClickListener(view -> register());
        findViewById(R.id.tvGoToLogin).setOnClickListener(view -> dismiss());
    }

    private void register() {
        if (!holder.match()) {
            notify(getString(R.string.passwords_mismatch));
            return;
        }
        viewModel.register(holder.collect());
    }

    @Override
    public void dismiss() {
        navigate(LoginActivity.class);
        finish();
    }

    @Override
    public void populate(List<String> occupations) {
        holder.populate(occupations);
    }
}
