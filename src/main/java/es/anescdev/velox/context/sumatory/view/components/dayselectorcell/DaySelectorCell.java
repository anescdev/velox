package es.anescdev.velox.context.sumatory.view.components.dayselectorcell;

import java.util.List;
import java.util.function.BiConsumer;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.unicons.UniconsLine;

import es.anescdev.velox.app.App;
import es.anescdev.velox.context.sumatory.view.utils.GroupedSumatoryEntry;
import es.anescdev.velox.context.sumatory.view.utils.ListByteStringConverter;
import es.anescdev.velox.core.view.components.StyleableTableCell;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * @author AnesCDev
 */
public class DaySelectorCell extends StyleableTableCell<GroupedSumatoryEntry, List<Byte>> {

    private final static String[] STYLECLASS_STRINGS = { "num-badge", "background" };
    private final static String OPENED_STYLECLASS = "opened";

    private final static int DAYS_LIMIT = 5;

    private DaySelectorPopup calendarPopup;
    private final HBox daysBox = new HBox(8);
    private Button calendarButton;

    private final Label[] dayLabels = new Label[DAYS_LIMIT];
    private final SimpleIntegerProperty restDay = new SimpleIntegerProperty(0);

    private final int year;
    private final int month;

    private BiConsumer<Byte, GroupedSumatoryEntry> daySelected;

    private BiConsumer<Byte, GroupedSumatoryEntry> dayUnselected;

    /**
     * @param converter
     * @param styleClass
     */
    public DaySelectorCell(StringConverter<List<Byte>> converter, int year, int month, String... styleClass) {
        super(converter, styleClass);

        this.year = year;
        this.month = month;

        this.calendarButton = new Button();
        calendarButton.getStyleClass().add("calendar-button");
        calendarButton.setGraphic(new FontIcon(UniconsLine.CALENDAR_ALT));
        calendarButton.setOnAction(event -> {
            this.createPopup();
            Point2D cellPos = this.localToScreen(0, this.getHeight());
            if (this.calendarPopup.isShowing()) {
                this.calendarPopup.hide();
                calendarButton.getStyleClass().remove(OPENED_STYLECLASS);
            } else {
                this.calendarPopup.show(this, cellPos.getX(), cellPos.getY());
                calendarButton.getStyleClass().add(OPENED_STYLECLASS);
            }
        });

        StackPane.setAlignment(calendarButton, Pos.CENTER);
        daysBox.setAlignment(Pos.CENTER);
        for (int i = 0; i < DAYS_LIMIT; i++) {
            var dayLabel = new Label();
            dayLabel.getStyleClass().addAll(STYLECLASS_STRINGS);
            this.dayLabels[i] = dayLabel;
            daysBox.getChildren().add(dayLabel);
        }
        Label restDaysLabel = new Label();
        restDaysLabel.textProperty()
                .bind(this.restDay.map(day -> day.intValue() == 0 ? "" : "+" + day));
        restDaysLabel.getStyleClass().addAll(STYLECLASS_STRINGS[0], "rest");
        daysBox.getChildren().add(restDaysLabel);
        StackPane.setAlignment(daysBox, Pos.CENTER_LEFT);
        StackPane.setAlignment(this.text, Pos.CENTER_LEFT);
        StackPane cellContainer = new StackPane(daysBox, this.text, calendarButton);
        cellContainer.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(cellContainer, Priority.NEVER);

        this.contentBox.getChildren().clear();
        this.contentBox.getChildren().add(cellContainer);
    }

    @Override
    protected void updateItem(List<Byte> item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {

        } else {
            if (!this.isEditing()) {
                if (item.isEmpty()) {
                    this.text.textProperty().setValue(App.instance().getMessage("dayselectorcell.empty"));
                    this.text.setVisible(true);
                    this.text.setManaged(true);
                    this.daysBox.setVisible(false);
                } else {
                    if (this.text.isVisible()) {
                        this.text.setManaged(false);
                        this.text.setVisible(false);
                    }
                    this.daysBox.setVisible(true);
                    for (int i = 0; i < DAYS_LIMIT; i++) {
                        if (i < item.size()) {
                            var day = item.get(i);
                            this.dayLabels[i].setText((day < 10 ? "0" : "") + Byte.toString(day));
                            this.dayLabels[i].setVisible(true);
                        } else {
                            this.dayLabels[i].setText("");
                            this.dayLabels[i].setVisible(false);
                        }
                        // TODO: Falta que al cambiar el texto
                    }
                    this.restDay.set(item.size() > DAYS_LIMIT ? item.size() - DAYS_LIMIT : 0);

                }
            }
        }
    }

    public void setDaySelected(BiConsumer<Byte, GroupedSumatoryEntry> daySelected) {
        this.daySelected = daySelected;
    }

    public void setDayUnselected(BiConsumer<Byte, GroupedSumatoryEntry> dayUnselected) {
        this.dayUnselected = dayUnselected;
    }

    protected BiConsumer<Byte, GroupedSumatoryEntry> getDaySelectedCallback() {
        return this.daySelected;
    }

    protected BiConsumer<Byte, GroupedSumatoryEntry> getDayUnselectedCallback() {
        return this.dayUnselected;
    }

    private void createPopup() {
        if (this.calendarPopup != null)
            return;
        var calendarPopup = new DaySelectorPopup(this, year, month, this.getTableRow().getItem().getDays());
        calendarPopup.setAutoHide(true);
        calendarPopup.setOnAutoHide(event -> calendarButton.getStyleClass().remove(OPENED_STYLECLASS));
        this.calendarPopup = calendarPopup;
    }

    public static Callback<TableColumn<GroupedSumatoryEntry, List<Byte>>, TableCell<GroupedSumatoryEntry, List<Byte>>> forTableColumn(
            int year, int month, BiConsumer<Byte, GroupedSumatoryEntry> daySelectedCallback,
            BiConsumer<Byte, GroupedSumatoryEntry> dayUnselectedCallback, String... styleClasseStrings) {
        return col -> {
            var daySelectorCell = new DaySelectorCell(new ListByteStringConverter(), year, month, styleClasseStrings);
            daySelectorCell.setDaySelected(daySelectedCallback);
            daySelectorCell.setDayUnselected(dayUnselectedCallback);
            return daySelectorCell;
        };
    }

}
