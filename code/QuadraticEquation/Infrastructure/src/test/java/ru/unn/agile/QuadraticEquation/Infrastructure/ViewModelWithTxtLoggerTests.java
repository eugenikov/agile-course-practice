package ru.unn.agile.QuadraticEquation.Infrastructure;

import ru.unn.agile.QuadraticEquation.ViewModel.ViewModel;
import ru.unn.agile.QuadraticEquation.ViewModel.ViewModelTests;

public class ViewModelWithTxtLoggerTests extends ViewModelTests {
    @Override
    public void setViewModel() {
        TxtLogger realLogger =
                new TxtLogger("./ViewModelWithTxtLoggerTests-lab3.log");
        super.setViewModel(new ViewModel(realLogger));
    }
}
