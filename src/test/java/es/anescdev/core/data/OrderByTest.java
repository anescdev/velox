package es.anescdev.core.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import es.anescdev.core.data.OrderBy;
import es.anescdev.core.data.OrderBy.Order;

/**
 * @author AnesCDev
 */
public class OrderByTest {

    @Test
    public void testReturnEmptyString() {
        OrderBy order = new OrderBy();
        assertEquals("", order.build());
    }

    @Test
    public void testReturnTwoOrderParameterSQL() {
        OrderBy order = new OrderBy()
            .addOrderParameter("column_a", Order.DESC)
            .addOrderParameter("column_b", Order.ASC);
        assertEquals("ORDER BY column_a DESC, column_b ASC", order.build());
    }
}
