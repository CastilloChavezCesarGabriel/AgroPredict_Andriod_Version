package com.agropredict.presentation.user_interface.export;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.core.content.FileProvider;
import com.agropredict.R;
import java.io.File;
import java.util.Objects;

public final class IntentCsvSharer implements ICsvSharer {
    private final Context context;

    public IntentCsvSharer(Context context) {
        this.context = Objects.requireNonNull(context, "intent csv sharer requires a context");
    }

    @Override
    public void share(String filePath) {
        Uri uri = FileProvider.getUriForFile(context,
                context.getPackageName() + ".fileprovider", new File(filePath));
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/csv");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_report)));
    }
}
