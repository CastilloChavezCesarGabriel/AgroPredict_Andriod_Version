package com.agropredict.application.operation_result;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.agropredict.visitor.TestOperationResultVisitor;

import org.junit.Test;

public final class OperationResultTest {

    @Test
    public void testSucceed() {
        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        OperationResult.succeed("id_123").accept(visitor);
        assertTrue(visitor.isCompleted("id_123"));
    }

    @Test
    public void testFail() {
        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        OperationResult.fail().accept(visitor);
        assertFalse(visitor.isCompleted());
    }

    @Test
    public void testReject() {
        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        OperationResult.reject("Account locked").accept(visitor);
        assertTrue(visitor.isRejected("Account locked"));
    }
}
