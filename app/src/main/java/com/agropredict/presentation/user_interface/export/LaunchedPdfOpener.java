package com.agropredict.presentation.user_interface.export;

import android.content.Context;
import com.agropredict.presentation.user_interface.navigation.PdfLauncher;
import java.util.Objects;

public final class LaunchedPdfOpener implements IPdfOpener {
    private final Context context;

    public LaunchedPdfOpener(Context context) {
        this.context = Objects.requireNonNull(context, "launched pdf opener requires a context");
    }

    @Override
    public void open(String filePath) {
        new PdfLauncher().open(context, filePath);
    }
}
