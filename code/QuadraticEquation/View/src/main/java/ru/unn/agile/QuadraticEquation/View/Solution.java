package ru.unn.agile.QuadraticEquation.View;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;

import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;
import java.util.function.UnaryOperator;
import java.util.Calendar;
import java.util.Locale;

import ru.unn.agile.QuadraticEquation.Infrastructure.TxtLogger;
import ru.unn.agile.QuadraticEquation.ViewModel.ViewModel;

import java.text.SimpleDateFormat;

public class Solution {

    @FXML
    void initialize() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH", Locale.ENGLISH);
        String currentDate = dataFormat.format(calendar.getTime());

        viewModel.setLogger(new TxtLogger("./Logger-lab3_" + currentDate + ".log"));

        final ChangeListener<Boolean> focusChangeListener = (observable, oldValue, newValue)
                -> viewModel.onFocusChanged(oldValue, newValue);
        final List<TextField> textFields = new ArrayList<TextField>() {
            {
                add(first);
                add(second);
                add(third);
            }
        };

        first.textProperty().bindBidirectional(viewModel.aProperty());
        first.focusedProperty().addListener(focusChangeListener);
        second.textProperty().bindBidirectional(viewModel.bProperty());
        second.focusedProperty().addListener(focusChangeListener);
        third.textProperty().bindBidirectional(viewModel.cProperty());
        third.focusedProperty().addListener(focusChangeListener);

        Pattern p = Pattern.compile(LEGAL_INPUT);
        for (TextField textField : textFields) {
            TextFormatter<String> formatter = new TextFormatter<>(
                    (UnaryOperator<TextFormatter.Change>) change -> {
                        return p.matcher(change.getControlNewText()).matches() ? change : null;
                    });
            textField.setTextFormatter(formatter);
        }

        btnSolve.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent event) {
                viewModel.solve();
            }
        });
    }

    private static final String LEGAL_INPUT = "[-+]?[0-9]{0,7}(\\.[0-9]{0,7})?([0-9]{0,3})?";
    @FXML
    private ViewModel viewModel;
    @FXML
    private TextField first;
    @FXML
    private TextField second;
    @FXML
    private TextField third;
    @FXML
    private Button btnSolve;
}


