package es.anescdev.velox.core.view.components.settingdialog;

import java.util.Collection;

import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;

import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * @author AnesCDev
 */
public class AppendedAutoCompletionTextFieldBinding<T> extends AutoCompletionTextFieldBinding<T> {

    private StringConverter<T> converter;

    public AppendedAutoCompletionTextFieldBinding(TextField textField,
            Callback<ISuggestionRequest, Collection<T>> suggestionProvider, StringConverter<T> converter) {
        super(textField, suggestionProvider, converter);
        this.converter = converter;
    }

    public AppendedAutoCompletionTextFieldBinding(TextField textField,
            Callback<ISuggestionRequest, Collection<T>> suggestionProvider) {
        super(textField, suggestionProvider);
    }

    @Override
    protected void completeUserInput(T completion) {
        var patternVar = this.converter == null ? completion.toString() : this.converter.toString(completion);
        var textField = this.getCompletionTarget();
        var newText = textField.getText().substring(0, textField.getText().length() - 1) + patternVar;
        textField.setText(newText);
        textField.positionCaret(newText.length());
    }

}
