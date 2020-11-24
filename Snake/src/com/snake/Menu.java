package com.snake;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Menu {
    private Stage stage;
    private Button playButton,
            settingsButton,
            recordsButton,
            exitButton;
    private Settings settings;

    public Menu(Stage stage) {
        settings = new Settings(this);
        this.stage = stage;
    }

    public void displayMenu() {
        Label name = new Label("ЗМЕЙКА");
        playButton = new Button("ИГРАТЬ");
        settingsButton = new Button("НАСТРОЙКИ");
        recordsButton = new Button("РЕКОРДЫ");
        exitButton = new Button("ВЫЙТИ");
        name.setFont(Font.font(30));
        name.setTextFill(Color.GOLD);
        playButton.setMaxWidth(100);
        settingsButton.setMaxWidth(100);
        recordsButton.setMaxWidth(100);
        exitButton.setMaxWidth(100);
        playButton.setFocusTraversable(false);
        settingsButton.setFocusTraversable(false);
        recordsButton.setFocusTraversable(false);
        exitButton.setFocusTraversable(false);
        VBox menuVBox = new VBox(name, playButton, settingsButton, recordsButton, exitButton);
        menuVBox.setMinWidth(stage.getWidth());
        menuVBox.setMinHeight(stage.getHeight());
        menuVBox.setSpacing(15);
        menuVBox.setAlignment(Pos.CENTER);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(Main.class.getResourceAsStream("snake_icon.png")),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        menuVBox.setBackground(new Background(backgroundImage));
        Scene menuScene = new Scene(menuVBox);
        stage.setScene(menuScene);
        menuScene.setFill(Color.LIGHTGREEN);
        stage.show();
    }

    public void setMenuEvents() {
        EventHandler<MouseEvent> playButtonEventHandler = mouseEvent -> {
            Game game = new Game(getMenu(), settings);
            game.displayGameField();
            game.setGameEvents();
        };
        playButton.setOnMouseClicked(playButtonEventHandler);
        EventHandler<MouseEvent> settingsButtonEventHandler = mouseEvent -> {
            settings.displaySettings();
            settings.setSettingsEvents();
        };
        settingsButton.setOnMouseClicked(settingsButtonEventHandler);
        EventHandler<MouseEvent> recordsButtonEventHandler = mouseEvent -> {
            Records records = new Records(getMenu());
            records.setRecordsMenuEvents();
        };
        recordsButton.setOnMouseClicked(recordsButtonEventHandler);
        EventHandler<MouseEvent> exitButtonEventHandler = mouseEvent -> Platform.exit();
        exitButton.setOnMouseClicked(exitButtonEventHandler);
    }

    public Stage getStage() {
        return stage;
    }

    public Menu getMenu() {
        return this;
    }
}