package es.anescdev.velox.core.view.controller;


import es.anescdev.velox.core.bus.Bus;
import es.anescdev.velox.core.view.TabManager;

/**
 * @author AnesCDev
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
