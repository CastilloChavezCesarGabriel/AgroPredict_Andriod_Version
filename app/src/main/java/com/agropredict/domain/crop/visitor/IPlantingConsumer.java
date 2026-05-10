package com.agropredict.domain.crop.visitor;

public interface IPlantingConsumer {
    void track(String date, String stageIdentifier);
}