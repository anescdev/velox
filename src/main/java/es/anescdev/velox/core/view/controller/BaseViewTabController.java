package es.anescdev.velox.core.view.controller;


import es.anescdev.velox.core.bus.Bus;
import es.anescdev.velox.core.view.TabManager;

/**
 * Controlador base para las pantallas que viven dentro de una pestaña del
 * {@code WorkspaceController} (gestionadas por {@link es.anescdev.velox.core.view.TabManager}).
 * Recoge automáticamente el id de su propia pestaña del {@code Bus} y ofrece
 * {@link #forceTabExit()} para cerrarse a sí mismo (p. ej. tras borrar la entidad que muestra).
 */
public abstract class BaseViewTabController extends BaseController {
    private final TabManager tabManager;
    private String tabId;

    /**
     * @param tabManager
     */
    public BaseViewTabController(TabManager tabManager) {
        this.tabManager = tabManager;
    }

    @Override
    public void initData(Bus busData) {
        busData.getFromBus(TabManager.TAB_ID_KEY).ifPresent(tabId -> this.tabId = tabId);
    }

    protected void forceTabExit() {
        this.tabManager.forceRemoveTab(tabId);
    }
}
