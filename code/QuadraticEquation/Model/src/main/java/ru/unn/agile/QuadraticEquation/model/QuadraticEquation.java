package ru.unn.agile.QuadraticEquation.model;

import java.util.ArrayList;
import java.util.List;

public class QuadraticEquation {
    public static final double BORDER_TOP = 10e12;

    public static final double BORDER_BOTTOM = -10e12;

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public void setA(final double a) {
        this.a = a;
    }

    public void setB(final double b) {
        this.b = b;
    }

    public void setC(final double c) {
        this.c = c;
    }

    public void setABC(final double a, final double b, final double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public QuadraticEquation(final double a, final double b, final double c) {
        if (isNumberZero(Math.abs(a))) {
            throw new IllegalArgumentException("QuadraticEquation is degenerated.");
        } else {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }

    public QuadraticEquation(final String a, final String b, final String c) {
        double newA = Double.parseDouble(a);
        double newB = Double.parseDouble(b);
        double newC = Double.parseDouble(c);
        if (isNumberZero(Math.abs(newA)) && isNumberZero(Math.abs(newB))) {
            throw new IllegalArgumentException("QuadraticEquation is degenerated.");
        } else {
            this.a = newA;
            this.b = newB;
            this.c = newC;
        }
    }

    public double getDiscriminant() {
        if (this.isLinear()) {
            throw new IllegalArgumentException("Discriminant is not exist.");
        } else {
            return b * b - DISCRIMINANT_MULTIPLIER * a * c;
        }
    }

    public int getNumberofRealRoots() {
        if (!this.isLinear()) {
            return numberOfRootsIfIsNotLinear();
        } else {
            return 1;
        }
    }

    public int getNumberofImaginaryRoots() {
        if (this.getNumberofRealRoots() > 0) {
            return 0;
        } else {
            return 2;
        }
    }

    public List<String> solveQuadraticEquation() {
        List<String> result = new ArrayList<String>();
        if (this.isLinear()) {
            double root = (-1) * this.getC() / this.getB();
            result.add(Formatter.doubleToString(root));
            return result;
        }
        if (isNumberZero(this.getDiscriminant())) {
            double root = (-1) * this.getB() / 2 / this.getA();
            result.add(Formatter.doubleToString(root));
            return result;
        }
        if (this.getDiscriminant() > -DELTA) {
            result.add(Formatter.doubleToString(this.realFirst()));
            result.add(Formatter.doubleToString(this.realSecond()));
            return result;
        }

        result.add(this.cmplxFirst().toString());
        result.add(this.cmplxSecond().toString());
        return result;
    }

    private boolean isNumberZero(final double value) {
        return (value > -DELTA) && (value < DELTA);
    }

    private boolean isLinear() {
        return isNumberZero(a);
    }

    private boolean hasZeroDiscriminant() {
        return isNumberZero(this.getDiscriminant());
    }

    private int numberOfRootsIfIsNotLinear() {
        if (this.hasZeroDiscriminant()) {
            return 1;
        } else if (this.getDiscriminant() > -DELTA) {
            return 2;
        } else {
            return 0;
        }
    }

    private Complex cmplxFirst() {
        return new Complex(-b / 2 / a,
                Math.sqrt(-this.getDiscriminant()) / 2);
    }

    private Complex cmplxSecond() {
        return new Complex(-b / 2 / a,
                -Math.sqrt(-this.getDiscriminant()) / 2);
    }

    private double realFirst() {
        return ((-b + Math.sqrt(this.getDiscriminant())) / 2 / a);
    }

    private double realSecond() {
        return ((-b - Math.sqrt(this.getDiscriminant())) / 2 / a);
    }

    private double a;
    private double b;
    private double c;
    private static final int DISCRIMINANT_MULTIPLIER = 4;
    private static final double DELTA = 1e-15;
}
