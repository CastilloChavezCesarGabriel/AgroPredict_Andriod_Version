package com.agropredict.domain.input_validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public final class PasswordCriteriaTest {

    @Test
    public void testAllCriteriaMet() {
        PasswordCriteria criteria = new PasswordCriteria();
        for (char character : "Aa1!".toCharArray()) criteria.update(character);
        assertTrue(criteria.isValid());
    }

    @Test
    public void testFreshCriteriaInvalid() {
        PasswordCriteria criteria = new PasswordCriteria();
        assertFalse(criteria.isValid());
    }

    @Test
    public void testMissingUppercase() {
        PasswordCriteria criteria = new PasswordCriteria();
        for (char character : "a1!".toCharArray()) criteria.update(character);
        assertFalse(criteria.isValid());
    }

    @Test
    public void testMissingLowercase() {
        PasswordCriteria criteria = new PasswordCriteria();
        for (char character : "A1!".toCharArray()) criteria.update(character);
        assertFalse(criteria.isValid());
    }

    @Test
    public void testMissingDigit() {
        PasswordCriteria criteria = new PasswordCriteria();
        for (char character : "Aa!".toCharArray()) criteria.update(character);
        assertFalse(criteria.isValid());
    }

    @Test
    public void testMissingSpecial() {
        PasswordCriteria criteria = new PasswordCriteria();
        for (char character : "Aa1".toCharArray()) criteria.update(character);
        assertFalse(criteria.isValid());
    }

    @Test
    public void testIncrementalUpdate() {
        PasswordCriteria criteria = new PasswordCriteria();
        criteria.update('A');
        assertFalse(criteria.isValid());
        criteria.update('a');
        assertFalse(criteria.isValid());
        criteria.update('1');
        assertFalse(criteria.isValid());
        criteria.update('!');
        assertTrue(criteria.isValid());
    }
}
