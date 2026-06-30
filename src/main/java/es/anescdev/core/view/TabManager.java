package es.anescdev.core.view;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.inject.Singleton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

/**
 * @author AnesCDev
 */
@Singleton
public class TabManager {
    public static final String TITLE_KEY = "title";
    public static final String SCENE_KEY = "scene";
    public static final String USER_DATA_KEY = "userData";

    private final ObservableSet<String> openedTabsId = FXCollections.observableSet();
    private Consumer<String> onTryOpenExistingTab;
    private BiConsumer<String, Map<String, Object>> onOpenNewTab;

    /**
     * Abre un nodo de JavaFX contenido en un archivo FXML en una pestaña del
     * espacio de trabajo. Si esta ya está abierta, no abrirá otra más.
     * <br>
     * Cuando intentes abrir una pestaña, deberás de incluir obligatoriamente en "data"
     * el título de la pestaña en la clave "title". Para incluir datos extra a la pestaña, opcionalmente
     * podrás incluirla en la clave "userData"
     * 
     * @param sceneName Nombre del archivo FXML. Si está en alguna subcarpeta de
     *                  resources/scenes, debe de llamarlo así: carpeta/nombre_fxml.
     *                  Sin extensión
     * @param data      Mapa de datos que contiene datos extra para la petición de
     *                  abrir la pestaña.
     */
    public void openTab(String id, Map<String, Object> data) {
        if (this.openedTabsId.contains(id)) {
            this.onTryOpenExistingTab.accept(id);
            return;
        }
        this.openedTabsId.add(id);
        this.onOpenNewTab.accept(id, data);
    }

    public void removeTab(String id) {
        this.openedTabsId.remove(id);
    }

    /**
     * @param onTryOpenExistingTab the onTryOpenExistingTab listener to set
     */
    public void setOnTryOpenExistingTab(Consumer<String> onTryOpenExistingTab) {
        this.onTryOpenExistingTab = onTryOpenExistingTab;
    }

    /**
     * @param onOpenNewTab the onOpenNewTab listener to set
     */
    public void setOnOpenNewTab(BiConsumer<String, Map<String, Object>> onOpenNewTab) {
        this.onOpenNewTab = onOpenNewTab;
    }

}
