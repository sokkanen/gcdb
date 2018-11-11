package app.gcdb.ui;

import app.gcdb.database.Database;
import java.sql.SQLException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GUI extends Application {

    public static Database database;
    private Button loginButton;
    private Button toNewUserWindow;
    private Button backToLoginView;
    private Button logOutFromProgram;

    public GUI() {
        this.loginButton = new Button();
        this.toNewUserWindow = new Button();
        this.backToLoginView = new Button();
        this.logOutFromProgram = new Button();
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Game Collector's Database");
        stage.setWidth(600);
        stage.setHeight(600);
        stage.setScene(loadLoginWindow());
        toNewUserWindow.setOnAction((event) -> {
            stage.setScene(loadNewUserWindow());
        });
        backToLoginView.setOnAction((event) -> {
            stage.setScene(loadLoginWindow());
        });
        /*loginButton.setOnAction((event) -> {
            stage.setScene(null); // MAIN WINDOW
        });
        logOutFromProgram.setOnAction((event) -> {
            stage.setScene(loadLoginWindow());
        });*/
        stage.show();
    }

    public static void main(String[] args) throws SQLException {
        database = new Database("jdbc:sqlite:gcdb.db");
        database.newConnection();
        launch(args);
    }

    /* Ensimmäinen näkymä -> Login */
    public Scene loadLoginWindow() {
        BorderPane signInWindow = new BorderPane();
        HBox signInElements = new HBox();
        this.loginButton.setText("Sign In");
        this.loginButton.setStyle("-fx-background-color: #70E124;");
        this.toNewUserWindow.setText("Create new user");
        this.toNewUserWindow.setStyle("-fx-background-color: #70A4DF;");
        VBox usernameAndPassword = new VBox();
        Text enterusername = new Text("Please enter username");
        Text enterpassword = new Text("Please enter password");
        TextField username = new TextField();
        PasswordField password = new PasswordField();

        usernameAndPassword.getChildren().addAll(enterusername, username, enterpassword, password);
        signInElements.getChildren().addAll(loginButton, toNewUserWindow);

        /* Padding ja välit elementeille */
        signInElements.setSpacing(20);
        signInWindow.setPadding(new Insets(20, 20, 20, 20));
        usernameAndPassword.setPadding(new Insets(20, 20, 20, 20));
        usernameAndPassword.setSpacing(20);
        signInWindow.setStyle("-fx-background-color: #6F6C6C;");

        /* Elementit ikkunaan */
        signInWindow.setBottom(signInElements);
        signInWindow.setCenter(usernameAndPassword);
        Scene loginScene = new Scene(signInWindow);
        return loginScene;
    }

    public Scene loadNewUserWindow() {
        BorderPane newUserWindow = new BorderPane();
        HBox signInElements = new HBox();
        this.backToLoginView.setText("Back");
        this.backToLoginView.setStyle("-fx-background-color: #DE5865;");
        Button newUser = new Button("Create user");
        newUser.setStyle("-fx-background-color: #70E124;");
        VBox usernameAndPassword = new VBox();
        Text enterusername = new Text("Please enter username");
        Text enterpassword = new Text("Please enter password");
        Text enterpassword2 = new Text("Please re-enter password");
        TextField username = new TextField();
        PasswordField password = new PasswordField();
        PasswordField password2 = new PasswordField();

        usernameAndPassword.getChildren().addAll(enterusername, username, enterpassword, password, enterpassword2, password2);
        signInElements.getChildren().addAll(backToLoginView, newUser);

        /* Padding ja välit elementeille */
        signInElements.setSpacing(20);
        newUserWindow.setPadding(new Insets(20, 20, 20, 20));
        usernameAndPassword.setPadding(new Insets(20, 20, 20, 20));
        usernameAndPassword.setSpacing(20);
        newUserWindow.setStyle("-fx-background-color: #6F6C6C;");

        /* Elementit ikkunaan */
        newUserWindow.setBottom(signInElements);
        newUserWindow.setCenter(usernameAndPassword);
        Scene newUserScene = new Scene(newUserWindow);
        return newUserScene;
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
