package es.anescdev.core.persistence;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author AnesCDev
 */
public class OrderBy {
    private final Queue<OrderByEntry> orderParams;

    public OrderBy() {
        this.orderParams = new LinkedList<>();
    }

    public OrderBy addOrderParameter(String column, Order order) {
        this.orderParams.offer(new OrderByEntry(column, order));
        return this;
    }

    public String build() {
        if (this.orderParams.isEmpty())
            return "";
        StringBuilder sql = new StringBuilder("ORDER BY ");
        for (Iterator<OrderByEntry> it = this.orderParams.iterator(); it.hasNext();) {
            OrderByEntry entry = it.next();
            sql.append(entry.column() + " " + entry.order().name());
            if (it.hasNext())
                sql.append(", ");
        }
        return sql.toString();
    }

    public static enum Order {
        ASC, DESC;
    }

    private static record OrderByEntry(String column, Order order) {
    }
}
