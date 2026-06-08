package es.anescdev.view.info.controller;

import es.anescdev.App;
import es.anescdev.view.controller.BaseController;
import javafx.fxml.FXML;

public class StageInfoController extends BaseController {
	
	@FXML
	private void goToDevWeb() {
		App.instance().getHostServices().showDocument(this.getMessages().getString("info.dev.web"));
	}
}
