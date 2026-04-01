package com.agropredict.presentation.user_interface.screen;

import android.os.Bundle;
import com.agropredict.AgroPredictApplication;
import com.agropredict.R;
import com.agropredict.application.usecase.authentication.RegisterUseCase;
import com.agropredict.application.usecase.catalog.ListCatalogUseCase;
import com.agropredict.presentation.viewmodel.authentication.IRegisterView;
import com.agropredict.presentation.viewmodel.authentication.RegisterViewModel;
import com.agropredict.presentation.user_interface.form.RegistrationForm;
import java.util.List;

public final class RegisterActivity extends BaseActivity implements IRegisterView {
    private RegisterViewModel viewModel;
    private RegistrationForm registrationForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registrationForm = new RegistrationForm(this);
        ((AgroPredictApplication) getApplication()).provide(factory -> {
            RegisterUseCase useCase = new RegisterUseCase(
                    factory.createUserRepository(), factory.createPasswordHasher());
            ListCatalogUseCase occupations = new ListCatalogUseCase(factory.createOccupationCatalog());
            viewModel = new RegisterViewModel(useCase, this);
            viewModel.populate(occupations);
        });
        findViewById(R.id.btnRegister).setOnClickListener(view -> register());
        findViewById(R.id.tvGoToLogin).setOnClickListener(view -> dismiss());
    }

    private void register() {
        if (!registrationForm.isMatching()) {
            notify(getString(R.string.passwords_mismatch));
            return;
        }
        viewModel.register(registrationForm.collect());
    }

    @Override
    public void dismiss() {
        navigate(LoginActivity.class);
        finish();
    }

    @Override
    public void populate(List<String> occupations) {
        registrationForm.populate(occupations);
    }
}