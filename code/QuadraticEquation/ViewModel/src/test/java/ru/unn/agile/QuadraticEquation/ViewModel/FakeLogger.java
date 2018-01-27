package ru.unn.agile.QuadraticEquation.ViewModel;


import java.util.ArrayList;
import java.util.List;

public class FakeLogger implements ILogger {
    @Override
    public void log(final String message) {
        logger.add(message);
    }

    @Override
    public List<String> getLog() {
        return logger;
    }

    private ArrayList<String> logger = new ArrayList<String>();
}
