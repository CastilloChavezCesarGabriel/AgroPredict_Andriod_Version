package com.agropredict.presentation.user_interface.export;

import com.agropredict.domain.guard.ArgumentPrecondition;
import java.util.Objects;

public final class CsvExportedFile implements IExportedFile {
    private final String filePath;
    private final ICsvSharer sharer;

    public CsvExportedFile(String filePath, ICsvSharer sharer) {
        this.filePath = ArgumentPrecondition.validate(filePath, "csv exported file path");
        this.sharer = Objects.requireNonNull(sharer, "csv exported file requires a sharer");
    }

    @Override
    public void present() {
        sharer.share(filePath);
    }
}
