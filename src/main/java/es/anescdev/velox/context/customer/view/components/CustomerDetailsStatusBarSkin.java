package es.anescdev.velox.context.customer.view.components;

import es.anescdev.velox.context.customer.view.utils.CustomerDetailsStatusBarData;
import es.anescdev.velox.core.view.components.statusbar.CustomStatusBar;
import es.anescdev.velox.core.view.components.statusbar.StateStatusBarSkin;

/**
 * Componente de interfaz reutilizable del dominio customer (diálogo, celda, "skin" de
 * barra de estado, etc.).
 */
public class CustomerDetailsStatusBarSkin extends StateStatusBarSkin{

    public CustomerDetailsStatusBarSkin(CustomStatusBar control) {
        super(control);
        
        this.updateSkin();
    }

    @Override
    protected void updateSkin() {
        if(this.getSkinnable().getUserData() instanceof CustomerDetailsStatusBarData data) {
            this.state.unbind();

            this.state.set(data.state().getValue());

            this.state.bind(data.state());
        }
    }
    
}
