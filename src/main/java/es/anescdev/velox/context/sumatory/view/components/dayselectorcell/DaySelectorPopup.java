package es.anescdev.velox.context.sumatory.view.components.dayselectorcell;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import es.anescdev.velox.app.App;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Popup;

/**
 * @author AnesCDev
 */
public class DaySelectorPopup extends Popup {

    private static final String SELECTED_STRING = "selected";

    private final DaySelectorCell control;

    public DaySelectorPopup(DaySelectorCell control, int year, int month, List<Byte> days) {
        this.control = control;
        GridPane calendarGrid = new GridPane(6,6);
        calendarGrid.getStyleClass().add("day-selector-popup");
        String[] daysArr = new String[] {
                "dayselectorcell.day.monday",
                "dayselectorcell.day.tuesday",
                "dayselectorcell.day.wednesday",
                "dayselectorcell.day.thursday",
                "dayselectorcell.day.friday",
                "dayselectorcell.day.saturday",
                "dayselectorcell.day.sunday"
        };
        for (String dayOfMonth : daysArr) {
            Label labelDayMonth  = new Label(App.instance().getMessage(dayOfMonth));
            labelDayMonth.setAlignment(Pos.CENTER);
            GridPane.setHalignment(labelDayMonth, HPos.CENTER);
            calendarGrid.addRow(0, labelDayMonth);
        }
        var actualMonthDay = LocalDate.of(year, month, 1);
        var actualRow = 1;
        while (actualMonthDay.getMonthValue() == month) {
            var actualWeekDay = actualMonthDay.getDayOfWeek();
            byte dayOfMonth = (byte) actualMonthDay.getDayOfMonth();
            Button dayButton = new Button(Integer.toString(dayOfMonth));
            dayButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            dayButton.setOnAction(event -> {
                this.control.startEdit();
                if (days.contains(dayOfMonth)) {
                    dayButton.getStyleClass().remove(SELECTED_STRING);
                    days.remove((Byte) dayOfMonth);
                    this.control.getDayUnselectedCallback().accept(dayOfMonth, this.control.getTableRow().getItem());
                } else {
                    dayButton.getStyleClass().add(SELECTED_STRING);
                    days.add(dayOfMonth);
                    this.control.getDaySelectedCallback().accept(dayOfMonth, this.control.getTableRow().getItem());
                }
                days.sort(Byte::compare);
                this.control.commitEdit(days);
            });
            if (days.contains(dayOfMonth))
                dayButton.getStyleClass().add(SELECTED_STRING);
            GridPane.setHgrow(dayButton, Priority.ALWAYS);
            GridPane.setVgrow(dayButton, Priority.ALWAYS);
            GridPane.setHalignment(dayButton, HPos.CENTER);
            GridPane.setValignment(dayButton, VPos.CENTER);
            calendarGrid.add(dayButton,
                    actualWeekDay.getValue() - 1,
                    actualRow);
            if (actualWeekDay == DayOfWeek.SUNDAY)
                actualRow++;
            actualMonthDay = actualMonthDay.plusDays(1);
        }
        this.getContent().add(calendarGrid);
    }

}
