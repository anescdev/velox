package es.anescdev.sumatory.data.dao;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import es.anescdev.sumatory.model.entities.TimeLog;

/**
 * @author AnesCDev
 */
public class TimeLogDao extends BaseDaoImpl<TimeLog, Long> {

    public TimeLogDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, TimeLog.class);
    }

}
