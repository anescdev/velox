package es.anescdev.velox.core.view.controller;

import es.anescdev.velox.app.App;
import javafx.fxml.FXML;

/**
 * Controlador FXML del dominio None. Conecta los componentes definidos en el FXML
 * asociado con el ViewModel y los comandos del dominio, siguiendo el patrón de
 * {@code BaseController}/{@code BaseViewTabController}.
 */
public class AppInfoController extends BaseController {
	
	@FXML
	private void goToDevWeb() {
		App.instance().getHostServices().showDocument(this.getMessage("info.developed.web"));
	}
}
