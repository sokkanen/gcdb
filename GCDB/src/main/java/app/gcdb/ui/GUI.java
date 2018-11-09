package app.gcdb.ui;

import app.gcdb.database.Database;
import java.sql.SQLException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GUI extends Application {

    public static Database database;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Game Collector's Database");
        stage.setWidth(400);
        stage.setHeight(400);
        BorderPane panel = new BorderPane();
        Scene scene = new Scene(panel);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws SQLException {
        database = new Database("jdbc:sqlite:gcdb.db");
        database.newConnection();
        launch(args);
    }

    public void exitProgram() {
        try {
            database.closeConnection();
        } catch (SQLException e) {
            System.out.println("error closing database");
        }
        // Exit ja close database
    }
}
