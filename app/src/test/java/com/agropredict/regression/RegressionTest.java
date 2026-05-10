package com.agropredict.regression;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.agropredict.domain.CapturingLoginGate;
import com.agropredict.domain.authentication.ILoginAttempt;
import com.agropredict.domain.authentication.InitialAttempt;
import com.agropredict.domain.authentication.Session;
import com.agropredict.domain.input_validation.EmailValidator;
import com.agropredict.domain.input_validation.PasswordValidator;
import com.agropredict.domain.input_validation.PhoneNumberValidator;
import com.agropredict.domain.input_validation.UsernameValidator;
import com.agropredict.domain.input_validation.ValidatorTester;
import com.agropredict.visitor.LimitExpecter;
import com.agropredict.domain.diagnostic.Prediction;
import com.agropredict.infrastructure.security.PasswordHasher;
import org.junit.Test;
public final class RegressionTest {

    @Test
    public void testBugEmailWithLeadingDot() {
        new ValidatorTester(new EmailValidator()).rejects(".user@example.com");
    }

    @Test
    public void testBugEmailWithConsecutiveDots() {
        new ValidatorTester(new EmailValidator()).rejects("user..name@example.com");
    }

    @Test
    public void testBugPasswordExactlyTwelveCharsAllCriteria() {
        new ValidatorTester(new PasswordValidator()).accepts("Aa1!xxxxxxxx");
    }

    @Test
    public void testBugPasswordElevenCharsWithAllCriteria() {
        new ValidatorTester(new PasswordValidator()).rejects("Aa1!xxxxxxx");
    }

    @Test
    public void testBugUsernameAllUnderscoresNoLetters() {
        new ValidatorTester(new UsernameValidator()).rejects("_____");
    }

    @Test
    public void testBugUsernameAllDigitsNoLetters() {
        new ValidatorTester(new UsernameValidator()).rejects("12345");
    }

    @Test
    public void testBugPhoneNullIsValid() {
        new ValidatorTester(new PhoneNumberValidator()).accepts(null);
    }

    @Test
    public void testBugPhoneEmptyIsValid() {
        new ValidatorTester(new PhoneNumberValidator()).accepts("");
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
        CapturingLoginGate atFailure = new CapturingLoginGate();
        attempt.evaluate(now, atFailure);
        assertTrue(atFailure.hasReceived("block"));
        ILoginAttempt reset = attempt.fail(afterExpiry);
        CapturingLoginGate afterReset = new CapturingLoginGate();
        reset.evaluate(afterExpiry, afterReset);
        assertTrue(afterReset.hasReceived("allow"));
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
}