package es.anescdev.velox.context.sumatory.view.components;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import es.anescdev.velox.core.view.utils.StringUtilities;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class MonthTableCell<S> extends TableCell<S, LocalDate> {

    private final DateTimeFormatter dateConverter;

    /**
     * @param dateConverter
     */
    public MonthTableCell(DateTimeFormatter dateConverter) {
        if (dateConverter == null) {
            throw new IllegalArgumentException("dateConverter cannot be null");
        }
        this.dateConverter = dateConverter;
    }

    public static <S> Callback<TableColumn<S, LocalDate>, TableCell<S, LocalDate>> forTableColumn(
            DateTimeFormatter converter) {
        return col -> new MonthTableCell<S>(converter);
    }

    @Override
    protected void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) {
            this.setText(StringUtilities.toFirstUpper(this.dateConverter.format(item)));
        } else {
            this.setText(null);
        }
    }
    

}