package com.agropredict.presentation.user_interface.export;

import com.agropredict.domain.guard.ArgumentPrecondition;

public final class PdfExportedFile implements IExportedFile {
    private final String filePath;
    private final IPdfOpener opener;

    public PdfExportedFile(String filePath, IPdfOpener opener) {
        this.filePath = ArgumentPrecondition.validate(filePath, "pdf exported file path");
        this.opener = opener;
    }

    @Override
    public void present() {
        opener.open(filePath);
    }
}