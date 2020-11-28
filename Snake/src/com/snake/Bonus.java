package com.snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Bonus {
    private int xCoord;
    private int yCoord;
    private Image bonusSprite;
    private boolean generate = true; //можно ли генерировать координаты бонуса
    private GraphicsContext graphicsContext;
    private int bonusTimeout;
    private final int SIZE = 16;
    private Image mapSprites;

    public Bonus(GraphicsContext graphicsContext) {
        mapSprites = new Image(Main.class.getResourceAsStream("Sprites/snake.png"));
        bonusTimeout = 0;
        this.graphicsContext = graphicsContext;
        bonusSprite = new Image(Main.class.getResourceAsStream("Sprites/snake-graphics_small3.png"));
    }

    public void generateCoords(Snake snake, Snake player2Snake, Fruit fruit) {
        //ограничения координат для генерации
        int upBorder = 32;
        int downBorder = 464;
        int rightBorder = 464;
        int leftBorder = 32;
        int bufCoord;
        for (int number = 0; number < 1; number++) {
            if (player2Snake != null) { //если создан второй игрок
                do {
                    bufCoord = (int) (upBorder + Math.random() * (downBorder - upBorder));
                } while (bufCoord % 16 != 0);
                yCoord = bufCoord; //случайная координата y в заданных пределах
                do {
                    do {
                        bufCoord = (int) (leftBorder + Math.random() * (rightBorder - leftBorder));
                    } while (bufCoord % 16 != 0);
                    xCoord = bufCoord; //случайная координата x в заданных пределах
                } while (snake.getTileMap()[yCoord / 16].charAt(xCoord / 16) == 'W' || player2Snake.getTileMap()[yCoord / 16].charAt(xCoord / 16) == 'W'); //пока не сгенерируется в свободных местах карты
                if (compareCoords(snake) && compareCoords(player2Snake) && xCoord != fruit.getXCoord() && yCoord != fruit.getYCoord()) { //если координаты не совпадают с другими игровыми элементами (фрукт и змеи игроков)
                    graphicsContext.drawImage(bonusSprite, 0, 48, SIZE, SIZE, xCoord, yCoord, SIZE, SIZE); //отображение спрайта бонуса
                } else {
                    number--; //произошло совпадение координат с игровыми элементами
                }
            } else { //все тоже самое, только проверяется совпадение с координатами змеи только первого игрока
                do {
                    bufCoord = (int) (upBorder + Math.random() * (downBorder - upBorder));
                } while (bufCoord % 16 != 0);
                yCoord = bufCoord;
                do {
                    do {
                        bufCoord = (int) (leftBorder + Math.random() * (rightBorder - leftBorder));
                    } while (bufCoord % 16 != 0);
                    xCoord = bufCoord;
                } while (snake.getTileMap()[yCoord / 16].charAt(xCoord / 16) == 'W');
                if (compareCoords(snake) && xCoord != fruit.getXCoord() && yCoord != fruit.getYCoord()) {
                    int SIZE = 16;
                    graphicsContext.drawImage(bonusSprite, 0, 48, SIZE, SIZE, xCoord, yCoord, SIZE, SIZE);
                } else {
                    number--;
                }
            }
        }
    }

    public void countBonusTimeout() {
        //отсчитывается время до исчезновения бонуса с карты
        if (bonusTimeout < 100) {
            bonusTimeout++;
        } else {
            bonusTimeout = 0;
            graphicsContext.drawImage(mapSprites, 33, 33, SIZE, SIZE, xCoord, yCoord, SIZE, SIZE);
            setGenerate(true);
        }
    }

    public void resetBonusTimeout() {
        bonusTimeout = 0; //сброс счетчика таймаута нахождения бонуса на карте
        setGenerate(true);
    }

    public boolean compareCoords(Snake snake) {
        //сравнение координат бонуса с координатами змеи игрока
        for (int fragmentNumber = 0; fragmentNumber < snake.getSnakeSize(); fragmentNumber++) {
            if (snake.getX(fragmentNumber) > xCoord - 16 && snake.getX(fragmentNumber) < xCoord + 16 && snake.getY(fragmentNumber) > yCoord - 16 && snake.getY(fragmentNumber) < yCoord + 16) {
                return false;
            }
        }
        return true;
    }

    public boolean compareHeadCoords(int headX, int headY) {
        //сравнение координат головы змеи игрока с координатами бонуса
        return headX <= xCoord - 16 || headX >= xCoord + 16 || headY <= yCoord - 16 || headY >= yCoord + 16;
    }

    public boolean getGenerate() {
        return generate;
    }

    public void setGenerate(boolean value) {
        generate = value;
    }
}