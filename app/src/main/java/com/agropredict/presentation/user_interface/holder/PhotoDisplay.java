package com.agropredict.presentation.user_interface.holder;

import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import com.agropredict.application.repository.ICropImageRepository;
import com.agropredict.domain.entity.CropImage;
import com.agropredict.domain.visitor.crop.ICropImageVisitor;
import java.io.File;

public final class PhotoDisplay implements ICropImageVisitor {
    private final ImageView cropPhotoView;
    private final ICropImageRepository imageRepository;

    public PhotoDisplay(ImageView cropPhotoView, ICropImageRepository imageRepository) {
        this.cropPhotoView = cropPhotoView;
        this.imageRepository = imageRepository;
    }

    public void load(String imageIdentifier) {
        if (imageIdentifier == null) return;
        CropImage image = imageRepository.find(imageIdentifier);
        if (image != null) image.accept(this);
    }

    @Override
    public void visit(String identifier, String filePath) {
        if (filePath == null) return;
        File file = new File(filePath);
        if (file.exists()) {
            cropPhotoView.setImageBitmap(BitmapFactory.decodeFile(filePath));
            cropPhotoView.setVisibility(View.VISIBLE);
        }
    }
}