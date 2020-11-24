package com.snake;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class Game {
    private Menu menu;
    private Map map;
    private Button returnToMenuButton;
    private ImageView restartButton;
    private Player player1;
    private Settings settings;

    public Game(Menu menu, Settings settings) {
        this.settings = settings;
        this.menu = menu;
        restartButton = new ImageView(new Image(Main.class.getResourceAsStream("Sprites/restart_icon.png")));
        map = new Map(settings.getMapPathname());
        returnToMenuButton = new Button("ВЕРНУТЬСЯ В ГЛАВНОЕ МЕНЮ");
        returnToMenuButton.setFocusTraversable(false);
        returnToMenuButton.setLayoutX(700);
        returnToMenuButton.setLayoutY(107.5);
    }

    public void displayGameField() {
        restartButton.setX(624);
        restartButton.setY(112);
        restartButton.setFitWidth(16);
        restartButton.setFitHeight(16);
        Canvas canvas = new Canvas(512, 512);
        canvas.setLayoutX(384);
        canvas.setLayoutY(104);
        Group root = new Group();
        root.getChildren().add(canvas);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        Scene gameScene = new Scene(root, menu.getStage().getWidth(), menu.getStage().getHeight());
        menu.getStage().setScene(gameScene);
        map.drawMap(graphicsContext);
        root.getChildren().addAll(returnToMenuButton, restartButton);
        Fruit fruit = new Fruit(graphicsContext);
        Bonus bonus = new Bonus(graphicsContext);
        player1 = new Player(bonus, fruit, menu, root, graphicsContext, map.getTileMap(), settings, 256, 256);
        player1.setPlayerEvents();
        if (settings.getNumOfPlayers() == 2) {
            player1.createPlayer2(map.getTileMap(), settings, 256, 288);
            player1.setPlayer2Events();
        }
    }

    public void setGameEvents() {
        EventHandler<MouseEvent> restartButtonEventHandler = mouseEvent -> {
            player1.setRestart();
            displayGameField();
        };
        restartButton.setOnMouseClicked(restartButtonEventHandler);
        EventHandler<MouseEvent> returnToMenuButtonEventHandler = mouseEvent -> {
            player1.setReturnToMenu(true);
            menu.displayMenu();
            menu.setMenuEvents();
        };
        returnToMenuButton.setOnMouseClicked(returnToMenuButtonEventHandler);
    }
}