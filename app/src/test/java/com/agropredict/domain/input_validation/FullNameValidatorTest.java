package com.agropredict.domain.input_validation;

import com.agropredict.application.input_validation.FullNameValidator;
import com.agropredict.factory.StubFullNameFailureFactory;
import org.junit.Test;

public final class FullNameValidatorTest {
    private final ValidatorTester tester = new ValidatorTester(new FullNameValidator(new StubFullNameFailureFactory()));

    @Test
    public void testValidFullName() {
        tester.accepts("Juan Perez");
    }

    @Test
    public void testValidSingleName() {
        tester.accepts("Juan");
    }

    @Test
    public void testValidAccentedName() {
        tester.accepts("Jose Garcia");
    }

    @Test
    public void testMinimumLength() {
        tester.accepts("Jo");
    }

    @Test
    public void testMaximumLength() {
        tester.accepts("A".repeat(50));
    }

    @Test
    public void testNullName() {
        tester.rejects(null);
    }

    @Test
    public void testEmptyName() {
        tester.rejects("");
    }

    @Test
    public void testSingleCharacter() {
        tester.rejects("J");
    }

    @Test
    public void testTooLong() {
        tester.rejects("A".repeat(51));
    }

    @Test
    public void testOnlySpaces() {
        tester.rejects("     ");
    }

    @Test
    public void testContainsNumbers() {
        tester.rejects("Juan123");
    }

    @Test
    public void testContainsSpecialChars() {
        tester.rejects("Juan@Perez");
    }

    @Test
    public void testLeadingTrailingSpaces() {
        tester.accepts("  Juan Perez  ");
    }
}
