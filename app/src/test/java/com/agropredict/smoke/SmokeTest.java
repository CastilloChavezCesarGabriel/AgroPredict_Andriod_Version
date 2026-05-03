package com.agropredict.smoke;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import com.agropredict.application.operation_result.ClassificationResult;
import com.agropredict.application.operation_result.OperationResult;
import com.agropredict.application.operation_result.RegistrationResult;
import com.agropredict.domain.CapturingLoginGate;
import com.agropredict.domain.Identifier;
import com.agropredict.domain.LoginAttempt;
import com.agropredict.domain.Session;
import com.agropredict.domain.input_validation.EmailValidator;
import com.agropredict.domain.input_validation.FullNameValidator;
import com.agropredict.domain.input_validation.PasswordValidator;
import com.agropredict.domain.input_validation.UsernameValidator;
import com.agropredict.infrastructure.security.PasswordHasher;
import org.junit.Test;

public final class SmokeTest {

    @Test
    public void testValidatorsInstantiate() {
        assertTrue(new EmailValidator().isValid("test@example.com"));
        assertTrue(new PasswordValidator().isValid("Passw0rd!"));
        assertTrue(new UsernameValidator().isValid("testuser"));
        assertTrue(new FullNameValidator().isValid("Test User"));
    }

    @Test
    public void testBasicEmailValidation() {
        assertTrue(new EmailValidator().isValid("test@example.com"));
    }

    @Test
    public void testBasicPasswordValidation() {
        assertTrue(new PasswordValidator().isValid("Passw0rd!"));
    }

    @Test
    public void testBasicUsernameValidation() {
        assertTrue(new UsernameValidator().isValid("testuser"));
    }

    @Test
    public void testBasicNameValidation() {
        assertTrue(new FullNameValidator().isValid("Test User"));
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
        String identifier = Identifier.generate("smoke");
        assertNotNull(identifier);
        assertTrue(identifier.startsWith("smoke_"));
    }

    @Test
    public void testSessionCreation() {
        Session session = new Session("user_1", "Farmer");
        assertTrue(session.isActive());
    }

    @Test
    public void testLoginAttemptCreation() {
        LoginAttempt attempt = new LoginAttempt(0, 0);
        CapturingLoginGate gate = new CapturingLoginGate();
        attempt.evaluate(System.currentTimeMillis(), gate);
        assertTrue(gate.hasReceived("allow"));
    }

    @Test
    public void testOperationResultFactories() {
        assertNotNull(OperationResult.succeed("ok"));
        assertNotNull(OperationResult.fail());
        assertNotNull(OperationResult.reject("reason"));
    }

    @Test
    public void testRegistrationResultFactories() {
        assertNotNull(RegistrationResult.succeed());
        assertNotNull(RegistrationResult.fail("error"));
    }

    @Test
    public void testClassificationResultCreation() {
        ClassificationResult result = new ClassificationResult("Tomato", 0.9);
        assertNotNull(result);
    }
}