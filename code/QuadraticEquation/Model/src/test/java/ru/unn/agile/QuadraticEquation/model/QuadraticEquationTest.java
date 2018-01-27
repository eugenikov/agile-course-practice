package ru.unn.agile.QuadraticEquation.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class QuadraticEquationTest {

    @Before
    public void createEquation() {
        equation = new QuadraticEquation(1, 1, 1);
    }

    @After
    public void deleteEquation() {
        equation = null;
    }

    @Test
    public void canCreateQuadraticEquation() {
        equation.setABC(1, 2, 3);
        assertNotNull(equation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void willThrowExceptionIfDegenerateEquation() {
        new QuadraticEquation(0, 0, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void willThrowExceptionIfDegenerateEquationWithZeroABCoeffs() {
        new QuadraticEquation("0", "0", "2");
    }

    @Test
    public void canGetSimpleDiscriminant() {
        equation.setABC(1, 0, 0);
        double discriminant = equation.getDiscriminant();
        assertEquals(0, discriminant, 1e-15);
    }

    @Test
    public void canGetPositiveDiscriminant() {
        equation.setABC(1, 0, -1);
        double discriminant = equation.getDiscriminant();
        assertEquals(4, discriminant, 1e-15);
    }

    @Test
    public void canGetNegativeDiscriminant() {
        equation.setABC(1, 2, 3);
        double discriminant = equation.getDiscriminant();
        assertEquals(-8, discriminant, 1e-15);
    }

    @Test(expected = IllegalArgumentException.class)
    public void willThrowExceptionIfDiscriminantNotExist() {
        equation.setABC(0, 1, 0);
        double discriminant = equation.getDiscriminant();
    }

    @Test
    public void canGetNumberofRealRootsSimple() {
        equation.setABC(1, 0, 0);
        int number = equation.getNumberofRealRoots();
        assertEquals(1, number);
    }

    @Test
    public void canGetNumberofRealRootsTwo() {
        equation.setABC(1, 2, -1);
        int number = equation.getNumberofRealRoots();
        assertEquals(2, number);
    }

    @Test
    public void canGetNumberofRealRootsNull() {
        equation.setABC(1, 2, 2);
        int number = equation.getNumberofRealRoots();
        assertEquals(0, number);
    }

    @Test
    public void canGetNumberofRealRootsLinearEquation() {
        equation.setABC(0, 2, 2);
        int number = equation.getNumberofRealRoots();
        assertEquals(1, number);
    }

    @Test
    public void canGetNumberofImaginaryRootsSimple() {
        equation.setABC(1, 0, 0);
        int number = equation.getNumberofImaginaryRoots();
        assertEquals(0, number);
    }

    @Test
    public void canGetNumberofImaginaryRoots() {
        equation.setABC(1, 4, 5);
        int number = equation.getNumberofImaginaryRoots();
        assertEquals(2, number);
    }

    @Test
    public void canFindFirstRootOfSimpleQuadraticEquation() {
        List<String> result = new ArrayList<String>();
        equation.setABC(4, 0, 0);
        result = equation.solveQuadraticEquation();
        assertEquals("0", result.get(0));
    }

    @Test
    public void canFindSecondRootOfSimpleQuadraticEquation() {
        List<String> result = new ArrayList<String>();
        equation.setABC(4, 0, 0);
        result = equation.solveQuadraticEquation();
        assertTrue(result.contains("0"));
    }

    @Test
    public void canFindSolutionOfSimpleQuadraticEquationWithZeroDiscr() {
        equation.setABC(1, 2, 1);
        List<String> result = new ArrayList<String>();
        result = equation.solveQuadraticEquation();
        assertTrue(result.contains("-1"));
    }

    @Test
    public void canFindFirstRootOfQuadraticEquationWithZeroComplexPart() {
        List<String> result = new ArrayList<String>();
        equation.setABC(1, 4, 4);
        result = equation.solveQuadraticEquation();
        assertEquals("-2", result.get(0));
    }

    @Test
    public void canFindFirstRootOfLinearEquation() {
        List<String> result = new ArrayList<String>();
        equation.setABC(0, 6, -2);
        result = equation.solveQuadraticEquation();
        String root = Formatter.doubleToString(1.0 / 3);
        assertTrue(result.contains(root));
    }

    @Test
    public void canFindFirstRootOfQuadraticEquationWithTwoRealRoots() {
        List<String> result = new ArrayList<String>();
        equation.setABC(-2, 3, 1);
        result = equation.solveQuadraticEquation();
        String root = Formatter.doubleToString(0.75 - Math.sqrt(17) / 4);
        assertTrue(result.contains(root));
    }

    @Test
    public void canFindSecondRootOfQuadraticEquationWithTwoRealRoots() {
        List<String> result = new ArrayList<String>();
        equation.setABC(-2, 3, 1);
        result = equation.solveQuadraticEquation();
        String root = Formatter.doubleToString(0.75 + Math.sqrt(17) / 4);
        assertTrue(result.contains(root));
    }

    @Test
    public void canFindFirstRootOfQuadraticEquationWithDoubleCoefficients() {
        List<String> result = new ArrayList<String>();
        equation.setABC(-0.4, 0.3, 0.2);
        result = equation.solveQuadraticEquation();
        String root = Formatter.doubleToString((-0.3 + Math.sqrt(0.41)) / (-0.8));
        assertTrue(result.contains(root));
    }

    @Test
    public void canFindSecondRootOfQuadraticEquationWithDoubleCoefficients() {
        List<String> result = new ArrayList<String>();
        equation.setABC(-0.4, 0.3, 0.2);
        result = equation.solveQuadraticEquation();
        String root = Formatter.doubleToString((-0.3 - Math.sqrt(0.41)) / (-0.8));
        assertTrue(result.contains(root));
    }

    @Test
    public void canFindRootsOfQuadraticEquationWithNoImaginaryParts() {
        List<String> result = new ArrayList<String>();
        equation.setABC(-1, -4, 3.3);
        result = equation.solveQuadraticEquation();
        assertTrue(result.contains("0.70186"));
        assertTrue(result.contains("-4.70186"));
    }

    @Test
    public void ifJustFirstCoefIsNotZeroNumberOfRootsIsOne() {
        List<String> result = new ArrayList<String>();
        equation.setABC(4., 0., 0.);
        result = equation.solveQuadraticEquation();
        assertEquals(1, result.size());
    }

    @Test
    public void ifJustSecondCoefIsNotZeroNumberOfRootsIsOne() {
        List<String> result = new ArrayList<String>();
        equation.setABC(0., 1., 0.);
        result = equation.solveQuadraticEquation();
        assertEquals(1, result.size());
    }

    @Test
    public void numberOfRootsEqualsTwo() {
        List<String> result = new ArrayList<String>();
        equation.setABC(1., -1., 0.);
        result = equation.solveQuadraticEquation();
        assertEquals(2, result.size());
    }

    @Test
    public void numberOfRootsWithComplexEqualsTwo() {
        List<String> result = new ArrayList<String>();
        equation.setABC(1., 1., 25.);
        result = equation.solveQuadraticEquation();
        assertEquals(2, result.size());
    }

    private QuadraticEquation equation;
}

