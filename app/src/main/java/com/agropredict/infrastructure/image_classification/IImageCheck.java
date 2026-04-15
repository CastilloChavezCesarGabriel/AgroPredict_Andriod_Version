package com.agropredict.infrastructure.image_classification;

import java.io.File;

public interface IImageCheck {
    String inspect(String imagePath, File file);
}