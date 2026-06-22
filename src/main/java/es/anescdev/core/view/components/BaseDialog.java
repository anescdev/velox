package es.anescdev.core.view.components;

import es.anescdev.App;
import es.anescdev.core.view.utils.DialogUtilities;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;

public abstract class BaseDialog<R> extends Dialog<R>{
    public BaseDialog(String title) {
        super();
        this.initModality(Modality.WINDOW_MODAL);
		this.initOwner(App.getMainStage());
        this.setTitle(title);
        DialogUtilities.centerDialog(this);
    }
}
