package es.anescdev.core.view.controller;

import es.anescdev.App;
import javafx.fxml.FXML;

public class AppInfoController extends BaseController {
	
	@FXML
	private void goToDevWeb() {
		App.instance().getHostServices().showDocument(this.getMessages().getString("info.developed.web"));
	}
}
