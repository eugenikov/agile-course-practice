package ru.unn.agile.QuadraticEquation.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ComplexTests {

    @Test
    public void canCreateComplexNumberWithInitialValues() {
        Complex number = new Complex(1, 1);
        assertNotNull(number);
    }

    @Test
    public void canCreateZeroComplexNumber() {
        Complex number = new Complex(0, 0);
        assertNotNull(number);
    }

    @Test
    public void canSetInitialRealValue() {
        Complex number = new Complex(1, 2);
        assertEquals(1.0, number.getReal(), DELTA);
    }

    @Test
    public void canSetZeroRealValue() {
        Complex number = new Complex(0, 2);
        assertEquals(0.0, number.getReal(), DELTA);
    }

    @Test
    public void canSetNegativeRealValue() {
        Complex number = new Complex(0, 2);
        assertEquals(0.0, number.getReal(), DELTA);
    }

    @Test
    public void canSetInitialImaginaryValue() {
        Complex number = new Complex(1, 2);
        assertEquals(2.0, number.getImaginary(), DELTA);
    }

    @Test
    public void canSetZeroImaginaryValue() {
        Complex number = new Complex(1, 0);
        assertEquals(0.0, number.getImaginary(), DELTA);
    }

    @Test
    public void canSetNegativeImaginaryValue() {
        Complex number = new Complex(1, -5);
        assertEquals(-5.0, number.getImaginary(), DELTA);
    }

    @Test
    public void canConvert() {
        Complex z = new Complex(1, 2);
        assertEquals("1 + 2i", z.toString());
    }

    @Test
    public void canConvertFloat() {
        Complex z = new Complex(3.14, 2);
        assertEquals("3.14 + 2i", z.toString());
    }

    @Test
    public void canConvertLongNumber() {
        Complex z = new Complex(1, 1.2456e-2);
        assertEquals("1 + 0.01246i", z.toString());
    }

    @Test
    public void canConvertNegativeReal() {
        Complex z = new Complex(-1, 1);
        assertEquals("-1 + 1i", z.toString());
    }

    @Test
    public void canConvertNegativeImaginary() {
        Complex z = new Complex(1, -1);
        assertEquals("1 - 1i", z.toString());
    }

    private static final double DELTA = 1e-6;
}

