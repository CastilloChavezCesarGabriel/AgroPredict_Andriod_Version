package com.agropredict.domain.input_validation;

import org.junit.Test;

public final class EmailValidatorTest {
    private final ValidatorTester tester = new ValidatorTester(new EmailValidator());

    @Test
    public void testValidEmail() {
        tester.accepts("user@example.com");
    }

    @Test
    public void testValidEmailWithDots() {
        tester.accepts("user.name@example.com");
    }

    @Test
    public void testValidEmailWithHyphens() {
        tester.accepts("user-name@example.com");
    }

    @Test
    public void testValidEmailWithUnderscore() {
        tester.accepts("user_name@example.com");
    }

    @Test
    public void testValidEmailWithNumbers() {
        tester.accepts("user123@example.com");
    }

    @Test
    public void testValidEmailSubdomain() {
        tester.accepts("user@mail.example.com");
    }

    @Test
    public void testNullEmail() {
        tester.rejects(null);
    }

    @Test
    public void testEmptyEmail() {
        tester.rejects("");
    }

    @Test
    public void testMissingAtSymbol() {
        tester.rejects("userexample.com");
    }

    @Test
    public void testMissingDomain() {
        tester.rejects("user@");
    }

    @Test
    public void testMissingUsername() {
        tester.rejects("@example.com");
    }

    @Test
    public void testMissingTld() {
        tester.rejects("user@example");
    }

    @Test
    public void testSingleCharTld() {
        tester.rejects("user@example.c");
    }

    @Test
    public void testSpacesInEmail() {
        tester.rejects("user @example.com");
    }

    @Test
    public void testDoubleAt() {
        tester.rejects("user@@example.com");
    }

    @Test
    public void testStartsWithDot() {
        tester.rejects(".user@example.com");
    }

    @Test
    public void testStartsWithSpecialChar() {
        tester.rejects("!user@example.com");
    }
}
