
package app.gcdb.dao;

import java.sql.*;
import java.util.List;

public interface Dao <X, T>{  
    List<X> findAll(T arg) throws SQLException;
    X findOne(T arg) throws SQLException;
    boolean save(T arg) throws SQLException;
    void delete(T arg) throws SQLException;
}
