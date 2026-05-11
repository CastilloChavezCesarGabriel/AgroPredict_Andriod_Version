package com.agropredict.application.profile;

import com.agropredict.application.repository.IUserRepository;
import com.agropredict.domain.user.User;
import com.agropredict.domain.user.visitor.IUserIdentityConsumer;
import java.util.Objects;

public final class GreetUserUseCase {
    private final IUserRepository userRepository;

    public GreetUserUseCase(IUserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository, "greet user use case requires a user repository");
    }

    public void greet(String userIdentifier, IUserIdentityConsumer consumer) {
        User user = userRepository.find(userIdentifier);
        if (user != null) user.describe(consumer);
    }
}
