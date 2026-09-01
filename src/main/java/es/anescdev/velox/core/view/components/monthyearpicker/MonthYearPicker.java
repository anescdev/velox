package es.anescdev.velox.core.view.components.monthyearpicker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Skin;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;

/**
 * Componente de interfaz reutilizable del dominio None (diálogo, celda, "skin" de
 * barra de estado, etc.).
 */
public class MonthYearPicker extends ComboBoxBase<LocalDate> {
    public final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/yyyy");

        public MonthYearPicker(LocalDate initialValue) {
        super();
        this.setValue(initialValue);
        this.setSkin(this.createDefaultSkin());
    }
    public MonthYearPicker() {
        this(LocalDate.now());
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new MonthYearPickerSkin(this);
    }

    protected StringConverter<LocalDate> getConverter() {
        return new LocalDateStringConverter(DATE_FORMATTER, DATE_FORMATTER);
    }

}
