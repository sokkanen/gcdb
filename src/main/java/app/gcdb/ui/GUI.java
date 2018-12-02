package app.gcdb.ui;

import app.gcdb.domain.Game;
import app.gcdb.domain.GcdbService;
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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * GUI on ohjelman pääluokka. GUI vastaa graafisen käyttöliittymän luomisesta ja
 * esittämisestä. GUI käyttää DAO-pakkauksen luokkia tiedon hakemiseen ja
 * tallentamiseen.
 */
// TODO: 2. Alustan poisto poistaa myös pelit. 3. Ohjelma nätimmäksi 4. Jos aikaa, niin Wikimedia API? 5. Jos älyttömästi aikaa, niin valittavat kategoriat ja keräilykohteet.
public class GUI extends Application {

    private Button toNewUserWindow;
    private Button backToLoginView;
    private Button logOut;
    private Button login;
    private GcdbService gcdbService;
    private Image kuva;

    @Override
    public void init() {
        this.toNewUserWindow = new Button();
        this.backToLoginView = new Button();
        this.logOut = new Button();
        this.logOut.setText("LOGOUT");
        this.logOut.setStyle("-fx-background-color: #DE5865;");
        this.login = new Button();
        this.gcdbService = new GcdbService();
        this.kuva = new Image("/tausta_bw1.jpeg");
    }

