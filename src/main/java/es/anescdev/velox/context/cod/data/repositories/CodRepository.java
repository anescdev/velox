package es.anescdev.velox.context.cod.data.repositories;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.anescdev.velox.context.cod.data.dao.CodDao;
import es.anescdev.velox.context.cod.model.entities.Cod;
import es.anescdev.velox.context.cod.model.entities.CodKey;
import es.anescdev.velox.context.customer.data.dao.CustomerDao;
import es.anescdev.velox.context.customer.model.entities.Customer;
import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.core.data.repositories.ORMLiteCompositeKeyAbstractRepository;
import es.anescdev.velox.core.exceptions.SearchEntityException;

/**
 * @author AnesCDev
 */
@Singleton
public class CodRepository extends ORMLiteCompositeKeyAbstractRepository<Cod, Long, CodKey, CodDao> {

    private final CustomerDao customerDao;

    private final String[] COMPOSITE_ID_COLUMNS = new String[] {
            CodKey.COD_COLUMN, CodKey.CUSTOMER_COLUMN,
            CodKey.EMPLOYEE_COLUMN
    };

    private final List<String> EXCLUDED_COLUMNS_SEARCH = List.of(CodKey.COD_COLUMN);

    @Inject
    public CodRepository(CodDao dao, CustomerDao customerDao) {
        super(dao);
        this.customerDao = customerDao;
    }

    @Override
    protected Class<Cod> getEntityClass() {
        return Cod.class;
    }

    @Override
    protected String[] getCompositeIdColumns() {
        return this.COMPOSITE_ID_COLUMNS;
    }

    @Override
    protected String getInternalIdColumn() {
        return CodKey.INTERNAL_ID_COLUMN;
    }

    @Override
    protected List<String> excludedFromSearch() {
        return this.EXCLUDED_COLUMNS_SEARCH;
    }

    public Optional<Cod> searchEntitiesByAbbreviationAndCod(String abbreviation, String cod, Employee employee) {
        var customerQuery = this.customerDao.queryBuilder();
        var query = this.dao.queryBuilder();
        try {
            customerQuery.where().eq(Customer.ABBREVIATION_COLUMN, abbreviation);
            query.leftJoin(customerQuery);
            query.where().eq(CodKey.COD_COLUMN, cod).and().eq(CodKey.EMPLOYEE_COLUMN, employee);
            var codFounded = query.queryForFirst();
            if (codFounded == null)
                return Optional.empty();
            return Optional.of(codFounded);
        } catch (SQLException e) {
            throw new SearchEntityException(e.getMessage());
        }
    }

}
