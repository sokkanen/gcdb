package app.gcdb.database;

import java.sql.*;

/**
 * Luokka hallinnoi tietokantayhteyksiä.
 */
public class Database {

    private String sqlAddress;
    private Connection connection;

    /**
     * Luo uuden Database-olion.
     *
     * @param address Käytettävän tietokannan osoite.
     *
     */
    public Database(String address) {
        this.sqlAddress = address;
    }

    /**
     * Luo uuden tietokantayhteyden konstruktorin saamaan osoitteeseen.
     *
     * @return Palauttaa uuden yhteyden.
     *
     * @throws SQLException virhe tietokantayhteydessä
     */
    public Connection newConnection() throws SQLException {
        this.connection = DriverManager.getConnection(sqlAddress);
        return this.connection;
    }

    /**
     * Katkaisee aikaisemmin luodun tietokantayhteyden.
     *
     * @throws SQLException virhe tietokantayhteydessä
     *
     */
    public void closeConnection() throws SQLException {
        if (this.connection != null) {
            this.connection.close();
        }
    }
}
