package es.anescdev.workspace.view.controller;

import es.anescdev.App;
import es.anescdev.shared.BaseController;
import javafx.fxml.FXML;

public class AppInfoController extends BaseController {
	
	@FXML
	private void goToDevWeb() {
		App.instance().getHostServices().showDocument(this.getMessages().getString("info.developed.web"));
	}
}
