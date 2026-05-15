package com.agropredict.domain.input_validation;

import com.agropredict.application.input_validation.PhoneNumberValidator;
import com.agropredict.factory.StubPhoneNumberFailureFactory;
import org.junit.Test;

public final class PhoneNumberValidatorTest {
    private final ValidatorTester tester = new ValidatorTester(new PhoneNumberValidator(new StubPhoneNumberFailureFactory()));

    @Test
    public void testValidPhone() {
        tester.accepts("3312345678");
    }

    @Test
    public void testValidLongPhone() {
        tester.accepts("523312345678");
    }

    @Test
    public void testMinimumLength() {
        tester.accepts("1234567");
    }

    @Test
    public void testMaximumLength() {
        tester.accepts("123456789012345");
    }

    @Test
    public void testNullPhone() {
        tester.accepts(null);
    }

    @Test
    public void testEmptyPhone() {
        tester.accepts("");
    }

    @Test
    public void testTooShort() {
        tester.rejects("123456");
    }

    @Test
    public void testTooLong() {
        tester.rejects("1234567890123456");
    }

    @Test
    public void testContainsLetters() {
        tester.rejects("33abc12345");
    }

    @Test
    public void testContainsDashes() {
        tester.rejects("33-1234-5678");
    }

    @Test
    public void testContainsSpaces() {
        tester.rejects("33 1234 5678");
    }

    @Test
    public void testContainsPlus() {
        tester.rejects("+523312345678");
    }
}
