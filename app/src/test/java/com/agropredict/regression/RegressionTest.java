package com.agropredict.regression;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.agropredict.domain.LoginAttempt;
import com.agropredict.domain.Session;
import com.agropredict.domain.input_validation.EmailValidator;
import com.agropredict.domain.input_validation.PasswordValidator;
import com.agropredict.domain.input_validation.PhoneNumberValidator;
import com.agropredict.domain.input_validation.UsernameValidator;
import com.agropredict.visitor.TestClassificationResultVisitor;
import com.agropredict.visitor.TestOccupationVisitor;
import com.agropredict.application.operation_result.ClassificationResult;
import com.agropredict.infrastructure.security.PasswordHasher;

import org.junit.Test;

public final class RegressionTest {

    @Test
    public void testBugEmailWithLeadingDot() {
        assertFalse(new EmailValidator().isValid(".user@example.com"));
    }

    @Test
    public void testBugEmailWithConsecutiveDots() {
        assertFalse(new EmailValidator().isValid("user..name@example.com"));
    }

    @Test
    public void testBugPasswordExactlyEightCharsAllCriteria() {
        assertTrue(new PasswordValidator().isValid("Aa1!xxxx"));
    }

    @Test
    public void testBugPasswordSevenCharsWithAllCriteria() {
        assertFalse(new PasswordValidator().isValid("Aa1!xxx"));
    }

    @Test
    public void testBugUsernameAllUnderscoresNoLetters() {
        assertFalse(new UsernameValidator().isValid("_____"));
    }

    @Test
    public void testBugUsernameAllDigitsNoLetters() {
        assertFalse(new UsernameValidator().isValid("12345"));
    }

    @Test
    public void testBugPhoneNullIsValid() {
        assertTrue(new PhoneNumberValidator().isValid(null));
    }

    @Test
    public void testBugPhoneEmptyIsValid() {
        assertTrue(new PhoneNumberValidator().isValid(""));
    }

    @Test
    public void testBugSessionNullOccupationNotAdvanced() {
        TestOccupationVisitor handler = new TestOccupationVisitor();
        new Session("user_1", null).observe(handler);
        assertFalse(handler.sawAdvanced());
    }

    @Test
    public void testBugLoginAttemptBlockResetAfterExpiry() {
        long now = System.currentTimeMillis();
        LoginAttempt attempt = new LoginAttempt();
        for (int count = 0; count < 5; count++) attempt = attempt.fail(now);
        assertTrue(attempt.isBlocked(now));
        assertFalse(attempt.isBlocked(now + 5 * 60 * 1000 + 1));
        LoginAttempt reset = attempt.fail(now + 5 * 60 * 1000 + 1);
        assertFalse(reset.isExhausted());
    }

    @Test
    public void testBugHashVerificationWithCorruptedHash() {
        assertFalse(new PasswordHasher().verify("password", "corrupted_no_delimiter"));
    }

    @Test
    public void testBugClassificationBoundaryAt060() {
        TestClassificationResultVisitor visitor = new TestClassificationResultVisitor();
        new ClassificationResult("Corn", 0.6).accept(visitor);
        assertTrue(visitor.wasAccepted());
    }

    @Test
    public void testBugClassificationBoundaryAt059() {
        TestClassificationResultVisitor visitor = new TestClassificationResultVisitor();
        new ClassificationResult("Corn", 0.59).accept(visitor);
        assertTrue(visitor.wasRejected());
    }

    @Test
    public void testBugEmptySessionIdentifier() {
        assertFalse(new Session("", "Farmer").isActive());
    }
}