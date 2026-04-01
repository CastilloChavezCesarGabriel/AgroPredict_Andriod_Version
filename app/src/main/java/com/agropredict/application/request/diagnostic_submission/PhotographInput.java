package com.agropredict.application.request.diagnostic_submission;

import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.domain.Identifier;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.Photograph;

public final class PhotographInput {
    private final String imagePath;

    public PhotographInput(String imagePath) {
        this.imagePath = imagePath;
    }

    public void store(IPhotographRepository repository, Crop crop) {
        Photograph photograph = new Photograph(Identifier.generate("image"), imagePath);
        repository.store(photograph, crop);
    }
}
