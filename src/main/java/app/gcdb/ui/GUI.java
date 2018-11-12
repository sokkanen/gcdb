package app.gcdb.ui;

import app.gcdb.dao.UserDao;
import app.gcdb.database.Database;
import app.gcdb.domain.User;
import java.sql.SQLException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GUI extends Application {

    private Button loginButton;
    private Button toNewUserWindow;
    private Button backToLoginView;
    private Button logOutFromProgram;
    private UserDao userdao;
    private Database database;

    public GUI() {
        this.loginButton = new Button();
        this.toNewUserWindow = new Button();
        this.backToLoginView = new Button();
        this.logOutFromProgram = new Button();
        this.database = new Database("jdbc:sqlite:gcdb.db");
        this.userdao = new UserDao(database);
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
        Label info = new Label();
        info.setText("Press <ENTER> in the password field to create a new user");
        info.setTextFill(Color.rgb(66, 244, 244));
        BorderPane newUserWindow = new BorderPane();
        HBox signInElements = new HBox();
        this.backToLoginView.setText("Back");
        this.backToLoginView.setStyle("-fx-background-color: #DE5865;");
        VBox usernameAndPassword = new VBox();
        Text enterusername = new Text("Please enter username");
        Text enterpassword = new Text("Please enter password");
        TextField username = new TextField();
        username.setPromptText("username");
        PasswordField password = new PasswordField();
        password.setPromptText("password");

        password.setOnAction((event) -> {
            try {
                User user = new User(username.getText(), password.getText());
                boolean doesNotExist = false;
                boolean emptyValue = false;
                if (username.getText().equals("") || password.getText().equals("")) {
                    emptyValue = true;
                } else {
                    doesNotExist = userdao.save(user);
                }
                if (doesNotExist) {
                    info.setText("New user '" + username.getText() + "' was created");
                    info.setTextFill(Color.rgb(66, 244, 69));
                    username.clear();
                    password.clear();
                } else if (emptyValue) {
                    info.setText("Username or password empty");
                    info.setTextFill(Color.rgb(195, 25, 25));
                } else {
                    info.setText("Please choose another username");
                    info.setTextFill(Color.rgb(195, 25, 25));
                    username.clear();
                    password.clear();
                }
            } catch (SQLException ex) {

            }
        });

        usernameAndPassword.getChildren().addAll(enterusername, username, enterpassword, password, info);
        signInElements.getChildren().addAll(backToLoginView);

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
