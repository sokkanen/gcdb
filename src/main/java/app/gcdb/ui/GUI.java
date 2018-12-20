package app.gcdb.ui;

import app.gcdb.domain.Game;
import app.gcdb.domain.GcdbService;
import app.gcdb.domain.Platform;
import app.gcdb.domain.User;
import java.sql.SQLException;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * GUI on ohjelman pääluokka. GUI vastaa graafisen käyttöliittymän luomisesta ja
 * esittämisestä. GUI käyttää GcdbService-luokka käyttöliittymälogiikan
 * toteuttamiseen.
 */
public class GUI extends Application {

    private int height;
    private int width;
    private Button toNewUserWindow;
    private Button backToLoginView;
    private Button logOut;
    private Button login;
    private Button yesButton;
    private Button noButton;
    private GcdbService gcdbService;
    private Image mainBackGround;
    private Image newUserBackGround;
    private Image loginBackGround;
    private Image gameOver;
    private Stage areYouSure;
    private ListView gameList;

    /**
     * Valmistelee luokkamuuttujat GUI:n käytettäväksi.
     */
    @Override
    public void init() {
        this.toNewUserWindow = new Button();
        this.backToLoginView = new Button();
        this.logOut = new Button();
        this.logOut.setText("LogOut");
        this.logOut.setStyle("-fx-background-color: #DE5865;");
        this.login = new Button();
        this.gcdbService = new GcdbService();
        this.mainBackGround = new Image("/tausta_bw1.jpeg");
        this.newUserBackGround = new Image("/oneup.png");
        this.loginBackGround = new Image("/start.png");
        this.gameOver = new Image("/gameover.png");
        this.yesButton = new Button("YES");
        this.yesButton.setStyle("-fx-background-color: #42f44b;");
        this.noButton = new Button("NO");
        this.noButton.setStyle("-fx-background-color: #DE5865;");
        this.height = 820;
        this.width = 1000;
    }

