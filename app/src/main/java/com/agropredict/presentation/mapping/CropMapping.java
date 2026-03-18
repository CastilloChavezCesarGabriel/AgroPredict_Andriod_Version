package com.agropredict.presentation.mapping;

import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.value.crop.CropContent;
import com.agropredict.domain.value.crop.CropData;
import com.agropredict.domain.value.crop.CropDetail;
import com.agropredict.domain.value.crop.CropEnvironment;
import com.agropredict.domain.value.crop.CropLocation;
import com.agropredict.domain.value.crop.CropOwnership;
import com.agropredict.domain.value.crop.CropSoil;
import com.agropredict.domain.visitor.ICropContentVisitor;
import com.agropredict.domain.visitor.ICropDataVisitor;
import com.agropredict.domain.visitor.ICropDetailVisitor;
import com.agropredict.domain.visitor.ICropEnvironmentVisitor;
import com.agropredict.domain.visitor.ICropLocationVisitor;
import com.agropredict.domain.visitor.ICropSoilVisitor;
import com.agropredict.domain.visitor.ICropVisitor;
import java.util.HashMap;
import java.util.Map;

public final class CropMapping implements ICropVisitor, ICropDataVisitor,
        ICropContentVisitor, ICropEnvironmentVisitor, ICropDetailVisitor,
        ICropLocationVisitor, ICropSoilVisitor {

    private final Map<String, Object> content;

    public CropMapping() {
        this.content = new HashMap<>();
    }

    public Map<String, String> map(Crop crop) {
        content.clear();
        crop.accept(this);
        return convert();
    }

    @Override
    public void visit(String identifier, CropData data) {
        content.put("identifier", identifier);
        data.accept(this);
    }

    @Override
    public void visit(CropDetail detail, CropContent cropContent) {
        if (detail != null) detail.accept(this);
        if (cropContent != null) cropContent.accept(this);
    }

    @Override
    public void visit(CropEnvironment environment, CropOwnership ownership) {
        if (environment != null) environment.accept(this);
    }

    @Override
    public void visit(CropLocation location, CropSoil soil) {
        if (location != null) location.accept(this);
        if (soil != null) soil.accept(this);
    }

    @Override
    public void visit(String cropType, String fieldName) {
        content.put("crop_type", cropType);
        content.put("field_name", fieldName);
    }

    @Override
    public void visitLocation(String location, String plantingDate) {
        content.put("location", location);
        content.put("planting_date", plantingDate);
    }

    @Override
    public void visitSoil(String soilTypeIdentifier, String area) {
        content.put("soil_type", soilTypeIdentifier);
        content.put("area", area);
    }

    private Map<String, String> convert() {
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : content.entrySet()) {
            if (entry.getValue() != null) {
                result.put(entry.getKey(), entry.getValue().toString());
            }
        }
        return result;
    }
}
