package ru.unn.agile.QuadraticEquation.ViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class ViewModelTests {
    private ViewModel viewModel;

    public void setViewModel(final ViewModel theViewModel) {
        this.viewModel = theViewModel;
    }

    @Before
    public void setViewModel() {
        if (viewModel == null) {
            viewModel = new ViewModel(new FakeLogger());
        }
    }

    @After
    public void tearDown() {
        viewModel = null;
    }

    @Test
    public void canCreateViewModelWithLogger() {
        FakeLogger logger = new FakeLogger();
        ViewModel viewModelWithLogger = new ViewModel(logger);
        assertNotNull(viewModelWithLogger);
    }

    @Test(expected = IllegalArgumentException.class)
    public void viewModelWithNullLogger() {
        new ViewModel(null);
    }

    @Test
    public void logContainMessageAfterSolve() {
        setInputData();
        viewModel.solve();
        String message = viewModel.getLog().get(0);
        assertTrue(message.matches(".*" + ViewModel.LogMessages.SOLVE_WAS_PRESSED +  ".*"));
    }

    @Test
    public void ifFocusIsNotChangedLogIsEmpty() {
        viewModel.aProperty().set("1");
        viewModel.onFocusChanged(Boolean.FALSE, Boolean.TRUE);
        String message = viewModel.getLogs();
        assertTrue(message.isEmpty());
    }

    @Test
    public void logIsWroteAfterFirstInput() {
        viewModel.aProperty().set("1");

        viewModel.onFocusChanged(Boolean.TRUE, Boolean.FALSE);
        String message = viewModel.getLog().get(0);
        assertTrue(message.matches(".*" + ViewModel.LogMessages.EDITING_FINISHED
                + "Input arguments are: a = 1; b = ; c = ."));
    }

    @Test
    public void afterOneInputNumberOfLogsStringsIsOne() {
        viewModel.aProperty().set("1");

        viewModel.onFocusChanged(Boolean.TRUE, Boolean.FALSE);
        assertEquals(1, viewModel.getLog().size());
    }

    @Test
    public void ifPutTwiceACoefficient() {
        viewModel.aProperty().set("123");
        viewModel.onFocusChanged(Boolean.TRUE, Boolean.FALSE);

        viewModel.aProperty().set("123.12");
        viewModel.onFocusChanged(Boolean.TRUE, Boolean.FALSE);

        assertEquals(2, viewModel.getLog().size());
    }
    @Test

    public void ifPutTwiceTheSameACoefficient() {
        viewModel.aProperty().set("123");
        viewModel.onFocusChanged(Boolean.TRUE, Boolean.FALSE);

        viewModel.aProperty().set("123");
        viewModel.onFocusChanged(Boolean.TRUE, Boolean.FALSE);

        assertEquals(1, viewModel.getLog().size());
    }

    @Test
    public void canWriteLogAfterSolving() {
        setInputData();

        viewModel.onFocusChanged(Boolean.TRUE, Boolean.FALSE);
        String message = viewModel.getLog().get(0);
        assertTrue(message.matches(".*" + ViewModel.LogMessages.EDITING_FINISHED
                + "Input arguments are: a = 1; "
                + "b = 2; c = 3."));
    }

    @Test
    public void canSetDefaultValues() {
        assertEquals("", viewModel.aProperty().get());
        assertEquals("", viewModel.bProperty().get());
        assertEquals("", viewModel.cProperty().get());
        assertEquals("", viewModel.getLogs());
        assertEquals(Status.WAITING.getStatusName(), viewModel.statusProperty().get());
    }

    @Test
    public void statusIsWaitingWhenSolutionWithEmptyFields() {
        viewModel.solve();
        assertEquals(Status.WAITING.getStatusName(), viewModel.statusProperty().get());
    }

    @Test
    public void statusIsReadyWhenFieldsAreFill() {
        setInputData();
        assertEquals(Status.READY.getStatusName(), viewModel.statusProperty().get());
    }

    @Test
    public void canReportBadFormat() {
        viewModel.aProperty().set("a");
        assertEquals(Status.BAD_FORMAT.getStatusName(), viewModel.statusProperty().get());
    }

    @Test
    public void statusIsWaitingIfNotEnoughCorrectData() {
        viewModel.aProperty().set("");
        viewModel.bProperty().set("");
        assertEquals(Status.WAITING.getStatusName(), viewModel.statusProperty().get());
    }

    @Test
    public void statusIsWaitingEvenIfThirdCoefIsHere() {
        viewModel.aProperty().set("");
        viewModel.bProperty().set("");
        viewModel.cProperty().set("12345");
        assertEquals(Status.BAD_FORMAT.getStatusName(), viewModel.statusProperty().get());
    }

    @Test
    public void statusIsBadIfTwoFirstCoefsAreZero() {
        viewModel.aProperty().set("0");
        viewModel.bProperty().set("0");
        assertEquals(Status.BAD_FORMAT.getStatusName(), viewModel.statusProperty().get());
    }

    @Test
    public void solveIsDisabledIfTwoFirstCoefsAreZero() {
        viewModel.aProperty().set("0");
        viewModel.bProperty().set("0");
        assertTrue(viewModel.solutionDisabledProperty().get());
    }

    @Test
    public void statusIsOkIfEvenFirstCoefIsZeroAndSecondIsNot() {
        viewModel.aProperty().set("0");
        viewModel.bProperty().set("1");
        assertEquals(Status.READY.getStatusName(), viewModel.statusProperty().get());
    }

    @Test
    public void solveIsEnabledIfEvenFirstCoefIsZeroAndSecondIsNot() {
        viewModel.aProperty().set("0");
        viewModel.bProperty().set("1");
        assertFalse(viewModel.solutionDisabledProperty().get());
    }

    @Test
    public void statusIsOkIfEvenFirstCoefIsEmptyAndSecondIsNot() {
        viewModel.aProperty().set("");
        viewModel.bProperty().set("1");
        assertEquals(Status.READY.getStatusName(), viewModel.statusProperty().get());
    }

    @Test
    public void solveIsEnabledIfEvenFirstCoefIsEmptyAndSecondIsNot() {
        viewModel.aProperty().set("");
        viewModel.bProperty().set("1");
        assertFalse(viewModel.solutionDisabledProperty().get());
    }

    @Test
    public void statusIsOkIfEvenSecondCoefIsZeroAndFirstIsNot() {
        viewModel.aProperty().set("123");
        viewModel.bProperty().set("0");
        assertEquals(Status.READY.getStatusName(), viewModel.statusProperty().get());
    }

    @Test
    public void solveIsEnabledIfEvenSecondCoefIsZeroAndFirstIsNot() {
        viewModel.aProperty().set("123");
        viewModel.bProperty().set("0");
        assertFalse(viewModel.solutionDisabledProperty().get());
    }

    @Test
    public void statusIsOkIfEvenSecondCoefIsEmptyAndFirstIsNot() {
        viewModel.aProperty().set("123");
        viewModel.bProperty().set("");
        assertEquals(Status.READY.getStatusName(), viewModel.statusProperty().get());
    }

    @Test
    public void solveIsEnabledIfEvenSecondCoefIsEmptyAndFirstIsNot() {
        viewModel.aProperty().set("123");
        viewModel.bProperty().set("");
        assertFalse(viewModel.solutionDisabledProperty().get());
    }

    @Test
    public void solveButtonIsDisabledInitially() {
        assertTrue(viewModel.solutionDisabledProperty().get());
    }

    @Test
    public void solveButtonIsDisabledWhenFormatIsBad() {
        setInputData();
        viewModel.aProperty().set("someLetters");
        assertTrue(viewModel.solutionDisabledProperty().get());
    }

    @Test
    public void solveButtonIsDisabledWithIncompleteInput() {
        viewModel.cProperty().set("1");
        assertTrue(viewModel.solutionDisabledProperty().get());
    }

    @Test
    public void calculateButtonIsEnabledWithCorrectInput() {
        setInputData();
        assertFalse(viewModel.solutionDisabledProperty().get());
    }

    @Test
    public void solutionHasCorrectResultWithOneRoot() {
        viewModel.aProperty().set("1");
        viewModel.bProperty().set("2");
        viewModel.cProperty().set("1");
        viewModel.solve();
        assertTrue(viewModel.getSolution().contains("-1"));
    }

    @Test
    public void solutionHasCorrectResultWithOnlyFirstCoef() {
        viewModel.aProperty().set("1");
        viewModel.bProperty().set("");
        viewModel.cProperty().set("");
        viewModel.solve();
        assertTrue(viewModel.getSolution().contains("0"));
    }

    @Test
    public void firstEmptyCellWillBeZeroIfYouInputNothingToIt() {
        viewModel.aProperty().set("");
        viewModel.bProperty().set("1");
        viewModel.cProperty().set("1");
        viewModel.solve();
        assertEquals("0", viewModel.aProperty().get());
    }

    @Test
    public void secondtEmptyCellWillBeZeroIfYouInputNothingToIt() {
        viewModel.aProperty().set("1");
        viewModel.bProperty().set("");
        viewModel.cProperty().set("1");
        viewModel.solve();
        assertEquals("0", viewModel.bProperty().get());
    }

    @Test
    public void thirdEmptyCellWillBeZeroIfYouInputNothingToIt() {
        viewModel.aProperty().set("1");
        viewModel.bProperty().set("1");
        viewModel.cProperty().set("");
        viewModel.solve();
        assertEquals("0", viewModel.cProperty().get());
    }

    @Test
    public void firstAndThirdEmptyCellWillBeZeroIfYouInputNothingToIt() {
        viewModel.aProperty().set("");
        viewModel.bProperty().set("1");
        viewModel.cProperty().set("");
        viewModel.solve();
        assertEquals("0", viewModel.aProperty().get());
        assertEquals("0", viewModel.cProperty().get());
    }

    @Test
    public void secondAndThirdEmptyCellWillBeZeroIfYouInputNothingToIt() {
        viewModel.aProperty().set("1");
        viewModel.bProperty().set("");
        viewModel.cProperty().set("");
        viewModel.solve();
        assertEquals("0", viewModel.bProperty().get());
        assertEquals("0", viewModel.cProperty().get());
    }

    @Test
    public void solutionHasCorrectResultWithOnlySecondCoef() {
        viewModel.aProperty().set("");
        viewModel.bProperty().set("5");
        viewModel.cProperty().set("");
        viewModel.solve();
        assertTrue(viewModel.getSolution().contains("0"));
    }

    @Test
    public void solutionHasCorrectResultWithSecondAndThirdCoef() {
        viewModel.aProperty().set("");
        viewModel.bProperty().set("5");
        viewModel.cProperty().set("-10");
        viewModel.solve();
        assertTrue(viewModel.getSolution().contains("2"));
    }

    @Test
    public void solutionHasCorrectResultWithSmallCoeffs() {
        viewModel.aProperty().set("1");
        viewModel.bProperty().set("-1.00005");
        viewModel.cProperty().set("0.00005");
        viewModel.solve();
        assertTrue(viewModel.getSolution().contains("1"));
        assertTrue(viewModel.getSolution().contains("0.00005"));
    }

    @Test
    public void solutionHasCorrectResultWithTwoInputCoefs() {
        viewModel.aProperty().set("1");
        viewModel.bProperty().set("-1");
        viewModel.cProperty().set("");
        viewModel.solve();
        assertTrue(viewModel.getSolution().contains("0"));
        assertTrue(viewModel.getSolution().contains("0"));
    }

    @Test
    public void solutionHasCorrectResultWithTwoIntRoots() {
        viewModel.aProperty().set("1");
        viewModel.bProperty().set("5");
        viewModel.cProperty().set("6");
        viewModel.solve();
        assertTrue(viewModel.getSolution().contains("-2"));
        assertTrue(viewModel.getSolution().contains("-3"));
    }

    @Test
    public void solutionHasCorrectResultWithTwoComplexRoots() {
        viewModel.aProperty().set("1");
        viewModel.bProperty().set("6");
        viewModel.cProperty().set("10");
        viewModel.solve();
        assertTrue(viewModel.getSolution().contains("-3 - 1i"));
        assertTrue(viewModel.getSolution().contains("-3 + 1i"));
    }

    @Test
    public void canSetSuccessMessage() {
        setInputData();
        viewModel.solve();
        assertEquals(Status.SUCCESS.getStatusName(), viewModel.statusProperty().get());
    }

    @Test
    public void canSetSuccessMessageForLinearEquation() {
        viewModel.bProperty().set("2");
        viewModel.cProperty().set("3");
        viewModel.solve();
        assertEquals(Status.SUCCESS.getStatusName(), viewModel.statusProperty().get());
    }

    @Test
    public void canSetSuccessMessageWithoutSecondCoef() {
        viewModel.aProperty().set("1");
        viewModel.cProperty().set("3");
        viewModel.solve();
        assertEquals(Status.SUCCESS.getStatusName(), viewModel.statusProperty().get());
    }

    @Test
    public void canSetSuccessMessageOnlyWithFirstCoef() {
        viewModel.aProperty().set("1");
        viewModel.solve();
        assertEquals(Status.SUCCESS.getStatusName(), viewModel.statusProperty().get());
    }

    @Test
    public void canSetSuccessMessageOnlyWithSecondCoef() {
        viewModel.bProperty().set("1");
        viewModel.solve();
        assertEquals(Status.SUCCESS.getStatusName(), viewModel.statusProperty().get());
    }

    @Test
    public void canSetSuccessMessageWithoutThirdCoef() {
        viewModel.aProperty().set("1");
        viewModel.bProperty().set("3");
        viewModel.solve();
        assertEquals(Status.SUCCESS.getStatusName(), viewModel.statusProperty().get());
    }

    @Test
    public void canSetBadFormatMessage() {
        viewModel.aProperty().set("#=-badMess");
        assertEquals(Status.BAD_FORMAT.getStatusName(), viewModel.statusProperty().get());
    }

    private void setInputData() {
        viewModel.aProperty().set("1");
        viewModel.bProperty().set("2");
        viewModel.cProperty().set("3");
    }
}



