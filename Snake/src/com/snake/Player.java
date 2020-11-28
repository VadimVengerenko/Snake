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
            public void handle(long l) { //игровой цикл
                if (l - startTime >= (double) 90000000 / settings.getSpeed()) { //рассчитывается интервал времени между предыдущим и текущим кадрами игры и проверяется его
                                                                                //соответствие выбранной в настройках скорости
                    if (restart || returnToMenu) { //произошел рестарт или возврат в меню
                        //прервать игровой цикл
                        timer.stop();
                        return;
                    }
                    if (settings.getNumOfPlayers() == 2) { //если в настройках выбран пункт 2 игрока
                        snake.move(player2Snake); //змейка двигается на один шаг в соответствии с выбранным направлением
                    } else {
                        snake.move(null);
                    }
                    if (snake.getCollision()) { //если змея столкнулась со стеной или змеей другого игрока
                        finishGame(root);
                        if (settings.getNumOfPlayers() == 2) {
                            openRecords(root, settings, Math.max(player2Snake.getFruitsEaten(), snake.getFruitsEaten())); //записывается рекорд лучшего из двух игроков
                        } else {
                            openRecords(root, settings, snake.getFruitsEaten());
                        }
                        return;
                    }
                    if (settings.getNumOfPlayers() == 2) {
                        player2Snake.move(snake);
                    }
                    snake.displaySnake(); //отобразить текущее положение змейки первого игрока
                    if (settings.getNumOfPlayers() == 2) {
                        if (player2Snake.getCollision()) {
                            finishGame(root);
                            openRecords(root, settings, Math.max(player2Snake.getFruitsEaten(), snake.getFruitsEaten()));
                            return;
                        }
                    }
                    if (settings.getNumOfPlayers() == 2) {
                        player2Snake.displaySnake(); //отобразить текущее положение второго игрока, если если выбран пункт 2 игрока в настройках
                    }
                    if (snake.getEat()) {
                        snake.grow(); //если змея первого игрока съела бонус или фрукт, то она вырастает на 1 фрагмент
                    }
                    if (settings.getNumOfPlayers() == 2) {
                        if (player2Snake.getEat()) {
                            player2Snake.grow();
                        }
                    }
                    if (fruit.getGenerate()) {
                        fruit.generateCoords(snake, player2Snake); //сгенерировать новые координаты для фрукта
                        fruit.setGenerate(false);
                    }
                    if (bonus.getGenerate()) {
                        if ((int) (Math.random() * 100) == 15) { //бонус появляется в случайный момент времени
                            bonus.generateCoords(snake, player2Snake, fruit); //сгенерировать новые координаты для бонуса
                            bonus.setGenerate(false);
                        }
                    }
                    if (!bonus.getGenerate()) {
                        bonus.countBonusTimeout(); //отсчитывается таймаут нахождения бонуса на карте
                    }
                    if (!fruit.compareHeadCoords(snake.getX(0), snake.getY(0))) { //если змея первого игрока головой попала в фрукт
                        snake.eat(fruit, null);
                        graphicsContext.drawImage(mapSprites, 0, 0, score.getText().length() * 6.5, 9, 32, 11.5, score.getText().length() * 6.5, 9);
                    }
                    if (settings.getNumOfPlayers() == 2) {
                        if (!fruit.compareHeadCoords(player2Snake.getX(0), player2Snake.getY(0))) {
                            player2Snake.eat(fruit, null); //съесть фрукт
                            graphicsContext.drawImage(mapSprites, 0, 0, score.getText().length() * 6.5, 9, 32, 11.5, score.getText().length() * 6.5, 9);
                        }
                    }
                    if (!bonus.compareHeadCoords(snake.getX(0), snake.getY(0)) && !bonus.getGenerate()) { //если змея первого игрока головой попала в бонус
                        snake.eat(null, bonus); //съесть бонус
                        graphicsContext.drawImage(mapSprites, 0, 0, score.getText().length() * 6.5, 9, 32, 11.5, score.getText().length() * 6.5, 9);
                    }
                    if (settings.getNumOfPlayers() == 2) {
                        if (!bonus.compareHeadCoords(player2Snake.getX(0), player2Snake.getY(0)) && !bonus.getGenerate()) {
                            player2Snake.eat(null, bonus);
                            graphicsContext.drawImage(mapSprites, 0, 0, score.getText().length() * 6.5, 9, 32, 11.5, score.getText().length() * 6.5, 9);
                        }
                    }
                    //выводится текущий счет игры
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
        startTime = System.nanoTime(); //время начала отсчета
        this.graphicsContext = graphicsContext;
        snake = new Snake(graphicsContext, startPosX, startPosY, tileMap, settings);
        //создается поле для вывода счета и его текущее значение выводится на экран
        score = new Label();
        score.setText("SCORE: " + snake.getFruitsEaten());
        timer.start(); //запуск игрового цикла
    }

    public void finishGame(Group root) {
        timer.stop(); //завершить игровой цикл
        //создается текст GAME OVER с заданными параметрами расположения, шрифта и цвета
        Label gameOver = new Label("GAME OVER");
        gameOver.setTextFill(Color.RED);
        gameOver.setFont(Font.font(30));
        gameOver.setTranslateX(558);
        gameOver.setTranslateY(343);
        root.getChildren().add(gameOver); //текст GAME OVER добавляется на экран
    }

    public void openRecords(Group root, Settings settings, int score) {
        Records records = new Records(menu, settings, root, score); //открывается файл с рекордами и выводится поле ввода имени игрока
        records.setRecordsEvents(); //устанавливается обработчик событий на поле ввода имени игрока
    }

    public void setReturnToMenu(boolean value) {
        returnToMenu = value;
    }

    public void setPlayerEvents() {
        //устанавливаются обработчики событий нажатия на клавиши для определения текущего направления змейки первого игрока
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
        player2Snake = new Snake(graphicsContext, startPosX, startPosY, tileMap, settings); //создается змейка второго игрока
    }

    public void setPlayer2Events() {
        //аналогично устанавливаются обработчики событий нажатия на клавиши для определения текущего направления змейки второго игрока
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