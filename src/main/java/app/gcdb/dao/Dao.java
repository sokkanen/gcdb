package app.gcdb.dao;

import java.sql.*;
import java.util.List;

/**
 * Tietokantahallintaluokkien perusrakenteen määrittävä rajapinta.
 */
public interface Dao<X, T> {
    
    void createTable() throws SQLException;

    List<X> findAll() throws SQLException;

    X findOne(T arg) throws SQLException;

    boolean save(T arg) throws SQLException;

    boolean delete(T arg) throws SQLException;
}
