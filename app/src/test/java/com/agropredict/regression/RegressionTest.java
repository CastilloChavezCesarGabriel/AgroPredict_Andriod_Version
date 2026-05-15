package com.agropredict.regression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.agropredict.domain.authentication.attempt.ILoginAttempt;
import com.agropredict.domain.authentication.attempt.InitialAttempt;
import com.agropredict.domain.authentication.gate.LoginGate;
import com.agropredict.domain.authentication.gate.LoginRejectedException;
import com.agropredict.domain.authentication.session.Session;
import com.agropredict.application.input_validation.EmailValidator;
import com.agropredict.application.input_validation.PasswordValidator;
import com.agropredict.application.input_validation.PhoneNumberValidator;
import com.agropredict.application.input_validation.UsernameValidator;
import com.agropredict.domain.input_validation.ValidatorTester;
import com.agropredict.factory.StubEmailFailureFactory;
import com.agropredict.factory.StubPasswordFailureFactory;
import com.agropredict.factory.StubPhoneNumberFailureFactory;
import com.agropredict.factory.StubUsernameFailureFactory;
import com.agropredict.visitor.LimitExpecter;
import com.agropredict.domain.diagnostic.classification.Prediction;
import com.agropredict.infrastructure.security.PasswordHasher;
import org.junit.Test;

public final class RegressionTest {

    @Test
    public void testBugEmailWithLeadingDot() {
        new ValidatorTester(new EmailValidator(new StubEmailFailureFactory())).rejects(".user@example.com");
    }

    @Test
    public void testBugEmailWithConsecutiveDots() {
        new ValidatorTester(new EmailValidator(new StubEmailFailureFactory())).rejects("user..name@example.com");
    }

    @Test
    public void testBugPasswordExactlyTwelveCharsAllCriteria() {
        new ValidatorTester(new PasswordValidator(new StubPasswordFailureFactory())).accepts("Aa1!xxxxxxxx");
    }

    @Test
    public void testBugPasswordElevenCharsWithAllCriteria() {
        new ValidatorTester(new PasswordValidator(new StubPasswordFailureFactory())).rejects("Aa1!xxxxxxx");
    }

    @Test
    public void testBugUsernameAllUnderscoresNoLetters() {
        new ValidatorTester(new UsernameValidator(new StubUsernameFailureFactory())).rejects("_____");
    }

    @Test
    public void testBugUsernameAllDigitsNoLetters() {
        new ValidatorTester(new UsernameValidator(new StubUsernameFailureFactory())).rejects("12345");
    }

    @Test
    public void testBugPhoneNullIsValid() {
        new ValidatorTester(new PhoneNumberValidator(new StubPhoneNumberFailureFactory())).accepts(null);
    }

    @Test
    public void testBugPhoneEmptyIsValid() {
        new ValidatorTester(new PhoneNumberValidator(new StubPhoneNumberFailureFactory())).accepts("");
    }

    @Test
    public void testBugSessionNullOccupationNotAdvanced() {
        new Session("user_1", null).observe(new LimitExpecter());
    }

    @Test
    public void testBugILoginAttemptBlockResetAfterExpiry() {
        long now = System.currentTimeMillis();
        ILoginAttempt attempt = new InitialAttempt();
        for (int count = 0; count < 5; count++) attempt = attempt.fail(now);
        long afterExpiry = now + 5 * 60 * 1000 + 1;
        assertEquals("blocked", evaluate(attempt, now));
        ILoginAttempt reset = attempt.fail(afterExpiry);
        assertEquals("allow", evaluate(reset, afterExpiry));
    }

    @Test
    public void testBugHashVerificationWithCorruptedHash() {
        assertFalse(new PasswordHasher().verify("password", "corrupted_no_delimiter"));
    }

    @Test
    public void testBugClassificationBoundaryAt045() {
        assertTrue(new Prediction("Corn", 0.45).isConfident());
    }

    @Test
    public void testBugClassificationBoundaryAt044() {
        assertFalse(new Prediction("Corn", 0.44).isConfident());
    }

    @Test
    public void testBugEmptySessionIdentifier() {
        org.junit.Assert.assertThrows(IllegalArgumentException.class, () -> new Session("", "Farmer"));
    }

    private String evaluate(ILoginAttempt attempt, long time) {
        LoginGate gate = new LoginGate(
            callback -> callback.receive("blocked"),
            callback -> callback.receive("exhausted"));
        try {
            attempt.evaluate(time, gate);
            return "allow";
        } catch (LoginRejectedException rejected) {
            return rejected.getMessage();
        }
    }
}
