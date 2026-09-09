package es.anescdev.velox.context.cod.view.components;

import es.anescdev.velox.app.App;

/**
 * @author AnesCDev
 */
public enum CodSearcherFilter {
    COD("cod.dialog.searcher.filter.cod"),
    DESCRIPTION("cod.dialog.searcher.filter.description");

    private String filter;

    private CodSearcherFilter(String filter) {
        this.filter = filter;
    }

    @Override
    public String toString() {
        return App.instance().getMessage(this.filter);
    }

}
