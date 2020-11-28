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
        restartButton = new ImageView(new Image(Main.class.getResourceAsStream("Sprites/restart_icon.png"))); //спрайт кнопки рестарт
        map = new Map(settings.getMapPathname()); //открывается файл с картой, указанной в настройках
        //создается кнопка возврата в меню и определяется ее расположение на игровом поле
        returnToMenuButton = new Button("ВЕРНУТЬСЯ В ГЛАВНОЕ МЕНЮ");
        returnToMenuButton.setFocusTraversable(false);
        returnToMenuButton.setLayoutX(700);
        returnToMenuButton.setLayoutY(107.5);
    }

    public void displayGameField() {
        //устанавливаются расположение и размеры кнопки рестарт
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
        //создается и устанавливается сцена с игровым полем
        Scene gameScene = new Scene(root, menu.getStage().getWidth(), menu.getStage().getHeight());
        menu.getStage().setScene(gameScene);
        map.drawMap(graphicsContext); //отображение карты
        root.getChildren().addAll(returnToMenuButton, restartButton); //кнопки добавляются на игровое поле
        Fruit fruit = new Fruit(graphicsContext); //создается фрукт
        Bonus bonus = new Bonus(graphicsContext); //создается бонус
        player1 = new Player(bonus, fruit, menu, root, graphicsContext, map.getTileMap(), settings, 256, 256); //создается первый игрок
        player1.setPlayerEvents(); //устанавливаются обработчики событий первого игрока
        if (settings.getNumOfPlayers() == 2) { //если в настройках отмечен пункт 2 игрока
            player1.createPlayer2(map.getTileMap(), settings, 256, 288); //создается второй игрок
            player1.setPlayer2Events(); //устанавливаются обработчики событий второго игрока
        }
    }

    public void setGameEvents() {
        //устанавливаются обработчики событий игрового поля
        EventHandler<MouseEvent> restartButtonEventHandler = mouseEvent -> {
            //рестарт
            player1.setRestart();
            displayGameField(); //заново отображается игровое поле
        };
        restartButton.setOnMouseClicked(restartButtonEventHandler);
        EventHandler<MouseEvent> returnToMenuButtonEventHandler = mouseEvent -> {
            //возврат в меню
            player1.setReturnToMenu(true);
            //отображается меню и устанавливаются его обработчики событий
            menu.displayMenu();
            menu.setMenuEvents();
        };
        returnToMenuButton.setOnMouseClicked(returnToMenuButtonEventHandler);
    }
}