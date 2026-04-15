package com.agropredict.infrastructure.image_classification;

import android.graphics.BitmapFactory;
import java.io.File;

public final class DimensionCheck implements IImageCheck {
    private static final int MIN_PIXELS = 100;
    private static final int MAX_PIXELS = 8000;

    @Override
    public String inspect(String imagePath, File file) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        if (options.outWidth < MIN_PIXELS || options.outHeight < MIN_PIXELS)
            return "Image is too small (minimum 100x100)";
        if (options.outWidth > MAX_PIXELS || options.outHeight > MAX_PIXELS)
            return "Image is too large (maximum 8000x8000)";
        return null;
    }
}