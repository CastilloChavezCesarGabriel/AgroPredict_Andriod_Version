package com.agropredict.infrastructure.export;

public interface IReportWriter {
    void write(String label, String value);
}