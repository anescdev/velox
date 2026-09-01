package es.anescdev.velox.context.employee.view.commands;

import es.anescdev.velox.app.App;
import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.core.bus.Bus;
import es.anescdev.velox.core.command.Command;
import es.anescdev.velox.core.view.controller.WorkspaceController;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

/**
 * @author AnesCDev
 */
public class GoToWorkspaceCommand implements Command<Void>{

    private final Scene mainScene;
    private final Employee employeeSelected;

    

    /**
     * @param mainScene
     * @param employeeSelected
     */
    public GoToWorkspaceCommand(Scene mainScene, Employee employeeSelected) {
        this.mainScene = mainScene;
        this.employeeSelected = employeeSelected;
    }



    @Override
    public Void executeCommand() {
        if(this.employeeSelected == null) return null;
        var result = App.<VBox, WorkspaceController>loadFXML("workspace");
        var data = new Bus();
        data.setData(WorkspaceController.EMPLOYEE_KEY, this.employeeSelected);
        result.controller().initData(data);
        mainScene.setRoot(result.node());
        return null;
    }
    
}
