package com.agropredict.domain.user;

import com.agropredict.domain.identifier.IIdentifierConsumer;
import com.agropredict.domain.user.visitor.ICredentialConsumer;
import com.agropredict.domain.user.visitor.IEmailConsumer;
import com.agropredict.domain.user.visitor.IOccupationConsumer;
import com.agropredict.domain.user.visitor.IPhoneConsumer;
import com.agropredict.domain.user.visitor.IUserIdentityConsumer;
import com.agropredict.domain.user.visitor.IUsernameConsumer;

public final class NoUser implements IUser {
    @Override
    public void describe(IUserIdentityConsumer consumer) {}

    @Override
    public void contact(IPhoneConsumer consumer) {}

    @Override
    public void enroll(IUsernameConsumer consumer) {}

    @Override
    public void authenticate(ICredentialConsumer consumer) {}

    @Override
    public void mail(IEmailConsumer consumer) {}

    @Override
    public void classify(IOccupationConsumer consumer) {}

    @Override
    public void identify(IIdentifierConsumer consumer) {}
}