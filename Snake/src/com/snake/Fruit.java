package com.snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Fruit {
    private int xCoord;
    private int yCoord;
    private Image fruitSprite;
    private boolean generate = true; //можно ли генерировать координаты фрукта
    private GraphicsContext graphicsContext;

    public Fruit(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
        fruitSprite = new Image(Main.class.getResourceAsStream("Sprites/snake-graphics_small1.png"));
    }

    public void generateCoords(Snake snake, Snake player2Snake) {
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
                if (compareCoords(snake) && compareCoords(player2Snake)) { //если координаты не совпадают с координатами змей игроков
                    int SIZE = 16; //размер спрайта фрукта
                    graphicsContext.drawImage(fruitSprite, 0, 48, SIZE, SIZE, xCoord, yCoord, SIZE, SIZE); //отображение спрайта бонуса
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
                if (compareCoords(snake)) {
                    int SIZE = 16;
                    graphicsContext.drawImage(fruitSprite, 0, 48, SIZE, SIZE, xCoord, yCoord, SIZE, SIZE);
                } else {
                    number--;
                }
            }
        }
    }

    public boolean compareCoords(Snake snake) {
        //сравнение координат фрукта с координатами змеи игрока
        for (int fragmentNumber = 0; fragmentNumber < snake.getSnakeSize(); fragmentNumber++) {
            if (snake.getX(fragmentNumber) > xCoord - 16 && snake.getX(fragmentNumber) < xCoord + 16 && snake.getY(fragmentNumber) > yCoord - 16 && snake.getY(fragmentNumber) < yCoord + 16) {
                return false;
            }
        }
        return true;
    }

    public boolean compareHeadCoords(int headX, int headY) {
        //сравнение координат головы змеи игрока с координатами фрукта
        return headX <= xCoord - 16 || headX >= xCoord + 16 || headY <= yCoord - 16 || headY >= yCoord + 16;
    }

    public boolean getGenerate() {
        return generate;
    }

    public void setGenerate(boolean value) {
        generate = value;
    }

    public int getXCoord() {
        return xCoord;
    }

    public int getYCoord() {
        return yCoord;
    }
}