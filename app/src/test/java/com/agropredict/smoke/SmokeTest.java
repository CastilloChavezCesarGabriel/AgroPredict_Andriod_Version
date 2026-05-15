package com.agropredict.smoke;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import com.agropredict.application.operation_result.SuccessfulOperation;
import com.agropredict.application.operation_result.FailedOperation;
import com.agropredict.application.operation_result.RejectedOperation;
import com.agropredict.domain.diagnostic.classification.ConfidentClassification;
import com.agropredict.domain.diagnostic.classification.UnconfidentClassification;
import com.agropredict.domain.authentication.gate.LoginGate;
import com.agropredict.domain.identifier.IdentifierFactory;
import com.agropredict.domain.authentication.attempt.ILoginAttempt;
import com.agropredict.domain.authentication.attempt.InitialAttempt;
import com.agropredict.domain.authentication.session.Session;
import com.agropredict.application.input_validation.EmailValidator;
import com.agropredict.application.input_validation.FullNameValidator;
import com.agropredict.application.input_validation.PasswordValidator;
import com.agropredict.application.input_validation.UsernameValidator;
import com.agropredict.domain.input_validation.ValidatorTester;
import com.agropredict.factory.StubEmailFailureFactory;
import com.agropredict.factory.StubFullNameFailureFactory;
import com.agropredict.factory.StubPasswordFailureFactory;
import com.agropredict.factory.StubUsernameFailureFactory;
import com.agropredict.infrastructure.security.PasswordHasher;
import org.junit.Test;

public final class SmokeTest {
    @Test
    public void testValidatorsInstantiate() {
        new EmailValidator(new StubEmailFailureFactory());
        new PasswordValidator(new StubPasswordFailureFactory());
        new UsernameValidator(new StubUsernameFailureFactory());
        new FullNameValidator(new StubFullNameFailureFactory());
    }

    @Test
    public void testBasicEmailValidation() {
        new ValidatorTester(new EmailValidator(new StubEmailFailureFactory())).accepts("test@example.com");
    }

    @Test
    public void testBasicPasswordValidation() {
        new ValidatorTester(new PasswordValidator(new StubPasswordFailureFactory())).accepts("Passw0rd!XYZ");
    }

    @Test
    public void testBasicUsernameValidation() {
        new ValidatorTester(new UsernameValidator(new StubUsernameFailureFactory())).accepts("testuser");
    }

    @Test
    public void testBasicNameValidation() {
        new ValidatorTester(new FullNameValidator(new StubFullNameFailureFactory())).accepts("Test User");
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
    public void testILoginAttemptAllowsInitialEvaluation() {
        ILoginAttempt attempt = new InitialAttempt();
        LoginGate gate = new LoginGate(
            callback -> callback.receive("blocked"),
            callback -> callback.receive("exhausted"));
        attempt.evaluate(System.currentTimeMillis(), gate);
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