package es.anescdev.velox.context.cod.view.components;

import java.util.List;

import javax.inject.Inject;

import org.controlsfx.control.textfield.TextFields;

import es.anescdev.velox.app.App;
import es.anescdev.velox.context.cod.model.entities.Cod;
import es.anescdev.velox.context.cod.model.services.CodService;
import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.core.view.components.BaseDialog;
import es.anescdev.velox.core.view.components.ComboBox;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

/**
 * @author AnesCDev
 */
public class CodSearcherDialog extends BaseDialog<Cod> {

    @Inject
    private CodService codService;
    private CodSearcherFilter lastFilter = CodSearcherFilter.COD;
    private final SimpleObjectProperty<Cod> pickedCod = new SimpleObjectProperty<>();

    public CodSearcherDialog(String title, Employee employeeFilter) {
        super(title);
        App.instance().getFeather().injectFields(this);

        Label label = new Label(App.instance().getMessage("cod.dialog.searcher.tip"));
        label.getStyleClass().add("subtitle");

        TextField searchTextField = new TextField();
        searchTextField.setPrefWidth(325);

        ComboBox<CodSearcherFilter> searchFilter = new ComboBox<>(FXCollections.observableArrayList(
                CodSearcherFilter.COD,
                CodSearcherFilter.DESCRIPTION));
        searchFilter.getSelectionModel().select(this.lastFilter);
        searchFilter.setMaxWidth(155);
        this.setOnShown(dialogEvent -> {
            searchFilter.setValue(this.lastFilter);
            searchTextField.requestFocus();
        });

        HBox searcherBox = new HBox(6, searchFilter, searchTextField);
        HBox.setHgrow(searchTextField, Priority.ALWAYS);

        TextFields.<Cod>bindAutoCompletion(searchTextField, autocompleteRequest -> {
            if(autocompleteRequest.getUserText().isBlank()) return List.of();
            if (searchFilter.getValue().equals(CodSearcherFilter.COD)) {
                var cod = autocompleteRequest.getUserText().split("-");
                return this.codService.searchCods(cod[0], cod.length > 1 ? cod[1] : "", employeeFilter);
            } else {
                return this.codService.searchCodsByDescription(autocompleteRequest.getUserText(), employeeFilter);
            }
        }, new CodStringConverter(searchFilter.valueProperty()))
                .setOnAutoCompleted(autoCompleteEvent -> {
                    this.pickedCod.set(autoCompleteEvent.getCompletion());
                    this.lastFilter = searchFilter.getValue();
                });

        this.setResultConverter(buttonPressed -> buttonPressed.equals(ButtonType.OK) ? this.pickedCod.get() : null);
        this.setContent(new VBox(14, label, searcherBox));

        this.addButtonType(ButtonType.CANCEL, ButtonType.OK);
        this.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(this.pickedCod.isNull());
    }

    public CodSearcherDialog(Employee employeeFilter) {
        this(App.instance().getMessage("cod.dialog.searcher.title"), employeeFilter);
    }

    private static class CodStringConverter extends StringConverter<Cod> {
        private final ObjectProperty<CodSearcherFilter> selectedFilter;

        /**
         * @param filterOptions
         * @param selectedFilter
         */
        public CodStringConverter(ObjectProperty<CodSearcherFilter> selectedFilter) {
            this.selectedFilter = selectedFilter;
        }

        @Override
        public String toString(Cod object) {
            return this.selectedFilter.get().equals(CodSearcherFilter.COD) ? object.getCompleteCod()
                    : object.getDescription();
        }

        @Override
        public Cod fromString(String string) {
            throw new UnsupportedOperationException("Unimplemented method 'fromString'");
        }

    }

}