    /* Painiketoiminnot määritelty start-metodissa, koska vaikuttavat suoraan
    Stage-olioon.
     */
    @Override
    public void start(Stage stage) throws SQLException {
        stage.setTitle("Game Collector's Database");
        stage.setWidth(1000);
        stage.setHeight(800);
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
            if (!gcdbService.logIn(new User(username.getText(), password.getText(), 1))) {
                label.setText("User not found or wrong password");
                label.setTextFill(Color.rgb(195, 25, 25));
            } else {
                label.setText("Welcome " + username.getText() + "! Press <LOGIN> to start.");
                label.setTextFill(Color.rgb(66, 244, 69));
                signInElements.getChildren().addAll(this.login, this.logOut);
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
        Text enterusername = new Text("Please enter username (Username must be at least 4 characters long)");
        Text enterpassword = new Text("Please enter password (Password must be at least 5 characters long)");
        TextField username = new TextField();
        username.setMaxWidth(200);
        username.setPromptText("username");
        PasswordField password = new PasswordField();
        password.setMaxWidth(200);
        password.setPromptText("password");

        password.setOnAction((event) -> {
            int cases = gcdbService.createNewUser(new User(username.getText(), password.getText(), 1));
            switch (cases) {
                case 1:
                    info.setText("Username or password too short");
                    info.setTextFill(Color.rgb(195, 25, 25));
                    break;
                case 2:
                    info.setText("Please choose another username");
                    info.setTextFill(Color.rgb(195, 25, 25));
                    username.clear();
                    password.clear();
                    break;
                case 3:
                    info.setText("New user '" + username.getText() + "' was created");
                    info.setTextFill(Color.rgb(66, 244, 69));
                    username.clear();
                    password.clear();
                    break;
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

        mainWindow.setBackground(new Background(new BackgroundImage(kuva, BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT))); // TESTITESTI

        ListView<String> gameList = new ListView();
        Label gameLabel = new Label("Games");
        gameLabel.setFont(Font.font("Cambria", 20));
        gameList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (gameList.getSelectionModel().getSelectedIndex() >= 0) {
                    BorderPane gw = gameView(gcdbService.getGameByIndex(gameList.getSelectionModel().getSelectedIndex()));
                    mainWindow.setBottom(gw);
                }
            }
        });
        ListView<String> platformList = new ListView();
        Label platformLabel = new Label("Platforms:");
        platformLabel.setFont(Font.font("Cambria", 20));
        Button newPlatformButton = new Button("Click here to add");
        Label addNewPlatformLabel = new Label("Add a new platform");
        addNewPlatformLabel.setPadding(new Insets(10));
        Button removePlatformButton = new Button("Remove selected platform");
        TextField newPlatformField = new TextField();
        newPlatformField.setPromptText("new platform");
        removePlatformButton.setOnAction((event -> {
            platformList.setItems(FXCollections.observableArrayList(gcdbService.deletePlatform(gcdbService.getCurrentlySelectedPlatform())));
        }));
        newPlatformButton.setOnAction((event -> {
            platformList.setItems(FXCollections.observableArrayList(gcdbService.saveNewPlatform(new Platform(newPlatformField.getText(), 0))));
            newPlatformField.clear();
        }));
        platformList.setPrefHeight(300);
        platformList.setPrefWidth(300);
        ObservableList<String> platforms = FXCollections.observableArrayList(gcdbService.getUsersPlatformList());
        VBox platformElements = new VBox();
        HBox topElements = new HBox();
        platformElements.getChildren().addAll(platformLabel, platformList, addNewPlatformLabel, newPlatformField, newPlatformButton, removePlatformButton);
        topElements.getChildren().addAll(this.logOut, platformElements);
        platformList.setItems(platforms);

        Button newGameButton = new Button("Add a game");
        Button removeGameButton = new Button("Remove selected game");
        ObservableList<String> gameCondition = FXCollections.observableArrayList("Not defined", "Very Poor", "Poor", "Fair", "Good", "Very good", "Excellent", "Mint", "Near mint", "Gem mint");
        ComboBox gameConditionDrop = new ComboBox(gameCondition);
        ObservableList<String> gameContent = FXCollections.observableArrayList("Not defined", "C", "CI", "CB", "CIB", "NIB");
        ComboBox gameContentDrop = new ComboBox(gameContent);
        Label addNewGameLabel = new Label("Add a new game:");
        addNewGameLabel.setPadding(new Insets(10));
        TextField newGameTextField = new TextField();
        newGameTextField.setPromptText("title");
        TextField newGameCommentField = new TextField();
        newGameCommentField.setPromptText("notes");
        newGameButton.setOnAction((event -> {
            if (gcdbService.platformIsSelected() && gameConditionDrop.getValue() != null && gameContentDrop.getValue() != null) {
                gameList.setItems(FXCollections.observableArrayList(gcdbService.saveNewGame(new Game(newGameTextField.getText(), gcdbService.conditionToInt(gameConditionDrop.getValue().toString()), gcdbService.contentToInt(gameContentDrop.getValue().toString()), newGameCommentField.getText()))));
                newGameTextField.clear();
                newGameCommentField.clear();
                gameLabel.setText(gcdbService.getLoggedInUser() + "'s " + platformList.getSelectionModel().getSelectedItem() + " - games. Total: " + gcdbService.loggedInUsersGameCountForPlatform());
            }
        }));
        removeGameButton.setOnAction((event -> {
            if (gcdbService.platformIsSelected() && gameList.getSelectionModel().getSelectedIndex() >= 0) {
                gameList.setItems(FXCollections.observableArrayList(gcdbService.deleteGame(new Game(gameList.getSelectionModel().getSelectedIndex()))));
                gameLabel.setText(gcdbService.getLoggedInUser() + "'s " + platformList.getSelectionModel().getSelectedItem() + " - games. Total: " + gcdbService.loggedInUsersGameCountForPlatform());
            }
        }));
        platformList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (platformList.getSelectionModel().getSelectedItem() != null) {
                    gameList.setItems(FXCollections.observableArrayList(gcdbService.getPlatformsGames(platformList.getSelectionModel().getSelectedItem())));
                    gameLabel.setText(gcdbService.getLoggedInUser() + "'s " + platformList.getSelectionModel().getSelectedItem() + " - games. Total: " + gcdbService.loggedInUsersGameCountForPlatform());
                }
            }
        });

        GridPane AddgameElements = new GridPane();
        Label title = new Label("Title:");
        Label condition = new Label("Condition:");
        Label content = new Label("Content:");
        Label comments = new Label("Comment:");
        VBox gameElements = new VBox();
        gameList.setPrefHeight(300);
        gameList.setPrefWidth(450);
        ColumnConstraints addGameCol0 = new ColumnConstraints(100);
        ColumnConstraints addGameCol1 = new ColumnConstraints(350);
        AddgameElements.getColumnConstraints().addAll(addGameCol0, addGameCol1);
        AddgameElements.add(title, 0, 0);
        AddgameElements.add(newGameTextField, 1, 0);
        AddgameElements.add(condition, 0, 1);
        AddgameElements.add(gameConditionDrop, 1, 1);
        AddgameElements.add(content, 0, 2);
        AddgameElements.add(gameContentDrop, 1, 2);
        AddgameElements.add(comments, 0, 3);
        AddgameElements.add(newGameCommentField, 1, 3);
        gameElements.getChildren().addAll(gameLabel, gameList, addNewGameLabel, AddgameElements, newGameButton, removeGameButton);
        gameElements.setPadding(new Insets(0, 10, 15, 10));
        platformElements.setPadding(new Insets(0, 10, 15, 10));
        mainWindow.setRight(gameElements);
        mainWindow.setLeft(topElements);
        mainWindow.setPadding(new Insets(15, 15, 15, 15));
        Scene mainScene = new Scene(mainWindow);
        return mainScene;
    }

    // Rakenna
    public BorderPane gameView(Game game) {
        BorderPane gameView = new BorderPane();
        gameView.setBackground(Background.EMPTY);
        HBox additionalInfo = new HBox();
        additionalInfo.setBackground(Background.EMPTY);
        additionalInfo.setSpacing(50);
        additionalInfo.setAlignment(Pos.CENTER);
        gameView.setPrefSize(500, 200);
        Label condition = new Label("Game condition: " + gcdbService.intToCondition(game.getCondition()));
        Label contents = new Label("Game contents: " +  gcdbService.intToContent(game.getContent()));
        contents.setFont(Font.font("Cambria", 25));
        condition.setFont(Font.font("Cambria", 25));
        additionalInfo.getChildren().addAll(condition, contents);
        Label name = new Label(gcdbService.getCurrentlySelectedPlatform().getName() + ": " + game.getName());
        name.setFont(Font.font("Cambria", 32));
        Label comments = new Label(game.getComment());
        comments.setFont(Font.font("Cambria", 18));
        comments.wrapTextProperty();
        comments.setPrefWidth(600);
        gameView.setTop(name);
        gameView.setCenter(additionalInfo);
        gameView.setBottom(comments);
        return gameView;
    }

    @Override
    public void stop() {
        gcdbService.stop();
    }

    public static void main(String[] args) throws SQLException {
        launch(args);
    }
}
