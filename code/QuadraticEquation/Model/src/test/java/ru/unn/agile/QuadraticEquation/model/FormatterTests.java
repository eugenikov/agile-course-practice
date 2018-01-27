package ru.unn.agile.QuadraticEquation.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FormatterTests {

    @Test(expected = IllegalArgumentException.class)
    public void throwsOnNegativeDouble() {
        double x = -1.0;
        Formatter.formatPositiveDouble(x);
    }

    @Test
    public void canFormat() {
        double x = 1.543;
        assertEquals("1.543", Formatter.formatPositiveDouble(x));
    }

    @Test
    public void canConvertToString() {
        Complex z = new Complex(1, 2);
        assertEquals("1 + 2i", z.toString());
    }

    @Test
    public void canConvertFloatingComplexNumberToString() {
        Complex z = new Complex(3.14, 2);
        assertEquals("3.14 + 2i", z.toString());
    }

    @Test
    public void canConvertScientificFormatToString() {
        Complex z = new Complex(1, 1.2456e-2);
        assertEquals("1 + 0.01246i", z.toString());
    }

    @Test
    public void canConvertNegativeRealPartToString() {
        Complex z = new Complex(-1, 1);
        assertEquals("-1 + 1i", z.toString());
    }

    @Test
    public void canConvertNegativeImaginaryPartToString() {
        Complex z = new Complex(1, -1);
        assertEquals("1 - 1i", z.toString());
    }
}

