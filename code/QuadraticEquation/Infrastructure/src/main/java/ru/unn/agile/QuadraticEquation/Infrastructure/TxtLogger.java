package ru.unn.agile.QuadraticEquation.Infrastructure;

import ru.unn.agile.QuadraticEquation.ViewModel.ILogger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TxtLogger implements ILogger {

    public TxtLogger(final String filename) {
        this.nameOfFile = filename;

        BufferedWriter logWriter = null;
        try {
            logWriter = new BufferedWriter(new FileWriter(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
        fileWriter = logWriter;
    }

    @Override
    public void log(final String s) {
        try {
            fileWriter.write(now() + " > " + s);
            fileWriter.newLine();
            fileWriter.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<String> getLog() {
        BufferedReader fileReader;
        ArrayList<String> log = new ArrayList<String>();
        try {
            fileReader = new BufferedReader(new FileReader(nameOfFile));
            String oneLine = fileReader.readLine();

            while (oneLine != null) {
                log.add(oneLine);
                oneLine = fileReader.readLine();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return log;
    }

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final BufferedWriter fileWriter;
    private final String nameOfFile;

    private static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        return sdf.format(cal.getTime());
    }
}
