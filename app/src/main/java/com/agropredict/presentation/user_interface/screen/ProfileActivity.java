package com.agropredict.presentation.user_interface.screen;

import android.os.Bundle;
import com.agropredict.R;
import com.agropredict.application.factory.IAccessFactory;
import com.agropredict.application.authentication.usecase.CheckSessionUseCase;
import com.agropredict.application.profile.FindUserUseCase;
import com.agropredict.domain.user.IUser;
import com.agropredict.presentation.viewmodel.profile.ProfilePresenter;

public final class ProfileActivity extends BaseActivity {
    private FindUserUseCase findUserUseCase;
    private CheckSessionUseCase checkSessionUseCase;
    private ProfilePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialize();
        load();
    }

    private void initialize() {
        IAccessFactory factory = (IAccessFactory) getApplication();
        findUserUseCase = new FindUserUseCase(factory.createUserRepository());
        checkSessionUseCase = new CheckSessionUseCase(factory.createSessionRepository());
        presenter = new ProfilePresenter(this);
    }

    private void load() {
        checkSessionUseCase.check(this::populate);
    }

    private void populate(String identifier, String occupation) {
        if (identifier == null || identifier.isEmpty()) return;
        IUser user = findUserUseCase.find(identifier);
        presenter.render(user);
    }
}
