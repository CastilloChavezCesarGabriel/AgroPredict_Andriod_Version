package com.agropredict.application.operation_result;

import static org.junit.Assert.assertTrue;
import com.agropredict.visitor.TestRegistrationResultVisitor;
import org.junit.Test;

public final class RegistrationResultTest {

    @Test
    public void testSucceed() {
        TestRegistrationResultVisitor visitor = new TestRegistrationResultVisitor();
        RegistrationResult.succeed().accept(visitor);
        assertTrue(visitor.isCompleted());
    }

    @Test
    public void testFail() {
        TestRegistrationResultVisitor visitor = new TestRegistrationResultVisitor();
        RegistrationResult.fail("Email already taken").accept(visitor);
        assertTrue(visitor.isRejected("Email already taken"));
    }
}