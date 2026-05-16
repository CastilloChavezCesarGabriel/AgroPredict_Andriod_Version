package com.agropredict.presentation.viewmodel.profile;

import android.app.Activity;
import android.widget.TextView;
import com.agropredict.R;
import com.agropredict.domain.user.IUser;
import com.agropredict.domain.user.visitor.IEmailConsumer;
import com.agropredict.domain.user.visitor.IOccupationConsumer;
import com.agropredict.domain.user.visitor.IPhoneConsumer;
import com.agropredict.domain.user.visitor.IUserIdentityConsumer;
import com.agropredict.domain.user.visitor.IUsernameConsumer;
import java.util.Objects;

public final class ProfilePresenter implements
        IUserIdentityConsumer,
        IPhoneConsumer,
        IUsernameConsumer,
        IOccupationConsumer,
        IEmailConsumer {

    private final Activity activity;

    public ProfilePresenter(Activity activity) {
        this.activity = Objects.requireNonNull(activity, "profile presenter requires an activity");
    }

    public void render(IUser user) {
        user.describe(this);
        user.contact(this);
        user.enroll(this);
        user.classify(this);
        user.mail(this);
    }

    @Override
    public void describe(String identifier, String fullName) {
        write(R.id.tvFullName, fullName);
    }

    @Override
    public void contact(String number) {
        write(R.id.tvPhone, number == null || number.isEmpty() ? activity.getString(R.string.not_specified) : number);
    }

    @Override
    public void enroll(String username) {
        write(R.id.tvUsername, username);
    }

    @Override
    public void classify(String occupation) {
        write(R.id.tvOccupation, occupation);
    }

    @Override
    public void mail(String email) {
        write(R.id.tvEmail, email);
    }

    private void write(int textViewId, String value) {
        ((TextView) activity.findViewById(textViewId)).setText(value);
    }
}