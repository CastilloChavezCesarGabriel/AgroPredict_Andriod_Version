package com.agropredict.domain.input_validation;

import org.junit.Test;

public final class PasswordValidatorTest {
    private final ValidatorTester tester = new ValidatorTester(new PasswordValidator());

    @Test
    public void testValidComplexPassword() {
        tester.accepts("Passw0rd!XYZ");
    }

    @Test
    public void testExactlyTwelveCharactersWithComplexity() {
        tester.accepts("Aa1!xxxxxxxx");
    }

    @Test
    public void testNullPassword() {
        tester.rejects(null);
    }

    @Test
    public void testEmptyPassword() {
        tester.rejects("");
    }

    @Test
    public void testTooShortBelowTwelve() {
        tester.rejects("Aa1!xxxx");
    }

    @Test
    public void testElevenCharactersStillRejected() {
        tester.rejects("Aa1!xxxxxxx");
    }

    @Test
    public void testMissingUppercaseUnderPassphraseLength() {
        tester.rejects("passw0rd!xyz");
    }

    @Test
    public void testMissingLowercaseUnderPassphraseLength() {
        tester.rejects("PASSW0RD!XYZ");
    }

    @Test
    public void testMissingDigitUnderPassphraseLength() {
        tester.rejects("Password!XYZ");
    }

    @Test
    public void testMissingSpecialCharacterUnderPassphraseLength() {
        tester.rejects("Passw0rd1XYZ");
    }

    @Test
    public void testFifteenCharactersWithoutComplexityRejected() {
        tester.rejects("abcdefghijklmno");
    }

    @Test
    public void testPassphraseAtSixteenCharacters() {
        tester.accepts("correcthorseatps");
    }

    @Test
    public void testLongPassphraseWithoutComplexityAccepted() {
        tester.accepts("correcthorsebatterystaple");
    }

    @Test
    public void testTrivialPassphraseRejectedByVariety() {
        tester.rejects("aaaaaaaaaaaaaaaa");
    }

    @Test
    public void testTwoCharacterPassphraseRejectedByVariety() {
        tester.rejects("abababababababab");
    }

    @Test
    public void testFourDistinctCharactersInPassphraseRejected() {
        tester.rejects("abcdabcdabcdabcd");
    }

    @Test
    public void testFiveDistinctCharactersInPassphraseAccepted() {
        tester.accepts("abcdeabcdeabcdef");
    }

    @Test
    public void testLongComplexPasswordAccepted() {
        tester.accepts("MyV3ryL0ng&SecureP@ssw0rd!");
    }

    @Test
    public void testSpecialCharactersVarietyUnderPassphrase() {
        tester.accepts("Aa1@xxxxxxxx");
        tester.accepts("Aa1#xxxxxxxx");
        tester.accepts("Aa1$xxxxxxxx");
        tester.accepts("Aa1%xxxxxxxx");
    }
}
