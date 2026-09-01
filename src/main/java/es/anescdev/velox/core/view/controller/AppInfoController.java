package es.anescdev.velox.core.view.controller;

import es.anescdev.velox.app.App;
import javafx.fxml.FXML;

public class AppInfoController extends BaseController {
	
	@FXML
	private void goToDevWeb() {
		App.instance().getHostServices().showDocument(this.getMessage("info.developed.web"));
	}
}
