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

/** GUI-olio on ohjelman pääluokka. 
* GUI vastaa graafisen käyttöliittymän luomisesta ja esittämisestä.
* GUI käyttää DAO-pakkauksen luokkia tiedon hakemiseen ja tallentamiseen.
*/

public class GUI extends Application {

    private Button toNewUserWindow;
    private Button backToLoginView;
    private Button logOut;
    private Button login;
    private UserDao userdao;
    private Database database;
    private User loggedInUser;

    @Override
    public void init() {
        this.toNewUserWindow = new Button();
        this.backToLoginView = new Button();
        this.logOut = new Button();
        this.login = new Button();
        this.database = new Database("jdbc:sqlite:gcdb.db");
        this.userdao = new UserDao(database);
    }

    
    /* Painiketoiminnot määritelty start-metodissa, koska vaikuttavat suoraan
    Stage-olioon.
    */ 
    
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Game Collector's Database");
        stage.setWidth(600);
        stage.setHeight(600);
        stage.setScene(loadLoginWindow());
        stage.show();
        toNewUserWindow.setOnAction((event) -> {
            stage.setScene(loadNewUserWindow());
        });
        backToLoginView.setOnAction((event) -> {
            stage.setScene(loadLoginWindow());
        });
        login.setOnAction((event) -> {
            stage.setScene(loadMainWindow());
        });
        logOut.setOnAction((event) -> {
            this.loggedInUser = null;
            stage.setScene(loadLoginWindow());
        });
    }

    /* Ensimmäinen näkymä -> Login */
    public Scene loadLoginWindow() {
        BorderPane signInWindow = new BorderPane();
        HBox signInElements = new HBox();
        this.toNewUserWindow.setText("Create new user");
        this.toNewUserWindow.setStyle("-fx-background-color: #70A4DF;");
        this.login.setText("LOGIN");
        this.login.setStyle("-fx-background-color: #42E621;");
        this.logOut.setText("LOGOUT");
        this.logOut.setStyle("-fx-background-color: #DE5865;");
        VBox usernameAndPassword = new VBox();
        Text enterusername = new Text("Please enter username and password or create a new user");
        TextField username = new TextField();
        username.setPromptText("username");
        Label label = new Label();
        label.setText("Press <ENTER> to enter credentials");
        label.setTextFill(Color.rgb(236, 236, 34));
        PasswordField password = new PasswordField();
        password.setPromptText("password");
        password.setOnAction((event) -> {
            User entered = new User(username.getText(), password.getText());
            try {
                User fromDataBase = (User) userdao.findOne(entered);
                if (fromDataBase == null) {
                    label.setText("User not found");
                    label.setTextFill(Color.rgb(195, 25, 25));
                } else if (fromDataBase.getPassHash() != entered.getPassHash()) {
                    label.setText("Wrong password");
                    label.setTextFill(Color.rgb(195, 25, 25));
                } else {
                    this.loggedInUser = fromDataBase;
                    label.setText("Welcome " + fromDataBase.getUsername() + "! Press <LOGIN> to start.");
                    label.setTextFill(Color.rgb(66, 244, 69));
                    signInElements.getChildren().addAll(this.login, this.logOut);
                }
            } catch (SQLException error) {
                System.out.println(error.getMessage());
            }
            password.clear();
            username.clear();
        });

        usernameAndPassword.getChildren().addAll(enterusername, username, password, label);
        signInElements.getChildren().add(toNewUserWindow);

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

    
    /*Uuden käyttäjän luominen */
    public Scene loadNewUserWindow() {
        Label info = new Label();
        info.setText("Press <ENTER> in the password field to create a new user");
        info.setTextFill(Color.rgb(236, 236, 34));
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
                System.out.println(ex.getMessage());
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

    public Scene loadMainWindow() {
        BorderPane mainWindow = new BorderPane();
        this.logOut.setText("LOGOUT");
        this.logOut.setStyle("-fx-background-color: #DE5865;");
        mainWindow.setBottom(this.logOut);
        Scene mainScene = new Scene(mainWindow);
        return mainScene;
    }

    
    
    public static void main(String[] args) throws SQLException {
        launch(args);
    }
    
    // Sulkee tietokannan. Ei vielä ilmoita, että ohjelman voi sulkea.
    @Override
    public void stop() {
        try {
            database.closeConnection();
        } catch (SQLException e) {
            System.out.println("error closing database");
        }
        
    }
}
