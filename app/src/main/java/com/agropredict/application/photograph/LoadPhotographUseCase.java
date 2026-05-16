package com.agropredict.application.photograph;

import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.domain.photograph.Photograph;
import java.util.Objects;

public final class LoadPhotographUseCase {
    private final IPhotographRepository photographRepository;

    public LoadPhotographUseCase(IPhotographRepository photographRepository) {
        this.photographRepository = Objects.requireNonNull(photographRepository, "load photograph use case requires a photograph repository");
    }

    public Photograph find(String identifier) {
        return photographRepository.find(identifier);
    }
}
