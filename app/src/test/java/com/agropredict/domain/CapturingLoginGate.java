package com.agropredict.domain;

import com.agropredict.domain.authentication.ILoginGate;

import java.util.ArrayList;
import java.util.List;

public final class CapturingLoginGate implements ILoginGate {
    private final List<String> verdicts = new ArrayList<>();

    @Override
    public void allow() {
        verdicts.add("allow");
    }

    @Override
    public void block() {
        verdicts.add("block");
    }

    @Override
    public void exhaust() {
        verdicts.add("exhaust");
    }

    public boolean hasReceived(String verdict) {
        return verdicts.contains(verdict);
    }
}