    /**
     * Ohjelman käynnistysmetodi. Hallinnoi ikkunoiden vaihtamista.
     *
     * @param stage Ohjelman pääikkuna
     *
     * @throws SQLException virhe tietokannanhallinnassa
     */
    @Override
    public void start(Stage stage) throws SQLException {
        this.areYouSure = areYouSureWindow();
        stage.setTitle("Game Collector's Database");
        stage.setWidth(width);
        stage.setHeight(height);
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
            areYouSure.show();
            yesButton.setOnAction((yesEvent) -> {
                areYouSure.close();
                stage.setScene(loadLoginWindow());
            });
            noButton.setOnAction((noEvent) -> {
                areYouSure.close();
            });

        });
    }

    /**
     * Sisäänkirjautumisnäkymän palauttava metodi. Rakentaa
     * sisäänkirjautumisnäkymän graafisen ulkoasun.
     *
     * @return Palauttaa Scene-olion.
     */
    public Scene loadLoginWindow() {
        BorderPane signInWindow = new BorderPane();
        signInWindow.setPrefSize(width, height);
        signInWindow.setBackground(new Background(new BackgroundImage(loginBackGround, BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        HBox signInElements = new HBox();
        signInElements.setSpacing(10);
        Button closeProgram = new Button();
        VBox usernameAndPassword = new VBox();
        Label label = new Label();
        closeProgram.setStyle("-fx-background-color: #DE5865;");
        closeProgram.setText("Exit program");
        closeProgram.setOnAction((event) -> {
            stop();
            signInWindow.setBackground(new Background(new BackgroundImage(gameOver, BackgroundRepeat.REPEAT,
                    BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
            label.setText("You can now close the program");
            label.setFont(Font.font("Calibri", 32));
            label.setTextFill(Color.rgb(0, 0, 0));
            signInWindow.getChildren().removeAll(signInElements, usernameAndPassword);
            signInWindow.setCenter(label);

        });
        this.toNewUserWindow.setText("Create a new user");
        this.toNewUserWindow.setStyle("-fx-background-color: #70A4DF;");
        this.login.setText("LOGIN");
        this.login.setStyle("-fx-background-color: #42E621;");
        this.logOut.setText("LOGOUT");
        this.logOut.setStyle("-fx-background-color: #DE5865;");
        Text enterusername = new Text("Please enter username and password or create a new user");
        enterusername.setFont(Font.font("Calibri", 20));
        TextField username = new TextField();
        username.setPromptText("username");
        username.setMaxSize(250, 100);
        label.setText("Press <ENTER> to enter credentials");
        label.setFont(Font.font("Calibri", 20));
        label.setTextFill(Color.rgb(6, 46, 206));
        PasswordField password = new PasswordField();
        password.setPromptText("password");
        password.setMaxSize(250, 100);
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

        signInElements.getChildren().addAll(toNewUserWindow, closeProgram);
        signInElements.setSpacing(20);
        usernameAndPassword.getChildren().addAll(enterusername, username, password, label);
        usernameAndPassword.setPadding(new Insets(20, 20, 20, 30));
        usernameAndPassword.setSpacing(20);
        signInWindow.setPadding(new Insets(20, 20, 20, 20));
        signInWindow.setBottom(signInElements);
        signInWindow.setCenter(usernameAndPassword);
        Scene loginScene = new Scene(signInWindow);
        return loginScene;
    }

    /**
     * Uuden käyttäjän luomisnäkymän palauttava metodi. Rakentaa näkymän
     * graafisen ulkoasun.
     *
     * @return Palauttaa Scene-olion.
     */
    public Scene loadNewUserWindow() {
        Label info = new Label();
        info.setText("Press <ENTER> in the password field to create a new user");
        info.setTextFill(Color.rgb(236, 236, 34));
        info.setFont(Font.font("Calibri", 20));
        BorderPane newUserWindow = new BorderPane();
        newUserWindow.setBackground(new Background(new BackgroundImage(newUserBackGround, BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        HBox signInElements = new HBox();
        this.backToLoginView.setText("Back");
        this.backToLoginView.setStyle("-fx-background-color: #DE5865;");
        VBox usernameAndPassword = new VBox();
        Text enterusername = new Text("Please enter username (Username must be between 5 to 20 characters)");
        Text enterpassword = new Text("Please enter password (Password must be at least 5 characters long)");
        enterusername.setFont(Font.font("Calibri", 20));
        enterpassword.setFont(Font.font("Calibri", 20));
        TextField username = new TextField();
        username.setMaxWidth(250);
        username.setPromptText("username");
        PasswordField password = new PasswordField();
        password.setMaxWidth(250);
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
        signInElements.setSpacing(20);
        newUserWindow.setPadding(new Insets(20, 20, 20, 20));
        usernameAndPassword.setPadding(new Insets(20, 20, 20, 20));
        usernameAndPassword.setSpacing(20);
        newUserWindow.setBottom(signInElements);
        newUserWindow.setCenter(usernameAndPassword);
        Scene newUserScene = new Scene(newUserWindow);
        return newUserScene;
    }

    /**
     * Ohjelman päänäkymän palauttava metodi. Rakentaa näkymän graafisen
     * ulkoasun ja määrittää näkymän painikkeiden ja hiiren painallusten
     * toiminnallisuuden. Alustaa gameList-luokkamuuttujan.
     *
     * @return Palauttaa Scene-olion.
     */
    public Scene loadMainWindow() {
        BorderPane mainWindow = new BorderPane();
        mainWindow.setBackground(new Background(new BackgroundImage(mainBackGround, BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        mainWindow.setPadding(new Insets(15, 15, 15, 15));
        this.gameList = new ListView();
        gameList.setPrefHeight(250);
        gameList.setPrefWidth(450);
        gameList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (gameList.getSelectionModel().getSelectedIndex() >= 0) {
                    BorderPane gw = gameView(gcdbService.getGameByIndex(gameList.getSelectionModel().getSelectedIndex()), mainWindow);
                    mainWindow.setBottom(gw);
                }
            }
        });
        ListView<String> platformList = new ListView();
        platformList.setPrefHeight(250);
        platformList.setPrefWidth(300);
        Label platformLabel = new Label("Platforms:");
        platformLabel.setFont(Font.font("Calibri", 20));
        Button newPlatformButton = new Button("Add a platform");
        Label addNewPlatformLabel = new Label("Add a new platform");
        addNewPlatformLabel.setFont(Font.font("Calibri", 20));
        addNewPlatformLabel.setPadding(new Insets(10));
        Button removePlatformButton = new Button("Remove selected platform");
        TextField newPlatformField = new TextField();
        newPlatformField.setPromptText("new platform");
        Label gameLabel = new Label("Games");
        gameLabel.setFont(Font.font("Calibri", 20));
        removePlatformButton.setOnAction((event -> {
            if (gcdbService.platformIsSelected()) {
                areYouSure.show();
                yesButton.setOnAction((yesEvent) -> {
                    gcdbService.deleteGamesByPlatform(gcdbService.getCurrentlySelectedPlatform());
                    platformList.setItems(FXCollections.observableArrayList(gcdbService.deletePlatform(gcdbService.getCurrentlySelectedPlatform())));
                    gameList.setItems(FXCollections.observableArrayList());
                    gameLabel.setText("Games");
                    mainWindow.setBottom(new BorderPane());
                    areYouSure.close();
                });
                noButton.setOnAction((noEvent) -> {
                    areYouSure.close();
                });
            }
        }));
        newPlatformButton.setOnAction((event -> {
            if (!newPlatformField.getText().equals("")) {
                platformList.setItems(FXCollections.observableArrayList(gcdbService.saveNewPlatform(new Platform(newPlatformField.getText(), 0))));
                newPlatformField.clear();
            }
        }));
        ObservableList<String> platforms = FXCollections.observableArrayList(gcdbService.getUsersPlatformList());
        platformList.setItems(platforms);
        platformList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (platformList.getSelectionModel().getSelectedItem() != null) {
                    gameList.setItems(FXCollections.observableArrayList(gcdbService.getPlatformsGames(platformList.getSelectionModel().getSelectedItem())));
                    gameLabel.setText(gcdbService.getLoggedInUser() + "'s " + platformList.getSelectionModel().getSelectedItem() + " - games. Total: " + gcdbService.loggedInUsersGameCountForPlatform());
                }
            }
        });
        VBox platformElements = new VBox();
        platformElements.setSpacing(5);
        HBox topElements = new HBox();
        platformElements.getChildren().addAll(platformLabel, platformList, addNewPlatformLabel, newPlatformField, newPlatformButton, removePlatformButton);
        platformElements.setPadding(new Insets(0, 10, 15, 10));
        topElements.getChildren().addAll(this.logOut, platformElements);
        Button newGameButton = new Button("Add a game");
        Button removeGameButton = new Button("Remove selected game");
        ObservableList<String> gameCondition = FXCollections.observableArrayList(gcdbService.getConditions());
        ComboBox gameConditionDrop = new ComboBox(gameCondition);
        ObservableList<String> gameContent = FXCollections.observableArrayList(gcdbService.getContents());
        ComboBox gameContentDrop = new ComboBox(gameContent);
        Label addNewGameLabel = new Label("Add a new game:");
        addNewGameLabel.setFont(Font.font("Calibri", 20));
        addNewGameLabel.setPadding(new Insets(10));
        TextField newGameTextField = new TextField();
        newGameTextField.setPromptText("title");
        TextField newGameCommentField = new TextField();
        newGameCommentField.setPromptText("comment (optional)");
        TextField newGameRegionField = new TextField();
        newGameRegionField.setPromptText("region (optional)");
        newGameButton.setOnAction((event -> {
            if (gcdbService.platformIsSelected() && gameConditionDrop.getValue() != null && gameContentDrop.getValue() != null) {
                gameList.setItems(FXCollections.observableArrayList(gcdbService.saveNewGame(new Game(newGameTextField.getText(), gcdbService.conditionToInt(gameConditionDrop.getValue().toString()), gcdbService.contentToInt(gameContentDrop.getValue().toString()), newGameRegionField.getText(), newGameCommentField.getText()))));
                newGameTextField.clear();
                newGameCommentField.clear();
                newGameRegionField.clear();
                gameLabel.setText(gcdbService.getLoggedInUser() + "'s " + platformList.getSelectionModel().getSelectedItem() + " - games. Total: " + gcdbService.loggedInUsersGameCountForPlatform());
            }
        }));
        removeGameButton.setOnAction((event -> {
            if (gcdbService.platformIsSelected() && gameList.getSelectionModel().getSelectedIndex() >= 0) {
                areYouSure.show();
                yesButton.setOnAction((yesEvent) -> {
                    gameList.setItems(FXCollections.observableArrayList(gcdbService.deleteGame(new Game(gameList.getSelectionModel().getSelectedIndex()))));
                    gameLabel.setText(gcdbService.getLoggedInUser() + "'s " + platformList.getSelectionModel().getSelectedItem() + " - games. Total: " + gcdbService.loggedInUsersGameCountForPlatform());
                    BorderPane emptyView = new BorderPane();
                    emptyView.setPrefSize(450, 200);
                    mainWindow.setBottom(emptyView);
                    areYouSure.close();
                });
                noButton.setOnAction((noEvent) -> {
                    areYouSure.close();
                });
            }
        }));

        GridPane AddgameElements = new GridPane();
        AddgameElements.setPadding(new Insets(5));
        Label title = new Label("Title:");
        title.setFont(Font.font("Calibri", 15));
        Label condition = new Label("Condition:");
        condition.setFont(Font.font("Calibri", 15));
        Label content = new Label("Content:");
        content.setFont(Font.font("Calibri", 15));
        Label comments = new Label("Comment:");
        comments.setFont(Font.font("Calibri", 15));
        Label region = new Label("Region:");
        region.setFont(Font.font("Calibri", 15));
        VBox gameElements = new VBox();
        gameElements.setSpacing(5);
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
        AddgameElements.add(region, 0, 4);
        AddgameElements.add(newGameRegionField, 1, 4);
        gameElements.getChildren().addAll(gameLabel, gameList, addNewGameLabel, AddgameElements, newGameButton, removeGameButton);
        gameElements.setPadding(new Insets(0, 10, 15, 10));
        mainWindow.setRight(gameElements);
        mainWindow.setLeft(topElements);
        Scene mainScene = new Scene(mainWindow);
        return mainScene;
    }

    /**
     * Yksittäisen pelin sisältämän BorderPane-olion palauttava metodi. Rakentaa
     * yksittäisen pelin graafisen ulkoasun.
     *
     * @param game näytettävän pelin tiedot
     * @param mainWindow pääikkuna annetaan parametrina, jotta pääikkunaan
     * voidaan asettaa muokkausikkuna
     *
     * @see GUI#gameUpdateView(app.gcdb.domain.Game,
     * javafx.scene.layout.BorderPane)
     * @return Palauttaa BorderPane-olion.
     */
    public BorderPane gameView(Game game, BorderPane mainWindow) {
        BorderPane gameView = new BorderPane();
        gameView.setBackground(Background.EMPTY);
        VBox additionalInfo = new VBox();
        additionalInfo.setBackground(Background.EMPTY);
        additionalInfo.setPadding(new Insets(20, 0, 0, 0));
        additionalInfo.setSpacing(10);
        gameView.setPrefSize(450, 200);
        Label condition = new Label("Game condition: " + gcdbService.intToCondition(game.getCondition()));
        Label contents = new Label("Contents: " + gcdbService.intToContent(game.getContent()));
        Label region = new Label("Region: " + gcdbService.getGameRegion(game));
        contents.setFont(Font.font("Calibri", 20));
        condition.setFont(Font.font("Calibri", 20));
        region.setFont(Font.font("Calibri", 20));
        additionalInfo.getChildren().addAll(condition, contents, region);
        Label name = new Label(gcdbService.getCurrentlySelectedPlatform().getName() + ": " + game.getName());
        name.setFont(Font.font("Calibri", 30));
        Label comments = new Label(game.getComment());
        comments.setFont(Font.font("Calibri", 18));
        comments.wrapTextProperty();
        comments.setPrefWidth(600);
        Button modifyGameButton = new Button("Modify game information");
        modifyGameButton.setStyle("-fx-background-color: #70A4DF;");
        modifyGameButton.setOnAction(event -> {
            BorderPane updateView = gameUpdateView(game, mainWindow);
            mainWindow.setBottom(updateView);
        });
        HBox bottomElements = new HBox();
        bottomElements.getChildren().addAll(comments, modifyGameButton);
        gameView.setTop(name);
        gameView.setLeft(additionalInfo);
        gameView.setBottom(bottomElements);
        return gameView;
    }

    /**
     * Yksittäisen pelin muokkausnäkymän palauttava metodi.
     *
     * @param game Muokattavan pelin tiedot.
     * @param mainWindow pääikkuna annetaan parametrina, jotta pääikkunaan
     * voidaan asettaa muokkausikkuna
     *
     * @return Palauttaa BorderPane-olion.
     */
    public BorderPane gameUpdateView(Game game, BorderPane mainWindow) {
        BorderPane modView = new BorderPane();
        modView.setBackground(Background.EMPTY);
        VBox gameInfo = new VBox();
        gameInfo.setBackground(Background.EMPTY);
        gameInfo.setPadding(new Insets(20, 0, 0, 0));
        gameInfo.setSpacing(10);
        modView.setPrefSize(450, 200);
        Label condition = new Label("Game condition: ");
        Label contents = new Label("Contents: ");
        Label region = new Label("Region: ");
        contents.setFont(Font.font("Calibri", 20));
        ObservableList<String> gameContent = FXCollections.observableArrayList(gcdbService.getContents());
        ComboBox gameContentDrop = new ComboBox(gameContent);
        gameContentDrop.getSelectionModel().select(game.getContent() - 1);
        condition.setFont(Font.font("Calibri", 20));
        ObservableList<String> gameCondition = FXCollections.observableArrayList(gcdbService.getConditions());
        ComboBox gameConditionDrop = new ComboBox(gameCondition);
        gameConditionDrop.getSelectionModel().select(game.getCondition() - 1);
        region.setFont(Font.font("Calibri", 20));
        TextField regionText = new TextField(game.getRegion());
        if (game.getRegion().isEmpty()) {
            regionText.setPromptText("Region");
        }

        HBox conditionBox = new HBox();
        conditionBox.setPadding(new Insets(0, 10, 0, 0));
        conditionBox.getChildren().addAll(condition, gameConditionDrop);
        HBox contentBox = new HBox();
        contentBox.setPadding(new Insets(0, 10, 0, 0));
        contentBox.getChildren().addAll(contents, gameContentDrop);
        HBox regionBox = new HBox();
        regionBox.setPadding(new Insets(0, 10, 0, 0));
        regionBox.getChildren().addAll(region, regionText);
        gameInfo.getChildren().addAll(conditionBox, contentBox, regionBox);
        Label name = new Label(gcdbService.getCurrentlySelectedPlatform().getName() + ": " + game.getName());
        name.setFont(Font.font("Calibri", 30));
        TextField comments = new TextField(game.getComment());
        comments.setPrefWidth(500);
        comments.setFont(Font.font("Calibri", 18));
        if (game.getComment().isEmpty()) {
            comments.setPromptText("Comment");
        }
        Button confirmButton = new Button("Confirm");
        confirmButton.setStyle("-fx-background-color: #42f465;");
        confirmButton.setOnAction(YesEvent -> {
            Game update = new Game(
                    game.getName(), game.getPlatform(), gameConditionDrop.getSelectionModel().getSelectedIndex() + 1,
                    gameContentDrop.getSelectionModel().getSelectedIndex() + 1, game.getId(), regionText.getText(), comments.getText());
            mainWindow.setBottom(gameView(update, mainWindow));
            gameList.setItems(FXCollections.observableArrayList(gcdbService.updateGame(update)));
        });
        HBox bottomElements = new HBox();
        bottomElements.setSpacing(10);
        bottomElements.getChildren().addAll(comments, confirmButton);
        modView.setTop(name);
        modView.setLeft(gameInfo);
        modView.setBottom(bottomElements);
        return modView;
    }

    /**
     * "Oletko varma" -ikkunan rakentava metodi. Toiminnallisuus on toteutettu
     * erillisenä Stage-oliona, joka lukitsee pää-Stagen, kunnes kysymykseen on
     * vastattu "YES" tai "NO".
     *
     * @return Palauttaa Stage-olion.
     */
    public Stage areYouSureWindow() {
        Stage areYouSureStage = new Stage();
        BorderPane areYouSureElements = new BorderPane();
        Label areYouSureLabel = new Label("Are you sure?");
        areYouSureLabel.setFont(Font.font("Calibri", 20));
        areYouSureElements.setPadding(new Insets(10, 70, 15, 70));
        areYouSureElements.setTop(areYouSureLabel);
        BorderPane.setAlignment(areYouSureLabel, Pos.CENTER);
        areYouSureElements.setLeft(yesButton);
        areYouSureElements.setRight(noButton);
        Scene areYouSureScene = new Scene(areYouSureElements);
        areYouSureStage.setWidth(400);
        areYouSureStage.setHeight(150);
        areYouSureStage.setScene(areYouSureScene);
        areYouSureStage.initModality(Modality.APPLICATION_MODAL);
        return areYouSureStage;
    }

    /**
     * Keskeyttää ohjelman suorituksen. Kutsuu GcdbService-luokan stop-metodia.
     *
     * @see GcdbService#stop()
     */
    @Override
    public void stop() {
        gcdbService.stop();
    }

    /**
     * Käynnistää ohjelman.
     *
     * @param args komentoriviparametrit
     *
     * @throws SQLException virhe tietokantayhteydessä
     */
    public static void main(String[] args) throws SQLException {
        launch(args);

    }
}
