package es.anescdev.sumatory.view.utils;

import java.util.HashMap;

import es.anescdev.core.view.TabManager;
import es.anescdev.sumatory.model.entities.Sumatory;
import lombok.experimental.UtilityClass;

/**
 * @author AnesCDev
 */
@UtilityClass
public class TimeLogUtils {
    public void openSumatoryDetails(TabManager tabManager, Sumatory sumatory) {
        var data = new HashMap<String, Object>();
        data.put(TabManager.TITLE_KEY, "Sumatorio de " + sumatory.getEmployee() + " " + sumatory.getMonth() + "/" + sumatory.getYear());
        data.put(TabManager.USER_DATA_KEY, sumatory);
        data.put(TabManager.SCENE_KEY, "time_log/list");
        tabManager.openTab(
                "timeLogList_" + sumatory.getEmployee() + "_" + sumatory.getMonth() + "/" + sumatory.getYear(), 
                data);
    }
}
