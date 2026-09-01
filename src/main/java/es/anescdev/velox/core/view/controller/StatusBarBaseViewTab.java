package es.anescdev.velox.core.view.controller;

import es.anescdev.velox.core.view.TabManager;

/**
 * Controlador base para pestañas que además actualizan la barra de estado inferior
 * ({@code CustomStatusBar}) con datos propios de esa pantalla al seleccionarse
 * (ver {@link #updateControlUserData()}, invocado desde {@code WorkspaceController}
 * cuando el usuario cambia de pestaña).
 */
public abstract class StatusBarBaseViewTab extends BaseViewTabController {

    public StatusBarBaseViewTab(TabManager tabManager) {
        super(tabManager);
    }

    protected abstract void updateControlUserData();

}
