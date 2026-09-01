package es.anescdev.velox.core.view.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

/**
 * Componente de interfaz reutilizable del dominio None (diálogo, celda, "skin" de
 * barra de estado, etc.).
 */
public class TextFieldActionTableCell<S, T> extends StyleableTableCell<S, T> {

    private TextField textField;
    private final HBox editContainer = new HBox();
    // TODO: Hacer funcionar el botón de búsqueda avanzada

    private TextFieldActionTableCell(StringConverter<T> converter, String... styleClasses) {
        super(converter, styleClasses);

    }

    public static <S> Callback<TableColumn<S, String>, TableCell<S, String>> noAction(String... styleClasses) {
        return col -> new TextFieldActionTableCell<>(new DefaultStringConverter(), styleClasses);
    }

    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> noAction(StringConverter<T> converter,
            String... styleClasses) {
        return col -> new TextFieldActionTableCell<>(converter, styleClasses);
    }

    public static <S> Callback<TableColumn<S, String>, TableCell<S, String>> withAction(TextFieldAction action,
            String... styleClasses) {
        return col -> {
            var cell = new TextFieldActionTableCell<S, String>(new DefaultStringConverter(), styleClasses);
            cell.editContainer.getChildren().add(action.toButton());
            return cell;
        };
    }

    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> withAction(StringConverter<T> converter,
            TextFieldAction action, String... styleClasses) {
        return col -> {
            var cell = new TextFieldActionTableCell<S, T>(converter, styleClasses);
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

    public static record TextFieldAction(Node buttonContent, boolean isGraphic, EventHandler<ActionEvent> clickEvent) {
        public static TextFieldAction onlyIcon(Node buttonContent, EventHandler<ActionEvent> clickEvent) {
            return new TextFieldAction(buttonContent, true, clickEvent);
        }

        public static TextFieldAction onlyLabel(Text buttonContent, EventHandler<ActionEvent> clickEvent) {
            return new TextFieldAction(buttonContent, false, clickEvent);
        }

        private Button toButton() {
            Button button = new Button();
            if (!this.isGraphic() && this.buttonContent() instanceof Text text) {
                button.setText(text.getText());
            } else {
                button.setGraphic(this.buttonContent());
            }
            button.setOnAction(this.clickEvent());
            return button;
        }
    }

}
