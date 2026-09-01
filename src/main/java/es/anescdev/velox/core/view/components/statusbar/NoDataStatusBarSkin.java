package es.anescdev.velox.core.view.components.statusbar;

/**
 * @author AnesCDev
 */
public class NoDataStatusBarSkin extends BaseStatusBarSkin {

    protected NoDataStatusBarSkin(CustomStatusBar control) {
        super(control);
        this.getChildren().clear();
    }

    @Override
    protected void updateSkin() {
    }

}
