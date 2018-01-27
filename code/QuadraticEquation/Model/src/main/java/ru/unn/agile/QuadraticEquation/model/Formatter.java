package ru.unn.agile.QuadraticEquation.model;

import java.text.DecimalFormat;
import java.math.RoundingMode;

public final class Formatter {

    public static String formatPositiveDouble(final double value) {
        if (value < 0) {
            throw new IllegalArgumentException("Use this function only for positive value!");
        }
        DecimalFormat df = new DecimalFormat(FORMAT);
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(value);
    }

    public static String getFormatted(final Complex z) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(z.getReal() < 0 ? "-" : "");
        String re = formatPositiveDouble(Math.abs(z.getReal()));
        String im = formatPositiveDouble(Math.abs(z.getImaginary()));
        buffer.append(re)
                .append(z.getImaginary() < 0 ? " - " : " + ")
                .append(im)
                .append("i");
        return buffer.toString();
    }

    public static String doubleToString(final double n) {
        StringBuffer buffer = new StringBuffer();
        if (n > -DELTA && n < DELTA) {
            buffer.append("0");
            return buffer.toString();
        }
        String formattedValue;
        buffer.append(n < -DELTA ? "-" : "");
        if (n < 0) {
            formattedValue = formatPositiveDouble(-n);
        } else {
            formattedValue = formatPositiveDouble(n);
        }
        buffer.append(formattedValue);
        return buffer.toString();
    }

    private Formatter() {
    }

    private static final double DELTA = 1e-15;
    private static final String FORMAT = "#.#####";
}



