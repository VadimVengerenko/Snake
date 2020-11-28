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
        settings = new Settings(this); //создаются настройки (по умолчанию)
        this.stage = stage;
    }

    public void displayMenu() {
        //создается заголовок меню и определяются его шрифт и цвет
        Label name = new Label("ЗМЕЙКА");
        name.setFont(Font.font(30));
        name.setTextFill(Color.GOLD);
        //создаются кнопки меню и устанавливаются их размеры
        playButton = new Button("ИГРАТЬ");
        settingsButton = new Button("НАСТРОЙКИ");
        recordsButton = new Button("РЕКОРДЫ");
        exitButton = new Button("ВЫЙТИ");
        playButton.setMaxWidth(100);
        settingsButton.setMaxWidth(100);
        recordsButton.setMaxWidth(100);
        exitButton.setMaxWidth(100);
        //отменяется фокусировка на кнопках
        playButton.setFocusTraversable(false);
        settingsButton.setFocusTraversable(false);
        recordsButton.setFocusTraversable(false);
        exitButton.setFocusTraversable(false);
        //создается панель компоновки, в которую добавляются все вышесозданные элементы,
        //а также устанавливаются ее параметры
        VBox menuVBox = new VBox(name, playButton, settingsButton, recordsButton, exitButton);
        menuVBox.setMinWidth(stage.getWidth());
        menuVBox.setMinHeight(stage.getHeight());
        menuVBox.setSpacing(15);
        menuVBox.setAlignment(Pos.CENTER);
        //устанавливается изображение на задний план
        BackgroundImage backgroundImage = new BackgroundImage(new Image(Main.class.getResourceAsStream("snake_icon.png")),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        menuVBox.setBackground(new Background(backgroundImage));
        //создается и устанавливается сцена меню
        Scene menuScene = new Scene(menuVBox);
        stage.setScene(menuScene);
        menuScene.setFill(Color.LIGHTGREEN); //устанавливается цвет сцены
        stage.show();
    }

    public void setMenuEvents() {
        //устанавливаются обработчики событий нажатия на кнопки меню кнопками мыши
        EventHandler<MouseEvent> playButtonEventHandler = mouseEvent -> {
            //создается и отображается игровое поле
            Game game = new Game(getMenu(), settings);
            game.displayGameField();
            game.setGameEvents(); //устанавливаются обработчики событий игрового поля
        };
        playButton.setOnMouseClicked(playButtonEventHandler);
        EventHandler<MouseEvent> settingsButtonEventHandler = mouseEvent -> {
            settings.displaySettings(); //отображается пункт настроек
            settings.setSettingsEvents(); //устанавливаются обработчики событий в пункте настроек
        };
        settingsButton.setOnMouseClicked(settingsButtonEventHandler);
        EventHandler<MouseEvent> recordsButtonEventHandler = mouseEvent -> {
            Records records = new Records(getMenu()); //создается пункт рекордов, в котором отображается таблица рекордов
            records.setRecordsMenuEvents(); //устанавливаются обработчики событий в пункте рекордов
        };
        recordsButton.setOnMouseClicked(recordsButtonEventHandler);
        EventHandler<MouseEvent> exitButtonEventHandler = mouseEvent -> Platform.exit(); //выход
        exitButton.setOnMouseClicked(exitButtonEventHandler);
    }

    public Stage getStage() {
        return stage;
    }

    public Menu getMenu() {
        return this;
    }
}