package com.agropredict.domain.crop.visitor;

public interface IFieldConsumer {
    void locate(String name, String location);
}