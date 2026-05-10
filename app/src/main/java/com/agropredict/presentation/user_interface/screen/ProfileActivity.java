package com.agropredict.presentation.user_interface.screen;

import android.os.Bundle;
import android.widget.TextView;
import com.agropredict.R;
import com.agropredict.application.factory.IAccessFactory;
import com.agropredict.application.usecase.authentication.CheckSessionUseCase;
import com.agropredict.application.usecase.profile.FindUserUseCase;
import com.agropredict.domain.user.User;
import com.agropredict.domain.user.visitor.IEmailConsumer;
import com.agropredict.domain.user.visitor.IOccupationConsumer;
import com.agropredict.domain.user.visitor.IPhoneConsumer;
import com.agropredict.domain.user.visitor.IUserIdentityConsumer;
import com.agropredict.domain.user.visitor.IUsernameConsumer;

public final class ProfileActivity extends BaseActivity implements
        IUserIdentityConsumer,
        IPhoneConsumer,
        IUsernameConsumer,
        IOccupationConsumer,
        IEmailConsumer {

    private FindUserUseCase findUserUseCase;
    private CheckSessionUseCase checkSessionUseCase;

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
    }

    private void load() {
        checkSessionUseCase.check(this::populate);
    }

    private void populate(String identifier, String occupation) {
        if (identifier == null || identifier.isEmpty()) return;
        User user = findUserUseCase.find(identifier);
        if (user == null) return;
        user.describe(this);
        user.contact(this);
        user.enroll(this);
        user.classify(this);
        user.mail(this);
    }

    @Override
    public void describe(String identifier, String fullName) {
        ((TextView) findViewById(R.id.tvFullName)).setText(fullName);
    }

    @Override
    public void contact(String number) {
        TextView phoneField = findViewById(R.id.tvPhone);
        phoneField.setText(number == null || number.isEmpty() ? getString(R.string.not_specified) : number);
    }

    @Override
    public void enroll(String username) {
        ((TextView) findViewById(R.id.tvUsername)).setText(username);
    }

    @Override
    public void classify(String occupation) {
        ((TextView) findViewById(R.id.tvOccupation)).setText(occupation);
    }

    @Override
    public void mail(String email) {
        ((TextView) findViewById(R.id.tvEmail)).setText(email);
    }
}
