package com.agropredict.domain.user;

import com.agropredict.domain.identifier.IIdentifierConsumer;
import com.agropredict.domain.user.visitor.ICredentialConsumer;
import com.agropredict.domain.user.visitor.IEmailConsumer;
import com.agropredict.domain.user.visitor.IOccupationConsumer;
import com.agropredict.domain.user.visitor.IPhoneConsumer;
import com.agropredict.domain.user.visitor.IUserIdentityConsumer;
import com.agropredict.domain.user.visitor.IUsernameConsumer;

public interface IUser {
    void describe(IUserIdentityConsumer consumer);
    void contact(IPhoneConsumer consumer);
    void enroll(IUsernameConsumer consumer);
    void authenticate(ICredentialConsumer consumer);
    void mail(IEmailConsumer consumer);
    void classify(IOccupationConsumer consumer);
    void identify(IIdentifierConsumer consumer);
}