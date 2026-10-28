package es.anescdev.velox.context.sumatory.view.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import es.anescdev.velox.context.sumatory.SumatoryModule;
import es.anescdev.velox.context.sumatory.model.entity.Sumatory;
import es.anescdev.velox.core.bus.Bus;
import es.anescdev.velox.core.view.TabManager;

/**
 * Utilidad de la capa de vista del dominio sumatory (conversión de datos para
 * la UI,
 * apertura de pantallas relacionadas, eventos de edición en tablas, etc.).
 */
public class SumatoryUtils {

    private static DateTimeFormatter DATE_TAB_FORMATTER;

    public static void openSumatory(TabManager tabManager, Sumatory sumatory) {
        Bus tabBus = new Bus();
        LocalDate date = LocalDate.of(sumatory.getYear(), sumatory.getMonth(), 1);
        tabBus.setData(TabManager.TITLE_KEY,
                "%sumatory.title.details %sumatory.title.details.separator1 "
                        + SumatoryUtils.getDateTimeFormatter().format(date));
        tabBus.setData(TabManager.COLLAPSED_TITLE_KEY,
                "%sumatory.title.details " + sumatory.getMonth() + "/" + sumatory.getYear());
        tabBus.setData(TabManager.SCENE_KEY, "sumatory/entry_list");
        tabBus.setData(SumatoryModule.SUMATORY_KEY, sumatory);
        tabManager.openTab("sumDetails" + sumatory.getId(), tabBus);
    }

    private static DateTimeFormatter getDateTimeFormatter() {
        if (DATE_TAB_FORMATTER == null) {
            DATE_TAB_FORMATTER = DateTimeFormatter.ofPattern("MMMM '%sumatory.title.details.separator2' YYYY");
        }
        return DATE_TAB_FORMATTER;
    }
}
