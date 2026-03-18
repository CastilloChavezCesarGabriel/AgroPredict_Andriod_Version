package com.agropredict.infrastructure.validation;

import android.graphics.BitmapFactory;
import com.agropredict.application.service.IImageValidatorService;
import java.io.File;

public final class AndroidImageValidatorService implements IImageValidatorService {
    private static final long MINIMUM_FILE_SIZE = 10 * 1024;
    private static final long MAXIMUM_FILE_SIZE = 10 * 1024 * 1024;
    private static final int MINIMUM_DIMENSION = 100;
    private static final int MAXIMUM_DIMENSION = 8000;

    @Override
    public String validate(String imagePath) {
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) return "El archivo no existe";
        String formatError = validateFormat(imagePath);
        if (formatError != null) return formatError;
        String sizeError = validateSize(imageFile);
        if (sizeError != null) return sizeError;
        return validateDimensions(imagePath);
    }

    private String validateFormat(String path) {
        String lowercasePath = path.toLowerCase();
        boolean isJpg = lowercasePath.endsWith(".jpg");
        boolean isJpeg = lowercasePath.endsWith(".jpeg");
        boolean isPng = lowercasePath.endsWith(".png");
        if (!isJpg && !isJpeg && !isPng) {
            return "Formato no soportado. Use JPG o PNG";
        }
        return null;
    }

    private String validateSize(File imageFile) {
        long fileSize = imageFile.length();
        if (fileSize < MINIMUM_FILE_SIZE) {
            return "La imagen es demasiado pequena (minimo 10KB)";
        }
        if (fileSize > MAXIMUM_FILE_SIZE) {
            return "La imagen es demasiado grande (maximo 10MB)";
        }
        return null;
    }

    private String validateDimensions(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        if (isTooSmall(options)) {
            return "La imagen es demasiado pequena (minimo 100x100)";
        }
        if (isTooLarge(options)) {
            return "La imagen es demasiado grande (maximo 8000x8000)";
        }
        return null;
    }

    private boolean isTooSmall(BitmapFactory.Options options) {
        return options.outWidth < MINIMUM_DIMENSION || options.outHeight < MINIMUM_DIMENSION;
    }

    private boolean isTooLarge(BitmapFactory.Options options) {
        return options.outWidth > MAXIMUM_DIMENSION || options.outHeight > MAXIMUM_DIMENSION;
    }
}