package dev.uptc.loginfx.presenter;

import dev.uptc.loginfx.model.DateCalculatorModel;
import dev.uptc.loginfx.views.DateCalculatorView;

import java.time.LocalDate;

public class DateCalculatorPresenter {
    private final DateCalculatorModel model;
    private final DateCalculatorView view;

    public DateCalculatorPresenter(DateCalculatorView view) {
        this.model = new DateCalculatorModel();
        this.view = view;
    }

    public void calculateFutureDate() {
        LocalDate currentDate = view.getCurrentDate();
        int daysToAdd = view.getDaysToAdd();

        if (currentDate != null && daysToAdd > 0) {
            LocalDate result = model.calculateFutureDate(currentDate, daysToAdd);
            view.setResult(result);
        } else {
            view.setResult(null);
        }
    }
}
