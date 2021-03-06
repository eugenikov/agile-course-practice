package ru.unn.agile.ConverterTemperature.viewmodel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ViewModelTests {
    @Before
    public void setUp() {
        viewModel = new ViewModel(new FakeLogger());
    }

    @After
    public void tearDown() {
        viewModel = null;
    }

    @Test
    public void canSetDefaultInputTemperatureValues() {
        assertEquals("", viewModel.inputTemperatureProperty().get());
    }

    @Test
    public void canSetDefaultValues() {
        assertEquals("", viewModel.inputTemperatureProperty().get());
        assertEquals(NameSystem.CELSIUS, viewModel.inputTypeProperty().get());
        assertEquals(NameSystem.FAHRENHEIT, viewModel.outputTypeProperty().get());
        assertEquals("", viewModel.getResult());
        assertEquals(Status.WAITING.toString(), viewModel.getStatus());
    }

    @Test
    public void canUseConstructorWithoutArguments() {
        viewModel = new ViewModel();
        assertEquals("", viewModel.inputTemperatureProperty().get());
    }

    @Test
    public void canConvertDefaultWhereInputSetZero() {
        viewModel.inputTemperatureProperty().set("0.0");

        viewModel.convert();

        assertEquals("32.00", viewModel.getResult());
    }

    @Test
    public void canConvertReturnCorrectValueWhenSystemIsEqual() {
        viewModel.inputTemperatureProperty().set("0.0");
        viewModel.inputTypeProperty().set(NameSystem.CELSIUS);
        viewModel.outputTypeProperty().set(NameSystem.CELSIUS);

        viewModel.convert();

        assertEquals("0.0", viewModel.getResult());
        assertEquals(Status.SUCCESS.toString(), viewModel.getStatus());
    }

    @Test
    public void statusIsWaitingWhenInputIsEmpty() {
        viewModel.convert();
        assertEquals(Status.WAITING.toString(), viewModel.getStatus());
    }

    @Test
    public void statusIsReadyWhenInputIsFill() {
        viewModel.inputTemperatureProperty().set("1.0");
        assertEquals(Status.READY.toString(), viewModel.getStatus());
    }

    @Test
    public void canReportBadFormat() {
        viewModel.inputTemperatureProperty().set("a");
        assertEquals(Status.BAD_FORMAT.toString(), viewModel.getStatus());
    }

    @Test
    public void canSetInputConversionOperation() {
        viewModel.inputTypeProperty().set(NameSystem.FAHRENHEIT);
        assertEquals(NameSystem.FAHRENHEIT, viewModel.inputTypeProperty().get());
    }

    @Test
    public void canSetInputAndOutputConversionsOperation() {
        viewModel.inputTypeProperty().set(NameSystem.FAHRENHEIT);
        viewModel.outputTypeProperty().set(NameSystem.CELSIUS);

        assertEquals(NameSystem.FAHRENHEIT, viewModel.inputTypeProperty().get());
        assertEquals(NameSystem.CELSIUS, viewModel.outputTypeProperty().get());
    }

    @Test
    public void canSetTrashInput() {
        viewModel.inputTemperatureProperty().set("trash");
        assertEquals(Status.BAD_FORMAT.toString(), viewModel.getStatus());
    }

    @Test
    public void statusIsUnphysicalWhneInputLessThanAbsoluteZero() {
        viewModel.inputTemperatureProperty().set("-1000.0");

        viewModel.convert();

        assertEquals(Status.IMPOSSIBLE.toString(), viewModel.getStatus());
    }

    @Test
    public void calculateButtonIsDisabledWhenInputIsEmpty() {
        viewModel.inputTemperatureProperty().set("");
        assertTrue(viewModel.isCalculationDisabled());
    }

    @Test
    public void calculateButtonIsEnabledWhenInputIsSet() {
        viewModel.inputTemperatureProperty().set("1");
        assertFalse(viewModel.isCalculationDisabled());
    }

    @Test
    public void canSetSuccessMessage() {
        viewModel.inputTemperatureProperty().set("1");

        viewModel.convert();

        assertEquals(Status.SUCCESS.toString(), viewModel.getStatus());
    }

    @Test
    public void convertMinus103FahrenheitsFrom198Point15KelvinsHasCorrectResult() {
        viewModel.inputTypeProperty().set(NameSystem.KELVIN);
        viewModel.outputTypeProperty().set(NameSystem.FAHRENHEIT);
        viewModel.inputTemperatureProperty().set("198.15");

        viewModel.convert();

        assertEquals("-103.00", viewModel.getResult());
        assertEquals(Status.SUCCESS.toString(), viewModel.getStatus());
    }

    @Test
    public void convertCelsiusToKelvinHasCorrectResult() {
        viewModel.inputTypeProperty().set(NameSystem.CELSIUS);
        viewModel.outputTypeProperty().set(NameSystem.KELVIN);
        viewModel.inputTemperatureProperty().set("0");

        viewModel.convert();

        assertEquals("273.15", viewModel.getResult());
        assertEquals(Status.SUCCESS.toString(), viewModel.getStatus());
    }

    @Test
    public void convertNewtonToFahrenheitHasCorrectResult() {
        viewModel.inputTypeProperty().set(NameSystem.NEWTON);
        viewModel.outputTypeProperty().set(NameSystem.FAHRENHEIT);
        viewModel.inputTemperatureProperty().set("0");

        viewModel.convert();

        assertEquals("32.00", viewModel.getResult());
        assertEquals(Status.SUCCESS.toString(), viewModel.getStatus());
    }

    @Test
    public void canConvertCorrectlyRoundsValueWhenTypeIsDifferent() {
        viewModel.inputTypeProperty().set(NameSystem.NEWTON);
        viewModel.outputTypeProperty().set(NameSystem.FAHRENHEIT);
        viewModel.inputTemperatureProperty().set("100.00");

        viewModel.convert();

        assertEquals("577.45", viewModel.getResult());
        assertEquals(Status.SUCCESS.toString(), viewModel.getStatus());
    }

    @Test
    public void canConvertCorrectlyRetranslateValueWhenTypeIsSame() {
        viewModel.inputTypeProperty().set(NameSystem.CELSIUS);
        viewModel.outputTypeProperty().set(NameSystem.CELSIUS);
        viewModel.inputTemperatureProperty().set("100.0");

        viewModel.convert();

        assertEquals("100.0", viewModel.getResult());
        assertEquals(Status.SUCCESS.toString(), viewModel.getStatus());
    }

    @Test
    public void logIsEmptyInTheBeginning() {
        List<String> log = viewModel.getLog();

        assertTrue(log.isEmpty());
    }

    @Test
    public void logContainsProperMessageAfterConvertion() {
        viewModel.inputTemperatureProperty().set("0");

        viewModel.convert();
        String message = viewModel.getLog().get(0);

        assertTrue(message.matches(".*" + LogMessages.CONVERT_WAS_PRESSED + ".*"));
    }

    @Test
    public void logContainsInputTemperatureAndResultAfterConvertion() {
        viewModel.inputTemperatureProperty().set("100");

        viewModel.convert();

        String message = viewModel.getLog().get(0);
        assertTrue(message.matches(".*" + viewModel.getInputTemperature()
                + ".*" + viewModel.getResult() + ".*"));
    }

    @Test
    public void logContainsNamesOfFromAndToSystemsAfterConvertion() {
        viewModel.inputTypeProperty().set(NameSystem.NEWTON);
        viewModel.outputTypeProperty().set(NameSystem.FAHRENHEIT);
        viewModel.inputTemperatureProperty().set("100");

        viewModel.convert();

        String message = viewModel.getLog().get(0);
        assertTrue(message.matches(".*" + NameSystem.NEWTON
                + ".*" + NameSystem.FAHRENHEIT + ".*"));
    }

    @Test
    public void logContainsProperMessageAfterInputTypeChange() {
        viewModel.onInputTypeChanged(NameSystem.CELSIUS, NameSystem.KELVIN);

        String message = viewModel.getLog().get(0);
        assertTrue(message.matches(".*" + LogMessages.INPUT_TYPE_WAS_CHANGED
                + NameSystem.KELVIN + ".*"));
    }

    @Test
    public void logDoesNotContainsMessagesAboutIdenticallyInputTypeChange() {
        viewModel.onInputTypeChanged(NameSystem.CELSIUS, NameSystem.KELVIN);
        viewModel.onInputTypeChanged(NameSystem.KELVIN, NameSystem.KELVIN);

        assertEquals(1, viewModel.getLog().size());
    }

    @Test
    public void logContainsProperMessageAfterOutputTypeChange() {
        viewModel.onOutputTypeChanged(NameSystem.FAHRENHEIT, NameSystem.NEWTON);

        String message = viewModel.getLog().get(0);
        assertTrue(message.matches(".*" + LogMessages.OUTPUT_TYPE_WAS_CHANGED
                + NameSystem.NEWTON + ".*"));
    }

    @Test
    public void logDoesNotContainMessagesAboutIdenticallyOutputTypeChange() {
        viewModel.onOutputTypeChanged(NameSystem.FAHRENHEIT, NameSystem.NEWTON);
        viewModel.onOutputTypeChanged(NameSystem.NEWTON, NameSystem.NEWTON);

        assertEquals(1, viewModel.getLog().size());
    }

    @Test
    public void logContainsProperMessageWhenUserRemovesFocusFromTextFieldAfterEditingIt() {
        viewModel.inputTemperatureProperty().set("100");
        viewModel.onFocusChanged(Boolean.TRUE, Boolean.FALSE);

        String message = viewModel.getLog().get(0);
        assertTrue(message.matches(".*" + LogMessages.EDITING_FINISHED + ".*"
                + viewModel.getInputTemperature() + ".*"));
    }

    @Test
    public void logDoesNotContainInfoAboutSameParametersTwiceWithPartialInput() {
        viewModel.inputTemperatureProperty().set("100");
        viewModel.onFocusChanged(Boolean.TRUE, Boolean.FALSE);
        viewModel.inputTemperatureProperty().set("100");
        viewModel.onFocusChanged(Boolean.TRUE, Boolean.FALSE);

        assertEquals(1, viewModel.getLog().size());
    }

    @Test
    public void logContainsTwoMessagesWhenUserEntersInputTemperatureAndClicksOnConvertButton() {
        viewModel.inputTemperatureProperty().set("100");
        viewModel.onFocusChanged(Boolean.TRUE, Boolean.FALSE);
        viewModel.convert();

        assertEquals(2, viewModel.getLog().size());
    }

    @Test
    public void logContainsSixMessagesWhenUserConvertsSomeTemperatureFromCelsiusToRestTypes() {
        viewModel.inputTemperatureProperty().set("100");
        viewModel.onFocusChanged(Boolean.TRUE, Boolean.FALSE);
        viewModel.convert();
        viewModel.onFocusChanged(Boolean.TRUE, Boolean.FALSE);
        viewModel.onOutputTypeChanged(NameSystem.FAHRENHEIT, NameSystem.NEWTON);
        viewModel.onFocusChanged(Boolean.TRUE, Boolean.FALSE);
        viewModel.convert();
        viewModel.onFocusChanged(Boolean.TRUE, Boolean.FALSE);
        viewModel.onOutputTypeChanged(NameSystem.NEWTON, NameSystem.KELVIN);
        viewModel.onFocusChanged(Boolean.TRUE, Boolean.FALSE);
        viewModel.convert();

        assertEquals(6, viewModel.getLog().size());
    }

    @Test
    public void calculateIsNotCalledWhenButtonIsDisabled() {
        viewModel.inputTemperatureProperty().set("very cold");

        viewModel.convert();

        assertTrue(viewModel.getLog().isEmpty());
    }

    @Test
    public void canPutSeveralLogMessages() {
        viewModel.inputTemperatureProperty().set("100");

        viewModel.convert();
        viewModel.convert();
        viewModel.convert();

        assertEquals(3, viewModel.getLog().size());
    }

    @Test
    public void canGetDefaultProperties() {
        assertEquals("", viewModel.resultProperty().get());
        assertEquals(Status.WAITING.toString(), viewModel.statusProperty().get());
        assertEquals(4, viewModel.inputTypesProperty().get().size());
        assertEquals(4, viewModel.outputTypesProperty().get().size());
        assertTrue(viewModel.calculationDisabledProperty().get());
        assertEquals("", viewModel.logsProperty().get());
    }

    public void setExternalViewModel(final ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    private ViewModel viewModel;
}
