package com.agropredict.application.operation_result;

import com.agropredict.domain.diagnostic.ConfidentClassification;
import com.agropredict.domain.diagnostic.UnconfidentClassification;
import com.agropredict.visitor.ConfidentExpecter;
import com.agropredict.visitor.UnconfidentExpecter;
import org.junit.Test;

public final class ClassificationResultTest {
    @Test
    public void testConfidentClassificationDispatchesCropAndConfidence() {
        new ConfidentClassification("Tomato", 0.85).accept(new ConfidentExpecter("Tomato", 0.85));
    }

    @Test
    public void testConfidentClassificationDispatchesPerfectConfidence() {
        new ConfidentClassification("Wheat", 1.0).accept(new ConfidentExpecter("Wheat", 1.0));
    }

    @Test
    public void testUnconfidentClassificationDispatchesReason() {
        new UnconfidentClassification("Could not identify the crop with certainty")
            .accept(new UnconfidentExpecter("Could not identify the crop with certainty"));
    }
}
