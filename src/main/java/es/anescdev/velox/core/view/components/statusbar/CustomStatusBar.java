package es.anescdev.velox.core.view.components.statusbar;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * @author AnesCDev
 */
public class CustomStatusBar extends Control {
    private static CustomStatusBar statusBar;
    private final static Map<Class<?>, Class<? extends BaseStatusBarSkin>> USER_DATA_SKIN_REGISTRY = new HashMap<>();

    /**
     * 
     */
    public CustomStatusBar() {
        if (CustomStatusBar.statusBar == null)
            CustomStatusBar.statusBar = this;
    }

    

    @Override
    protected Skin<?> createDefaultSkin() {
        return new NoDataStatusBarSkin(this);
    }



    public static CustomStatusBar mainStatusBar() throws IllegalStateException {
        if (CustomStatusBar.statusBar == null)
            throw new IllegalStateException("The principal status bar isn't initialized");
        return CustomStatusBar.statusBar;
    }

    public static <T extends BaseStatusBarSkin> void registerDataSkin(Class<T> skin, Class<?> dataObject) {
        if (CustomStatusBar.USER_DATA_SKIN_REGISTRY.containsKey(dataObject))
            return;
        CustomStatusBar.USER_DATA_SKIN_REGISTRY.put(dataObject, skin);
    }

    @Override
    public void setUserData(Object value) {
        var oldValue = this.getUserData();
        super.setUserData(value);
        if (value == null) {
            this.setSkin(new NoDataStatusBarSkin(this));
        } else if (oldValue == null || !value.getClass().equals(oldValue.getClass())) {
            var newSkinClass = CustomStatusBar.USER_DATA_SKIN_REGISTRY.getOrDefault(value.getClass(),
                    NoDataStatusBarSkin.class);
            try {
                var constructor = newSkinClass.getConstructor(CustomStatusBar.class);
                this.setSkin(constructor.newInstance(this));
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException e) {
                System.err.println("The skin of the CustomStatusBar don't have the required public constructor");
                super.setUserData(null);
                this.setSkin(new NoDataStatusBarSkin(this));
            }
        } else {
            if (this.getSkin() instanceof BaseStatusBarSkin statusSkin)
                statusSkin.updateSkin();
        }
    }

}
