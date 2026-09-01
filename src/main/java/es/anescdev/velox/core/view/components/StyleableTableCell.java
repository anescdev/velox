package es.anescdev.velox.core.view.components;


import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

/**
 * @author AnesCDev
 */
public class StyleableTableCell<S, T> extends TableCell<S, T> {

    protected final StringConverter<T> converter;
    private final String[] styleClasses;
    protected final Label text = new Label();
    protected final HBox textBox = new HBox(text);

    public StyleableTableCell(StringConverter<T> converter, String ...styleClass) {
        this.converter = converter;
        this.styleClasses = styleClass;
        this.setGraphic(textBox);
        HBox.setHgrow(text, Priority.NEVER);
        this.setText(null);
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            this.text.setText(null);
            this.setGraphic(null);
            this.unapplyStyleClass();
        } else if(!this.isEditing()) {
            this.setGraphic(this.textBox);
            this.text.setText(this.itemToString(item));
            this.applyStyleClass();
        }
    }

    

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        this.setGraphic(this.textBox);
        this.applyStyleClass();
    }

    @Override
    public void startEdit() {
        super.startEdit();
        this.unapplyStyleClass();
    }

    protected void applyStyleClass() {
        var contentStyleClass = this.text.getStyleClass();
        for (String style : this.styleClasses) {
            if (!contentStyleClass.contains(style)) {
                contentStyleClass.add(style);
            }
        }
    }

    protected void unapplyStyleClass() {
        this.text.getStyleClass().removeAll(this.styleClasses);
    }

    protected String itemToString(T item) {
        return this.converter == null ? item.toString() : this.converter.toString(item);
    }

    public static <S> Callback<TableColumn<S, String>, TableCell<S, String>> forTableColumn(String ...styleClass) {
        return col -> new StyleableTableCell<>(new DefaultStringConverter(), styleClass);
    }

    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(StringConverter<T> converter, String ...styleClass) {
        return col -> new StyleableTableCell<>(converter, styleClass);
    }
    

}
