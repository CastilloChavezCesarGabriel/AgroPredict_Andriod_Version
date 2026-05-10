package com.agropredict.application.operation_result;

import com.agropredict.visitor.FailExpecter;
import com.agropredict.visitor.RejectExpecter;
import com.agropredict.visitor.SucceedExpecter;
import org.junit.Test;

public final class OperationResultTest {
    @Test
    public void testSucceed() {
        new SuccessfulOperation("id_123").accept(new SucceedExpecter("id_123"));
    }

    @Test
    public void testFail() {
        new FailedOperation().accept(new FailExpecter());
    }

    @Test
    public void testReject() {
        new RejectedOperation("Account locked").accept(new RejectExpecter("Account locked"));
    }
}