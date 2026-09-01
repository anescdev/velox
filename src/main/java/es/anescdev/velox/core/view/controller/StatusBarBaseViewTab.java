package es.anescdev.velox.core.view.controller;

import es.anescdev.velox.core.view.TabManager;

/**
 * @author AnesCDev
 */
public abstract class StatusBarBaseViewTab extends BaseViewTabController {

    public StatusBarBaseViewTab(TabManager tabManager) {
        super(tabManager);
    }

    protected abstract void updateControlUserData();

}
