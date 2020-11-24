package com.snake;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Player {
    private Snake snake;
    private Snake player2Snake;
    private GraphicsContext graphicsContext;
    private long startTime;
    private Label score;
    private Image mapSprites;
    private AnimationTimer timer;
    private boolean restart;
    private boolean returnToMenu;
    Menu menu;

    public Player(Bonus bonus,
           Fruit fruit,
           Menu menu,
           Group root,
           GraphicsContext graphicsContext,
           StringBuilder[] tileMap,
           Settings settings,
           int startPosX,
           int startPosY) {
        returnToMenu = false;
        this.menu = menu;
        restart = false;
        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (l - startTime >= (double) 90000000 / settings.getSpeed()) {
                    if (restart || returnToMenu) {
                        timer.stop();
                        return;
                    }
                    if (settings.getNumOfPlayers() == 2) {
                        snake.move(player2Snake);
                    } else {
                        snake.move(null);
                    }
                    if (snake.getCollision()) {
                        finishGame(root, settings);
                        if (settings.getNumOfPlayers() == 2) {
                            openRecords(root, settings, Math.max(player2Snake.getFruitsEaten(), snake.getFruitsEaten()));
                        } else {
                            openRecords(root, settings, snake.getFruitsEaten());
                        }
                        return;
                    }
                    if (settings.getNumOfPlayers() == 2) {
                        player2Snake.move(snake);
                    }
                    snake.displaySnake();
                    if (settings.getNumOfPlayers() == 2) {
                        if (player2Snake.getCollision()) {
                            finishGame(root, settings);
                            openRecords(root, settings, Math.max(player2Snake.getFruitsEaten(), snake.getFruitsEaten()));
                            return;
                        }
                    }
                    if (settings.getNumOfPlayers() == 2) {
                        player2Snake.displaySnake();
                    }
                    if (snake.getEat()) {
                        snake.grow();
                    }
                    if (settings.getNumOfPlayers() == 2) {
                        if (player2Snake.getEat()) {
                            player2Snake.grow();
                        }
                    }
                    if (fruit.getGenerate()) {
                        fruit.generateCoords(snake, player2Snake);
                        fruit.setGenerate(false);
                    }
                    if (bonus.getGenerate()) {
                        if ((int) (Math.random() * 100) == 15) {
                            bonus.generateCoords(snake, player2Snake, fruit);
                            bonus.setGenerate(false);
                        }
                    }
                    if (!bonus.getGenerate()) {
                        bonus.countBonusTimeout();
                    }
                    if (!fruit.compareHeadCoords(snake.getX(0), snake.getY(0))) {
                        snake.eat(fruit, null);
                        graphicsContext.drawImage(mapSprites, 0, 0, score.getText().length() * 6.5, 9, 32, 11.5, score.getText().length() * 6.5, 9);
                    }
                    if (settings.getNumOfPlayers() == 2) {
                        if (!fruit.compareHeadCoords(player2Snake.getX(0), player2Snake.getY(0))) {
                            player2Snake.eat(fruit, null);
                            graphicsContext.drawImage(mapSprites, 0, 0, score.getText().length() * 6.5, 9, 32, 11.5, score.getText().length() * 6.5, 9);
                        }
                    }
                    if (!bonus.compareHeadCoords(snake.getX(0), snake.getY(0)) && !bonus.getGenerate()) {
                        snake.eat(null, bonus);
                        graphicsContext.drawImage(mapSprites, 0, 0, score.getText().length() * 6.5, 9, 32, 11.5, score.getText().length() * 6.5, 9);
                    }
                    if (settings.getNumOfPlayers() == 2) {
                        if (!bonus.compareHeadCoords(player2Snake.getX(0), player2Snake.getY(0)) && !bonus.getGenerate()) {
                            player2Snake.eat(null, bonus);
                            graphicsContext.drawImage(mapSprites, 0, 0, score.getText().length() * 6.5, 9, 32, 11.5, score.getText().length() * 6.5, 9);
                        }
                    }
                    if (settings.getNumOfPlayers() == 2) {
                        if (player2Snake.getFruitsEaten() > snake.getFruitsEaten()) {
                            score.setText("SCORE: " + player2Snake.getFruitsEaten());
                        } else {
                            score.setText("SCORE: " + snake.getFruitsEaten());
                        }
                    } else {
                        score.setText("SCORE: " + snake.getFruitsEaten());
                    }
                    graphicsContext.fillText(score.getText(), 32, 20.5);
                    startTime = l;
                }
            }
        };
        mapSprites = new Image(Main.class.getResourceAsStream("Sprites/snake.png"));
        startTime = System.nanoTime();
        this.graphicsContext = graphicsContext;
        snake = new Snake(graphicsContext, startPosX, startPosY, tileMap, settings);
        score = new Label();
        score.setText("SCORE: " + snake.getFruitsEaten());
        timer.start();
    }

    public void finishGame(Group root, Settings settings) {
        timer.stop();
        Label gameOver = new Label("GAME OVER");
        gameOver.setTextFill(Color.RED);
        gameOver.setFont(Font.font(30));
        gameOver.setTranslateX(558);
        gameOver.setTranslateY(343);
        root.getChildren().add(gameOver);
    }

    public void openRecords(Group root, Settings settings, int score) {
        Records records = new Records(menu, settings, root, score);
        records.setRecordsEvents();
    }

    public void setReturnToMenu(boolean value) {
        returnToMenu = value;
    }

    public void setPlayerEvents() {
        EventHandler<KeyEvent> playerEventHandler = keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.UP) && (snake.getX(0) != snake.getX(1) || (snake.getX(0) == snake.getX(1) && snake.getY(0) < snake.getY(1)))) {
                snake.setDirection("up");
            }
            if (keyEvent.getCode().equals(KeyCode.DOWN) && (snake.getX(0) != snake.getX(1) || (snake.getX(0) == snake.getX(1) && snake.getY(0) > snake.getY(1)))) {
                snake.setDirection("down");
            }
            if (keyEvent.getCode().equals(KeyCode.RIGHT) && (snake.getY(0) != snake.getY(1) || (snake.getY(0) == snake.getY(1) && snake.getX(0) > snake.getX(1)))) {
                snake.setDirection("right");
            }
            if (keyEvent.getCode().equals(KeyCode.LEFT) && (snake.getY(0) != snake.getY(1) || (snake.getY(0) == snake.getY(1) && snake.getX(0) < snake.getX(1)))) {
                snake.setDirection("left");
            }
        };
        graphicsContext.getCanvas().setFocusTraversable(true);
        graphicsContext.getCanvas().addEventHandler(KeyEvent.KEY_PRESSED, playerEventHandler);
    }

    public void createPlayer2(StringBuilder[] tileMap, Settings settings, int startPosX, int startPosY) {
        player2Snake = new Snake(graphicsContext, startPosX, startPosY, tileMap, settings);
    }

    public void setPlayer2Events() {
        EventHandler<KeyEvent> playerEventHandler = keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.W) && (player2Snake.getX(0) != player2Snake.getX(1) || (player2Snake.getX(0) == player2Snake.getX(1) && player2Snake.getY(0) < player2Snake.getY(1)))) {
                player2Snake.setDirection("up");
            }
            if (keyEvent.getCode().equals(KeyCode.S) && (player2Snake.getX(0) != player2Snake.getX(1) || (player2Snake.getX(0) == player2Snake.getX(1) && player2Snake.getY(0) > player2Snake.getY(1)))) {
                player2Snake.setDirection("down");
            }
            if (keyEvent.getCode().equals(KeyCode.D) && (player2Snake.getY(0) != player2Snake.getY(1) || (player2Snake.getY(0) == player2Snake.getY(1) && player2Snake.getX(0) > player2Snake.getX(1)))) {
                player2Snake.setDirection("right");
            }
            if (keyEvent.getCode().equals(KeyCode.A) && (player2Snake.getY(0) != player2Snake.getY(1) || (player2Snake.getY(0) == player2Snake.getY(1) && player2Snake.getX(0) < player2Snake.getX(1)))) {
                player2Snake.setDirection("left");
            }
        };
        graphicsContext.getCanvas().setFocusTraversable(true);
        graphicsContext.getCanvas().addEventHandler(KeyEvent.KEY_PRESSED, playerEventHandler);
    }

    public void setRestart() {
        restart = true;
    }
}