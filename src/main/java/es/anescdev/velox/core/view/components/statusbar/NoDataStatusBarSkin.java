package es.anescdev.velox.core.view.components.statusbar;

/**
 * Componente de interfaz reutilizable del dominio None (diálogo, celda, "skin" de
 * barra de estado, etc.).
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
