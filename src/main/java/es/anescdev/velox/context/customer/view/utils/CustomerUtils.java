package es.anescdev.velox.context.customer.view.utils;

import es.anescdev.velox.context.customer.CustomerModule;
import es.anescdev.velox.context.customer.model.entities.Customer;
import es.anescdev.velox.core.bus.Bus;
import es.anescdev.velox.core.view.TabManager;

import lombok.experimental.UtilityClass;

@UtilityClass
/**
 * Utilidad de la capa de vista del dominio customer (conversión de datos para la UI,
 * apertura de pantallas relacionadas, eventos de edición en tablas, etc.).
 */
public class CustomerUtils {
    public void openCustomerDetails(TabManager tabManager, Customer customer) {
        var data = new Bus();
        data.setData(TabManager.TITLE_KEY, "%customer.title.details - " + customer.getName());
        data.setData(TabManager.SCENE_KEY, "customer/details");
        data.setData(CustomerModule.CUSTOMER_KEY, customer);
        tabManager.openTab(CustomerUtils.buildTabId(customer), data);
    }

    public String buildTabId(Customer customer) {
        return "customerDetails" + customer.getId();
    }
}
