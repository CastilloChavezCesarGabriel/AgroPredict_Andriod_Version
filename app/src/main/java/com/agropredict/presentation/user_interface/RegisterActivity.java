package com.agropredict.presentation.user_interface;

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
        compose();
        bind();
    }

    private void compose() {
        AgroPredictApplication application = (AgroPredictApplication) getApplication();
        application.provide(factory -> {
            RegisterUseCase registerUseCase = new RegisterUseCase(factory.createUserRepository());
            ListCatalogUseCase listOccupations = new ListCatalogUseCase(factory.createOccupationCatalog());
            viewModel = new RegisterViewModel(registerUseCase, listOccupations);
            viewModel.bind(this);
        });
    }

    private void bind() {
        holder = new RegisterViewHolder(this);
        findViewById(R.id.btnRegister).setOnClickListener(clickedView -> onRegisterClicked());
        findViewById(R.id.tvGoToLogin).setOnClickListener(clickedView -> navigateToLogin());
        viewModel.populate();
    }

    private void onRegisterClicked() {
        if (!holder.matchPasswords()) {
            notify(getString(R.string.passwords_mismatch));
            return;
        }
        viewModel.register(holder.collect());
    }

    @Override
    public void navigateToLogin() {
        navigate(LoginActivity.class);
        finish();
    }

    @Override
    public void populateOccupations(List<String> occupations) {
        holder.populateOccupations(occupations);
    }
}
