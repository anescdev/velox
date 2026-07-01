package es.anescdev.core.view.components.monthyearpicker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * @author AnesCDev
 */
public class MonthYearPickerPopup extends HBox {
    protected final SimpleObjectProperty<LocalDate> dateProperty = new SimpleObjectProperty<>();
    private final SimpleStringProperty yearStringProperty = new SimpleStringProperty();
    private final SimpleStringProperty monthStringProperty = new SimpleStringProperty();
    private final DateTimeFormatter monthNameFormatter = DateTimeFormatter.ofPattern("MMMM");

    public MonthYearPickerPopup() {
        super();
        this.getStyleClass().add("month-year-picker-popup");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        this.getChildren().addAll(this.createMonthSelector(), spacer, this.createYearSelector());
        this.setSpacing(6);
        this.dateProperty.addListener((ob, oldDate, newDate) -> {
            if (newDate == null)
                return;
            this.yearStringProperty.set(String.valueOf(newDate.getYear()));
            this.monthStringProperty.set(monthNameFormatter.format(newDate));
        });
    }

    public void setValue(LocalDate date) {
        this.dateProperty.set(date);
    }

    private HBox createMonthSelector() {
        Label label = new Label();
        label.textProperty().bind(monthStringProperty);
        label.setMinWidth(80);
        label.setAlignment(Pos.CENTER);

        Button prev = this.createButton("‹");
        Button next = this.createButton("›");

        prev.setOnAction(e -> this.dateProperty.set(this.dateProperty.get().minusMonths(1)));
        next.setOnAction(e -> this.dateProperty.set(this.dateProperty.get().plusMonths(1)));

        HBox monthSelector = new HBox(prev, label, next);
        monthSelector.setAlignment(Pos.CENTER_LEFT);
        return monthSelector;
    }

    private HBox createYearSelector() {
        Label label = new Label();
        label.textProperty().bind(yearStringProperty);
        label.setMinWidth(40);
        label.setAlignment(Pos.CENTER);

        Button prev = this.createButton("‹");
        Button next = this.createButton("›");

        prev.setOnAction(e -> this.dateProperty.set(this.dateProperty.get().minusYears(1)));
        next.setOnAction(e -> this.dateProperty.set(this.dateProperty.get().plusYears(1)));

        HBox yearSelector = new HBox(prev, label, next);
        yearSelector.setAlignment(Pos.CENTER_RIGHT);
        return yearSelector;
    }
    private Button createButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("month-year-picker-nav-button");
        return btn;
    }
}
