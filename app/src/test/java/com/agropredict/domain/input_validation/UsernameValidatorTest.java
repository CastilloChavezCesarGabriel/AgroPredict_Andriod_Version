package com.agropredict.domain.input_validation;

import org.junit.Test;

public final class UsernameValidatorTest {
    private final ValidatorTester tester = new ValidatorTester(new UsernameValidator());

    @Test
    public void testValidUsername() {
        tester.accepts("JuanPerez");
    }

    @Test
    public void testValidUsernameWithUnderscore() {
        tester.accepts("juan_perez");
    }

    @Test
    public void testValidUsernameWithNumbers() {
        tester.accepts("user123");
    }

    @Test
    public void testMinimumLength() {
        tester.accepts("abcde");
    }

    @Test
    public void testMaximumLength() {
        tester.accepts("a".repeat(32));
    }

    @Test
    public void testNullUsername() {
        tester.rejects(null);
    }

    @Test
    public void testEmptyUsername() {
        tester.rejects("");
    }

    @Test
    public void testTooShort() {
        tester.rejects("abc");
    }

    @Test
    public void testFourCharacters() {
        tester.rejects("abcd");
    }

    @Test
    public void testTooLong() {
        tester.rejects("a".repeat(33));
    }

    @Test
    public void testOnlyNumbers() {
        tester.rejects("12345");
    }

    @Test
    public void testOnlyUnderscores() {
        tester.rejects("_____");
    }

    @Test
    public void testSpecialCharacters() {
        tester.rejects("user@name");
    }

    @Test
    public void testSpaces() {
        tester.rejects("user name");
    }

    @Test
    public void testHyphen() {
        tester.rejects("user-name");
    }

    @Test
    public void testNumbersWithOneLetter() {
        tester.accepts("1234a");
    }
}
