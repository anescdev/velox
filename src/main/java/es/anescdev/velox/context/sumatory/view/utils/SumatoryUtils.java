package es.anescdev.velox.context.sumatory.view.utils;

import es.anescdev.velox.app.App;
import es.anescdev.velox.context.sumatory.SumatoryModule;
import es.anescdev.velox.context.sumatory.model.entity.Sumatory;
import es.anescdev.velox.core.bus.Bus;
import es.anescdev.velox.core.view.TabManager;

/**
 * @author AnesCDev
 */
public class SumatoryUtils {
    public static void openSumatory(TabManager tabManager, Sumatory sumatory) {
        Bus tabBus = new Bus();
        tabBus.setData(TabManager.TITLE_KEY, App.instance().getMessage("%sumatory.title.details "+sumatory.getMonth()+"/"+sumatory.getYear()));
        tabBus.setData(TabManager.SCENE_KEY, "sumatory/entry_list");
        tabBus.setData(SumatoryModule.SUMATORY_KEY, sumatory);
        tabManager.openTab("sumDetails"+sumatory.getId(), tabBus);
    }
}
