package com.agropredict.presentation.user_interface.screen;

import android.os.Bundle;
import com.agropredict.R;
import com.agropredict.application.factory.IAccessFactory;
import com.agropredict.application.authentication.usecase.RegisterUseCase;
import com.agropredict.application.catalog.ListCatalogUseCase;
import com.agropredict.presentation.user_interface.form.RegistrationForm;
import com.agropredict.presentation.viewmodel.authentication.IRegisterView;
import com.agropredict.presentation.viewmodel.authentication.RegisterViewModel;
import java.util.List;

public final class RegisterActivity extends BaseActivity implements IRegisterView {
    private RegisterViewModel viewModel;
    private RegistrationForm registrationForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        bind();
        initialize();
        listen();
    }

    private void bind() {
        registrationForm = new RegistrationForm(this);
    }

    private void initialize() {
        IAccessFactory factory = (IAccessFactory) getApplication();
        RegisterUseCase useCase = new RegisterUseCase(
                factory.createUserRepository(), factory.createOccupationCatalog());
        ListCatalogUseCase occupations = new ListCatalogUseCase(factory.createOccupationCatalog());
        viewModel = new RegisterViewModel(useCase, this);
        viewModel.populate(occupations);
    }

    private void listen() {
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
    public void confirm() {
        notify(getString(R.string.registration_success));
    }

    @Override
    public void populate(List<String> occupations) {
        registrationForm.populate(occupations);
    }
}