package com.agropredict.application.usecase.crop;

import com.agropredict.application.repository.IClearable;

public final class CropCleanup implements IClearable {
    private final IClearable first;
    private final IClearable second;

    public CropCleanup(IClearable first, IClearable second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public void clear(String cropIdentifier) {
        first.clear(cropIdentifier);
        second.clear(cropIdentifier);
    }
}
