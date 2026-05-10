package com.agropredict.smoke;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import com.agropredict.application.operation_result.SuccessfulOperation;
import com.agropredict.application.operation_result.FailedOperation;
import com.agropredict.application.operation_result.RejectedOperation;
import com.agropredict.domain.diagnostic.ConfidentClassification;
import com.agropredict.domain.diagnostic.UnconfidentClassification;
import com.agropredict.domain.CapturingLoginGate;
import com.agropredict.domain.identifier.IdentifierFactory;
import com.agropredict.domain.authentication.ILoginAttempt;
import com.agropredict.domain.authentication.InitialAttempt;
import com.agropredict.domain.authentication.Session;
import com.agropredict.domain.input_validation.EmailValidator;
import com.agropredict.domain.input_validation.FullNameValidator;
import com.agropredict.domain.input_validation.PasswordValidator;
import com.agropredict.domain.input_validation.UsernameValidator;
import com.agropredict.domain.input_validation.ValidatorTester;
import com.agropredict.infrastructure.security.PasswordHasher;
import org.junit.Test;

public final class SmokeTest {
    @Test
    public void testValidatorsInstantiate() {
        new EmailValidator();
        new PasswordValidator();
        new UsernameValidator();
        new FullNameValidator();
    }

    @Test
    public void testBasicEmailValidation() {
        new ValidatorTester(new EmailValidator()).accepts("test@example.com");
    }

    @Test
    public void testBasicPasswordValidation() {
        new ValidatorTester(new PasswordValidator()).accepts("Passw0rd!XYZ");
    }

    @Test
    public void testBasicUsernameValidation() {
        new ValidatorTester(new UsernameValidator()).accepts("testuser");
    }

    @Test
    public void testBasicNameValidation() {
        new ValidatorTester(new FullNameValidator()).accepts("Test User");
    }

    @Test
    public void testPasswordHashingWorks() {
        PasswordHasher hasher = new PasswordHasher();
        String hash = hasher.hash("test");
        assertNotNull(hash);
        assertTrue(hasher.verify("test", hash));
    }

    @Test
    public void testIdentifierGeneration() {
        String identifier = IdentifierFactory.generate("smoke");
        assertNotNull(identifier);
        assertTrue(identifier.startsWith("smoke_"));
    }

    @Test
    public void testSessionCreation() {
        Session session = new Session("user_1", "Farmer");
        assertNotNull(session);
    }

    @Test
    public void testILoginAttemptCreation() {
        ILoginAttempt attempt = new InitialAttempt();
        CapturingLoginGate gate = new CapturingLoginGate();
        attempt.evaluate(System.currentTimeMillis(), gate);
        assertTrue(gate.hasReceived("allow"));
    }

    @Test
    public void testOperationResultFactories() {
        new SuccessfulOperation("ok");
        new FailedOperation();
        new RejectedOperation("reason");
    }

    @Test
    public void testClassificationResultCreation() {
        new ConfidentClassification("Tomato", 0.9);
        new UnconfidentClassification("low confidence");
    }
}