package com.agropredict.application.usecase.profile;

import com.agropredict.application.repository.IUserRepository;
import com.agropredict.domain.user.User;
import java.util.Objects;

public final class FindUserUseCase {
    private final IUserRepository userRepository;

    public FindUserUseCase(IUserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository, "find user use case requires a user repository");
    }

    public User find(String userIdentifier) {
        return userRepository.find(userIdentifier);
    }
}