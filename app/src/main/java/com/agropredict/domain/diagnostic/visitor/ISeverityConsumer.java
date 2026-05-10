package com.agropredict.domain.diagnostic.visitor;

public interface ISeverityConsumer {
    void label(String text, int urgency);
}