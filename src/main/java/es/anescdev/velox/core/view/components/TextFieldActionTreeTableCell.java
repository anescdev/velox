package es.anescdev.velox.core.view.components;

import es.anescdev.velox.core.view.utils.TextFieldAction;

import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

/**
 * Componente de interfaz reutilizable del dominio None (diálogo, celda, "skin" de
 * barra de estado, etc.).
 */
public class TextFieldActionTreeTableCell<S, T> extends StyleableTreeTableCell<S, T> {

    private TextField textField;
    private final HBox editContainer = new HBox();
    // TODO: Hacer funcionar el botón de búsqueda avanzada

    private TextFieldActionTreeTableCell(StringConverter<T> converter, String... styleClasses) {
        super(converter, styleClasses);

    }

    public static <S> Callback<TreeTableColumn<S, String>, TreeTableCell<S, String>> noAction(String... styleClasses) {
        return col -> new TextFieldActionTreeTableCell<>(new DefaultStringConverter(), styleClasses);
    }

    public static <S, T> Callback<TreeTableColumn<S, T>, TreeTableCell<S, T>> noAction(StringConverter<T> converter,
            String... styleClasses) {
        return col -> new TextFieldActionTreeTableCell<>(converter, styleClasses);
    }

    public static <S> Callback<TreeTableColumn<S, String>, TreeTableCell<S, String>> withAction(TextFieldAction action,
            String... styleClasses) {
        return col -> {
            var cell = new TextFieldActionTreeTableCell<S, String>(new DefaultStringConverter(), styleClasses);
            cell.editContainer.getChildren().add(action.toButton());
            return cell;
        };
    }

    public static <S, T> Callback<TreeTableColumn<S, T>, TreeTableCell<S, T>> withAction(StringConverter<T> converter,
            TextFieldAction action, String... styleClasses) {
        return col -> {
            var cell = new TextFieldActionTreeTableCell<S, T>(converter, styleClasses);
            cell.editContainer.getChildren().add(action.toButton());
            return cell;
        };
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        this.text.setText(this.itemToString(this.getItem()));
    }

    @Override
    public void startEdit() {
        super.startEdit();
        if (!isEditing())
            return;
        if (this.textField == null) {
            this.textField = this.createTextField();
            this.editContainer.getChildren().addFirst(this.textField);
        }
        this.setGraphic(this.editContainer);
        this.textField.setText(this.text.getText());
        this.textField.requestFocus();
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (this.isEditing()) {
            this.setGraphic(text);
            this.textField.setText(this.itemToString(item));
        }
            
    }

    @Override
    public void commitEdit(T newValue) {
        super.commitEdit(newValue);
        setGraphic(textBox);
        if (this.text != null) {
            this.text.setText(this.itemToString(newValue));
        }
    }

    private TextField createTextField() {
        var field = new TextField();
        field.setOnAction(event -> {
            if (this.converter != null) {
                this.commitEdit(this.converter.fromString(field.getText()));
                event.consume();
            }

        });
        field.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                this.cancelEdit();
                event.consume();
            }
        });
        return field;
    }

}
