package ru.unn.agile.QuadraticEquation.Infrastructure;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import static junit.framework.TestCase.assertNotNull;

public class TxtLoggerTests {

    @Before
    public void setUp() {
        logger = new TxtLogger(NAME_OF_FILE);
    }

    @Test
    public void canCreateLogFile() {
        try {
            new BufferedReader(new FileReader(NAME_OF_FILE));
        } catch (FileNotFoundException e) {
            fail("file " + NAME_OF_FILE + " was not found");
        }
    }

    @Test
    public void canWriteLogMessage() {
        String testMessage = "the message";

        logger.log(testMessage);

        String message = logger.getLog().get(0);
        assertTrue(message.matches(".*" + testMessage));
    }

    @Test
    public void canCreateLoggerWithoutNameOfFile() {
        assertNotNull(logger);
    }

    @Test
    public void canWriteLogMessages() {
        String[] messages = {"message 1", "message 2"};

        logger.log("message 1");
        logger.log("message 2");

        List<String> messagesFromLog = logger.getLog();
        for (int i = 0; i < messagesFromLog.size(); i++) {
            assertTrue(messagesFromLog.get(i).matches(".*" + messages[i]));
        }
    }

    @Test
    public void doesLogContainDateAndTime() {
        String message = "the message";

        logger.log(message);

        String logMessage = logger.getLog().get(0);
        assertTrue(logMessage.matches(PATTERN));
    }

    private static final String PATTERN = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} > .*";
    private static final String NAME_OF_FILE = "./TxtLoggerTests-lab3.log";
    private TxtLogger logger;
}
