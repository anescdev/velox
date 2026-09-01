package es.anescdev.velox.core.view;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.inject.Singleton;

import es.anescdev.velox.app.App;
import es.anescdev.velox.core.bus.Bus;
import es.anescdev.velox.core.bus.BusKey;
import es.anescdev.velox.core.utils.Reseteable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

@Singleton
/**
 * Gestiona la apertura, reutilización y cierre de las pestañas del
 * {@link es.anescdev.velox.core.view.controller.WorkspaceController}. Evita abrir dos veces
 * la misma pantalla (si ya existe una pestaña con ese id, la selecciona en lugar de crear
 * otra) y centraliza la comprobación de si el conjunto de pestañas abiertas permite cerrar
 * sesión / cambiar de empleado.
 */
public class TabManager implements Reseteable {
    public static final BusKey<String> TITLE_KEY = new BusKey<>("title", String.class);
    public static final BusKey<String> SCENE_KEY = new BusKey<>("scene", String.class);
    public static final BusKey<String> TAB_ID_KEY = new BusKey<>("tabid", String.class);

    private final ObservableSet<String> openedTabsId = FXCollections.observableSet();
    private Consumer<String> onTryOpenExistingTab;
    private BiConsumer<String, Bus> onOpenNewTab;
    private Consumer<String> onForceCloseTab;
    private Supplier<Boolean> canReset;

    /**
     * Abre un nodo de JavaFX contenido en un archivo FXML en una pestaña del
     * espacio de trabajo. Si esta ya está abierta, no abrirá otra más.
     * <br>
     * Cuando intentes abrir una pestaña, deberás de incluir obligatoriamente en
     * "data"
     * el título de la pestaña en la clave "title". Para incluir datos extra a la
     * pestaña, opcionalmente
     * podrás incluirla en la clave "userData"
     * 
     * @param sceneName Nombre del archivo FXML. Si está en alguna subcarpeta de
     *                  resources/scenes, debe de llamarlo así: carpeta/nombre_fxml.
     *                  Sin extensión
     * @param data      Mapa de datos que contiene datos extra para la petición de
     *                  abrir la pestaña.
     */
    public void openTab(String id, Bus data) {
        if (this.openedTabsId.contains(id)) {
            this.onTryOpenExistingTab.accept(id);
            return;
        }
        data.setData(TabManager.TAB_ID_KEY, id);
        this.openedTabsId.add(id);
        this.onOpenNewTab.accept(id, data);
    }

    public void forceRemoveTab(String id) {
        this.openedTabsId.remove(id);
        this.onForceCloseTab.accept(id);
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
    public void setOnOpenNewTab(BiConsumer<String, Bus> onOpenNewTab) {
        this.onOpenNewTab = onOpenNewTab;
    }

    /**
     * @param onForceCloseTab the onForceCloseTab to set
     */
    public void setOnForceCloseTab(Consumer<String> onForceCloseTab) {
        this.onForceCloseTab = onForceCloseTab;
    }

    /**
     * @param checkCanClose the checkCanClose to set
     */
    public void setCheckCanClose(Supplier<Boolean> canReset) {
        this.canReset = canReset;
    }

    @Override
    public void reset() {
        this.openedTabsId.clear();
        App.instance().getLogger().info("Reseted " + this.getClass().getSimpleName());
    }

    @Override
    public boolean canReset() {
        return this.canReset.get();
    }

    

}
