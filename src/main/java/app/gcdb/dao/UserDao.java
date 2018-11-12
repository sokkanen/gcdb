package app.gcdb.dao;

import app.gcdb.database.Database;
import java.sql.SQLException;
import java.util.List;

public class UserDao implements Dao{
    
    private Database db;
    
    public UserDao(Database db){
        this.db = db;
    }
    
    
    @Override
    public Object findOne(Object arg) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean save(Object arg) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Object arg) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List findAll(Object arg) throws SQLException {
        throw new UnsupportedOperationException("Not implemented on this class.");
    }
}
