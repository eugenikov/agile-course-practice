package ru.unn.agile.QuadraticEquation.ViewModel;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.binding.BooleanBinding;

import java.util.List;
import java.util.ArrayList;

import ru.unn.agile.QuadraticEquation.model.QuadraticEquation;

public class ViewModel {

    public ViewModel() {
        init();
    }

    public final void setLogger(final ILogger logger) {
        if (logger == null) {
            throw new IllegalArgumentException("Logger is null.");
        }
        this.logger = logger;
    }

    public ViewModel(final ILogger logger) {
        setLogger(logger);
        init();
    }

    public void solve() {
        if (solutionDisabled.get()) {
            return;
        }
        if (a.get().equals("")) {
            a.set("0");
        }
        if (b.get().equals("")) {
            b.set("0");
        }
        if (c.get().equals("")) {
            c.set("0");
        }
        QuadraticEquation findSolution = new QuadraticEquation(a.get(), b.get(), c.get());
        List<String> res;

        res = findSolution.solveQuadraticEquation();
        if (res.size() == 1) {
            firstRoot.set(res.get(0));
            secondRoot.set(res.get(0));
            solution.add(firstRoot);
        }
        if (res.size() == 2) {
            firstRoot.set(res.get(0));
            secondRoot.set(res.get(1));
            solution.add(firstRoot);
            solution.add(secondRoot);
        }
        status.set(Status.SUCCESS.getStatusName());

        String message = calculateLogMessage();
        logger.log(message);
        updateLogs();
    }

    public final class LogMessages {
        public static final String SOLVE_WAS_PRESSED = "Solve. ";
        public static final String EDITING_FINISHED = "Updated input. ";

        private LogMessages() { }
    }

    public void onFocusChanged(final Boolean oldValue, final Boolean newValue) {
        if (!oldValue && newValue) {
            return;
        }
        for (ChangeStatus listener : inputChangedListeners) {
            if (listener.isChanged()) {
                String message = LogMessages.EDITING_FINISHED + "Input arguments are: a = "
                        + a.get() + "; b = " + b.get() + "; c = " + c.get() + ".";
                logger.log(message);
                listener.cache();
                break;
            }
        }
        updateLogs();
    }

    public List<String> getLog() {
        return logger.getLog();
    }

    public StringProperty logsProperty() {
        return logs;
    }

    public final String getLogs() {
        return logs.get();
    }

    public StringProperty aProperty() {
        return a;
    }

    public StringProperty bProperty() {
        return b;
    }

    public StringProperty cProperty() {
        return c;
    }

    public List<StringProperty> solutionProperty() {
        return solution;
    }

    public StringProperty firstRootProperty() {
        return firstRoot;
    }

    public StringProperty secondRootProperty() {
        return secondRoot;
    }

    public final String getFirstRoot() {
        return firstRoot.get();
    }

    public final String getSecondRoot() {
        return secondRoot.get();
    }

    public BooleanProperty solutionDisabledProperty() {
        return solutionDisabled;
    }

    public final boolean isSolutionDisabled() {
        return solutionDisabled.get();
    }

    public final List<String> getSolution() {
        List<String> res = new ArrayList<>();
        if (solution.size() == 1) {
            res.add(solution.get(0).getValue());
        }
        if (solution.size() == 2) {
            res.add(solution.get(0).getValue());
            res.add(solution.get(1).getValue());
        }
        return res;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public final String getStatus() {
        return status.get();
    }

    private void init() {
        logs.set("");
        a.set("");
        b.set("");
        c.set("");
        firstRoot.set("");
        secondRoot.set("");
        status.set(Status.WAITING.getStatusName());
        BooleanBinding canSolve = new BooleanBinding() {
            {
                super.bind(a, b, c);
            }

            @Override
            protected boolean computeValue() {
                return getInputStatus() == Status.READY;
            }
        };
        solutionDisabled.bind(canSolve.not());

        final List<StringProperty> fields = new ArrayList<StringProperty>() {
            {
                add(a);
                add(b);
                add(c);
            }
        };

        for (StringProperty field : fields) {
            final ChangeStatus listener = new ChangeStatus();
            field.addListener(listener);
            inputChangedListeners.add(listener);
        }
    }

    private void updateLogs() {
        List<String> log = logger.getLog();
        String record = new String("");
        for (String logItem : log) {
            record += logItem + "\n";
        }
        logs.set(record);
    }

    private String calculateLogMessage() {
        return LogMessages.SOLVE_WAS_PRESSED + "Root1 = " + getFirstRoot() + ", Root2 = "
                + getSecondRoot() + ". Arguments: " + allInputCoefficients();
    }

    private String allInputCoefficients() {
        return String.format("a = %s; b = %s; c = %s.", a.get(), b.get(), c.get());
    }

    private Status getInputStatus() {
        Status inputStatus = Status.READY;
        if (a.get().isEmpty() && b.get().isEmpty()) {
            inputStatus = Status.WAITING;
        }
        try {
            double localA = 0., localB = 0.;
            if (!a.get().isEmpty()) {
                localA = Double.parseDouble(a.get());
            }
            if (!b.get().isEmpty()) {
                localB = Double.parseDouble(b.get());
            }
            if (!c.get().isEmpty()) {
                Double.parseDouble(c.get());
            }
            inputStatus = this.conditionForBadFormat(localA, localB);
        } catch (NumberFormatException e) {
            inputStatus = Status.BAD_FORMAT;
        }
        return inputStatus;
    }

    private Status conditionForBadFormat(final double a, final double b) {
        if (Math.abs(a) < EPS && Math.abs(b) < EPS) {
            return Status.BAD_FORMAT;
        }
        double borderTop = QuadraticEquation.BORDER_TOP;
        double borderBottom = QuadraticEquation.BORDER_BOTTOM;
        if ((a < borderBottom) || (a > borderTop)) {
            return Status.BAD_FORMAT;
        }
        if ((b < borderBottom) || (b > borderTop)) {
            return Status.BAD_FORMAT;
        }
        return Status.READY;
    }

    private class ChangeStatus implements ChangeListener<String> {
        @Override
        public void changed(final ObservableValue<? extends String> observable,
                            final String oldInput, final String newInput) {
            if (oldInput.equals(newInput)) {
                return;
            }
            status.set(getInputStatus().getStatusName());
            currentInput = newInput;
        }

        public boolean isChanged() {
            return !previousInput.equals(currentInput);
        }
        public void cache() {
            previousInput = currentInput;
        }

        private String previousInput = new String("");
        private String currentInput = new String("");
    }

    private final StringProperty logs = new SimpleStringProperty();
    private ILogger logger;
    private final StringProperty a = new SimpleStringProperty(),
            b = new SimpleStringProperty(),
            c = new SimpleStringProperty();
    private final List<StringProperty> solution = new ArrayList<>();

    private final StringProperty firstRoot = new SimpleStringProperty();
    private final StringProperty secondRoot = new SimpleStringProperty();

    private final BooleanProperty solutionDisabled = new SimpleBooleanProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final List<ChangeStatus> inputChangedListeners = new ArrayList<>();
    private static final double EPS = 1e-6;
}

enum Status {
    WAITING("Please input data"),
    READY("Press 'Solve' or Enter"),
    BAD_FORMAT("Bad format"),
    SUCCESS("Success");

    Status(final String name) {
        this.name = name;
    }

    public String getStatusName() {
        return name;
    }

    private final String name;
}


