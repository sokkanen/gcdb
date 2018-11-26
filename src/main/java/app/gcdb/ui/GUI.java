package app.gcdb.ui;

import app.gcdb.dao.PlatformDao;
import app.gcdb.dao.UserDao;
import app.gcdb.database.Database;
import app.gcdb.domain.Platform;
import app.gcdb.domain.User;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * GUI-olio on ohjelman pääluokka. GUI vastaa graafisen käyttöliittymän
 * luomisesta ja esittämisestä. GUI käyttää DAO-pakkauksen luokkia tiedon
 * hakemiseen ja tallentamiseen.
 */
public class GUI extends Application {

    private Button toNewUserWindow;
    private Button backToLoginView;
    private Button logOut;
    private Button login;
    private UserDao userDao;
    private PlatformDao platformDao;
    private Database database;
    private User loggedInUser;
    private Platform currentlySelected;

    @Override
    public void init() {
        this.toNewUserWindow = new Button();
        this.backToLoginView = new Button();
        this.logOut = new Button();
        this.login = new Button();
        this.database = new Database("jdbc:sqlite:gcdb.db");
        this.userDao = new UserDao(database);
    }

    /* Painiketoiminnot määritelty start-metodissa, koska vaikuttavat suoraan
    Stage-olioon.
     */
    @Override
    public void start(Stage stage) throws SQLException {
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
        signInElements.setSpacing(10);
        Button closeProgram = new Button();
        VBox usernameAndPassword = new VBox();
        Label label = new Label();
        closeProgram.setStyle("-fx-background-color: #DE5865;");
        closeProgram.setText("CLOSE PROGRAM");
        closeProgram.setOnAction((event) -> {
            stop();
            label.setText("You can now close the program");
            label.setFont(Font.font("Cambria", 32));
            label.setTextFill(Color.rgb(66, 194, 244));
            signInWindow.getChildren().removeAll(signInElements, usernameAndPassword);
            signInWindow.setCenter(label);
        });
        this.toNewUserWindow.setText("Create new user");
        this.toNewUserWindow.setStyle("-fx-background-color: #70A4DF;");
        this.login.setText("LOGIN");
        this.login.setStyle("-fx-background-color: #42E621;");
        this.logOut.setText("LOGOUT");
        this.logOut.setStyle("-fx-background-color: #DE5865;");
        Text enterusername = new Text("Please enter username and password or create a new user");
        TextField username = new TextField();
        username.setPromptText("username");
        label.setText("Press <ENTER> to enter credentials");
        label.setTextFill(Color.rgb(66, 194, 244));
        PasswordField password = new PasswordField();
        password.setPromptText("password");
        password.setOnAction((event) -> {
            User entered = new User(username.getText(), password.getText(), 1);
            try {
                User fromDataBase = (User) userDao.findOne(entered);
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
        signInElements.getChildren().addAll(toNewUserWindow, closeProgram);

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
                User user = new User(username.getText(), password.getText(), 1);
                boolean doesNotExist = false;
                boolean emptyValue = false;
                if (username.getText().equals("") || password.getText().equals("")) {
                    emptyValue = true;
                } else {
                    doesNotExist = userDao.save(user);
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
        this.platformDao = new PlatformDao(database, loggedInUser);
        try {
            loggedInUser.setPlatforms(platformDao.findAll(null));
        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
        BorderPane mainWindow = new BorderPane();
        ListView<String> gameList = new ListView();
        gameList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Napsautit sitten " + gameList.getSelectionModel().getSelectedItem());
            }
        });
        ListView<String> platformList = new ListView();
        platformList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (platformList.getSelectionModel().getSelectedItem() != null){
                    currentlySelected = loggedInUser.getOneOfUsersPlatforms(platformList.getSelectionModel().getSelectedItem());
                }
            }
        });
        Label platformLabel = new Label("Platforms:");
        Button newPlatformButton = new Button("Add a new platform");
        Button removePlatformButton = new Button("Remove selected platform");
        removePlatformButton.setOnAction((event ->{
            try {
                platformDao.delete(currentlySelected);
                loggedInUser.setPlatforms(platformDao.findAll(null));
                platformList.setItems(FXCollections.observableArrayList(loggedInUser.getViewablePlatforms()));
            } catch (SQLException error) {
                System.out.println(error.getMessage());
            }
        }));
        TextField newPlatformField = new TextField();
        newPlatformField.setPromptText("new platform");
        platformList.setPrefHeight(200);
        platformList.setPrefWidth(200);
        gameList.setPrefHeight(300);
        gameList.setPrefWidth(350);
        ObservableList<String> platforms = FXCollections.observableArrayList(loggedInUser.getViewablePlatforms());
        VBox platformElements = new VBox();
        VBox gameElements = new VBox();
        platformElements.getChildren().addAll(platformLabel, platformList, newPlatformField, newPlatformButton, removePlatformButton);
        gameElements.getChildren().add(gameList);
        newPlatformButton.setOnAction((event -> {
            try {
                Platform toBeSaved = new Platform(newPlatformField.getText(), 0);
                if (!toBeSaved.getName().isEmpty()) {
                    platformDao.save(toBeSaved);
                    newPlatformField.clear();
                    loggedInUser.setPlatforms(platformDao.findAll(null));
                    platformList.setItems(FXCollections.observableArrayList(loggedInUser.getViewablePlatforms()));
                }
            } catch (SQLException error) {
                System.out.println(error.getMessage());
            }
        }));

        platformList.setItems(platforms);
        this.logOut.setText("LOGOUT");
        this.logOut.setStyle("-fx-background-color: #DE5865;");
        mainWindow.setBottom(this.logOut);
        mainWindow.setRight(gameElements);
        mainWindow.setLeft(platformElements);
        Scene mainScene = new Scene(mainWindow);
        return mainScene;
    }

    public static void main(String[] args) throws SQLException {
        launch(args);
    }

    @Override
    public void stop() {
        try {
            database.closeConnection();
        } catch (SQLException e) {
            System.out.println("error closing database");
        }

    }
}
