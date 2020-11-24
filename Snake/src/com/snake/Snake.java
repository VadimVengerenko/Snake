package com.snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class Snake {
    private int snakeSize;
    private ArrayList<Integer> xCoords;
    private ArrayList<Integer> yCoords;
    private String direction;
    private boolean collision;
    private Image snakeSprites;
    private Image mapSprites;
    private int fruitsEaten;
    private final int SIZE = 16;
    private boolean eat;
    private int prevTailX;
    private int prevTailY;
    private GraphicsContext graphicsContext;
    private StringBuilder[] tileMap;

    public Snake(GraphicsContext graphicsContext, int startPosX, int startPosY, StringBuilder[] tileMap, Settings settings) {
        this.tileMap = tileMap;
        this.graphicsContext = graphicsContext;
        eat = false;
        direction = "";
        snakeSize = 3;
        collision = false;
        fruitsEaten = 0;
        xCoords = new ArrayList<>();
        yCoords = new ArrayList<>();
        snakeSprites = new Image(Main.class.getResourceAsStream(settings.getCharacterPath()));
        mapSprites = new Image(Main.class.getResourceAsStream("Sprites/snake.png"));
        xCoords.add(startPosX);
        xCoords.add(startPosX - 16);
        xCoords.add(startPosX - 32);
        yCoords.add(startPosY);
        yCoords.add(startPosY);
        yCoords.add(startPosY);
        graphicsContext.drawImage(snakeSprites, 64, 0, SIZE, SIZE, xCoords.get(0), yCoords.get(0), SIZE, SIZE);
        graphicsContext.drawImage(snakeSprites, 16, 0, SIZE, SIZE, xCoords.get(1), yCoords.get(1), SIZE, SIZE);
        graphicsContext.drawImage(snakeSprites, 64, 32, SIZE, SIZE, xCoords.get(2), yCoords.get(2), SIZE, SIZE);
    }

    public void move(Snake player2Snake) {
        int prevX = xCoords.get(0);
        int prevY = yCoords.get(0);
        int prevXBuffer;
        int prevYBuffer;
        if (!direction.isEmpty()) {
            if (direction.equals("up")) {
                yCoords.set(0, yCoords.get(0) - 16);
            }
            if (direction.equals("down")) {
                yCoords.set(0, yCoords.get(0) + 16);
            }
            if (direction.equals("right")) {
                xCoords.set(0, xCoords.get(0) + 16);
            }
            if (direction.equals("left")) {
                xCoords.set(0, xCoords.get(0) - 16);
            }
            if (tileMap[yCoords.get(0) / 16].charAt(xCoords.get(0) / 16) == 'W') {
                collision = true;
                return;
            }
            if (player2Snake != null) {
                for (int fragmentNumber = 0; fragmentNumber < player2Snake.getSnakeSize(); fragmentNumber++) {
                    if (xCoords.get(0) == player2Snake.getX(fragmentNumber) && yCoords.get(0) == player2Snake.getY(fragmentNumber)) {
                        collision = true;
                        return;
                    }
                }
            }
            for (int fragmentNumber = 1; fragmentNumber < snakeSize; fragmentNumber++) {
                if ((direction.equals("up") && yCoords.get(fragmentNumber).equals(yCoords.get(0)) && xCoords.get(0).equals(xCoords.get(fragmentNumber))) ||
                        (direction.equals("down") && yCoords.get(fragmentNumber).equals(yCoords.get(0)) && xCoords.get(0).equals(xCoords.get(fragmentNumber))) ||
                        (direction.equals("right") && xCoords.get(fragmentNumber).equals(xCoords.get(0)) && yCoords.get(0).equals(yCoords.get(fragmentNumber))) ||
                        (direction.equals("left") && xCoords.get(fragmentNumber).equals(xCoords.get(0)) && yCoords.get(0).equals(yCoords.get(fragmentNumber)))) {
                    collision = true;
                    return;
                }
            }
            if (direction.equals("up")) {
                graphicsContext.drawImage(mapSprites, 33, 33, SIZE, SIZE, xCoords.get(0), yCoords.get(0), SIZE, SIZE);
                graphicsContext.drawImage(snakeSprites, 48, 0, SIZE, SIZE, xCoords.get(0), yCoords.get(0), SIZE, SIZE);
            }
            if (direction.equals("down")) {
                graphicsContext.drawImage(mapSprites, 33, 33, SIZE, SIZE, xCoords.get(0), yCoords.get(0), SIZE, SIZE);
                graphicsContext.drawImage(snakeSprites, 64, 16, SIZE, SIZE, xCoords.get(0), yCoords.get(0), SIZE, SIZE);
            }
            if (direction.equals("right")) {
                graphicsContext.drawImage(mapSprites, 33, 33, SIZE, SIZE, xCoords.get(0), yCoords.get(0), SIZE, SIZE);
                graphicsContext.drawImage(snakeSprites, 64, 0, SIZE, SIZE, xCoords.get(0), yCoords.get(0), SIZE, SIZE);
            }
            if (direction.equals("left")) {
                graphicsContext.drawImage(mapSprites, 33, 33, SIZE, SIZE, xCoords.get(0), yCoords.get(0), SIZE, SIZE);
                graphicsContext.drawImage(snakeSprites, 48, 16, SIZE, SIZE, xCoords.get(0), yCoords.get(0), SIZE, SIZE);
            }
            for (int fragmentNumber = 1; fragmentNumber < snakeSize; fragmentNumber++) {
                prevXBuffer = xCoords.get(fragmentNumber);
                prevYBuffer = yCoords.get(fragmentNumber);
                if (fragmentNumber == snakeSize - 1) {
                    graphicsContext.drawImage(mapSprites, 33, 33, SIZE, SIZE, xCoords.get(fragmentNumber), yCoords.get(fragmentNumber), SIZE, SIZE);
                }
                xCoords.set(fragmentNumber, prevX);
                yCoords.set(fragmentNumber, prevY);
                prevX = prevXBuffer;
                prevY = prevYBuffer;
            }
        }
    }

    public void eat(Fruit fruit, Bonus bonus) {
        if (fruit != null) {
            fruitsEaten++;
            fruit.setGenerate(true);
            prevTailX = xCoords.get(0);
            prevTailY = yCoords.get(0);
            eat = true;
        }
        if (bonus != null) {
            fruitsEaten += 2;
            bonus.resetBonusTimeout();
            prevTailX = xCoords.get(0);
            prevTailY = yCoords.get(0);
            eat = true;
        }
    }

    public void grow() {
        snakeSize++;
        xCoords.add(prevTailX);
        yCoords.add(prevTailY);
        eat = false;
    }

    public void displaySnake() {
        for (int fragmentNumber = 1; fragmentNumber < snakeSize; fragmentNumber++) {
            graphicsContext.drawImage(mapSprites, 33, 33, SIZE, SIZE, xCoords.get(fragmentNumber), yCoords.get(fragmentNumber), SIZE, SIZE);
            if (fragmentNumber == snakeSize - 1 && yCoords.get(fragmentNumber) > yCoords.get(fragmentNumber - 1)) {
                graphicsContext.drawImage(snakeSprites, 48, 32, SIZE, SIZE, xCoords.get(fragmentNumber), yCoords.get(fragmentNumber), SIZE, SIZE);
            }
            if (fragmentNumber == snakeSize - 1 && yCoords.get(fragmentNumber) < yCoords.get(fragmentNumber - 1)) {
                graphicsContext.drawImage(snakeSprites, 64, 48, SIZE, SIZE, xCoords.get(fragmentNumber), yCoords.get(fragmentNumber), SIZE, SIZE);
            }
            if (fragmentNumber == snakeSize - 1 && xCoords.get(fragmentNumber) > xCoords.get(fragmentNumber - 1)) {
                graphicsContext.drawImage(snakeSprites, 48, 48, SIZE, SIZE, xCoords.get(fragmentNumber), yCoords.get(fragmentNumber), SIZE, SIZE);
            }
            if (fragmentNumber == snakeSize - 1 && xCoords.get(fragmentNumber) < xCoords.get(fragmentNumber - 1)) {
                graphicsContext.drawImage(snakeSprites, 64, 32, SIZE, SIZE, xCoords.get(fragmentNumber), yCoords.get(fragmentNumber), SIZE, SIZE);
            }
            if (fragmentNumber != snakeSize - 1 && xCoords.get(fragmentNumber).equals(xCoords.get(fragmentNumber - 1)) && xCoords.get(fragmentNumber).equals(xCoords.get(fragmentNumber + 1))) {
                graphicsContext.drawImage(snakeSprites, 32, 16, SIZE, SIZE, xCoords.get(fragmentNumber), yCoords.get(fragmentNumber), SIZE, SIZE);
            }
            if (fragmentNumber != snakeSize - 1 && !xCoords.get(fragmentNumber).equals(xCoords.get(fragmentNumber - 1)) && !xCoords.get(fragmentNumber).equals(xCoords.get(fragmentNumber + 1))) {
                graphicsContext.drawImage(snakeSprites, 16, 0, SIZE, SIZE, xCoords.get(fragmentNumber), yCoords.get(fragmentNumber), SIZE, SIZE);
            }
            if (fragmentNumber != snakeSize - 1 && ((xCoords.get(fragmentNumber) > xCoords.get(fragmentNumber - 1) && yCoords.get(fragmentNumber) > yCoords.get(fragmentNumber + 1)) || (xCoords.get(fragmentNumber) > xCoords.get(fragmentNumber + 1) && yCoords.get(fragmentNumber) > yCoords.get(fragmentNumber - 1)))) {
                graphicsContext.drawImage(snakeSprites, 32, 32, SIZE, SIZE, xCoords.get(fragmentNumber), yCoords.get(fragmentNumber), SIZE, SIZE);
            }
            if (fragmentNumber != snakeSize - 1 && ((xCoords.get(fragmentNumber) > xCoords.get(fragmentNumber - 1) && yCoords.get(fragmentNumber) < yCoords.get(fragmentNumber + 1)) || (xCoords.get(fragmentNumber) > xCoords.get(fragmentNumber + 1) && yCoords.get(fragmentNumber) < yCoords.get(fragmentNumber - 1)))) {
                graphicsContext.drawImage(snakeSprites, 32, 0, SIZE, SIZE, xCoords.get(fragmentNumber), yCoords.get(fragmentNumber), SIZE, SIZE);
            }
            if (fragmentNumber != snakeSize - 1 && ((xCoords.get(fragmentNumber) < xCoords.get(fragmentNumber - 1) && yCoords.get(fragmentNumber) > yCoords.get(fragmentNumber + 1)) || (xCoords.get(fragmentNumber) < xCoords.get(fragmentNumber + 1) && yCoords.get(fragmentNumber) > yCoords.get(fragmentNumber - 1)))) {
                graphicsContext.drawImage(snakeSprites, 0, 16, SIZE, SIZE, xCoords.get(fragmentNumber), yCoords.get(fragmentNumber), SIZE, SIZE);
            }
            if (fragmentNumber != snakeSize - 1 && ((xCoords.get(fragmentNumber) < xCoords.get(fragmentNumber - 1) && yCoords.get(fragmentNumber) < yCoords.get(fragmentNumber + 1)) || (xCoords.get(fragmentNumber) < xCoords.get(fragmentNumber + 1) && yCoords.get(fragmentNumber) < yCoords.get(fragmentNumber - 1)))) {
                graphicsContext.drawImage(snakeSprites, 0, 0, SIZE, SIZE, xCoords.get(fragmentNumber), yCoords.get(fragmentNumber), SIZE, SIZE);
            }
        }
    }

    public void setDirection(String newDirection) {
        direction = newDirection;
    }

    public boolean getCollision() {
        return collision;
    }

    public int getFruitsEaten() {
        return fruitsEaten;
    }

    public int getSnakeSize() {
        return snakeSize;
    }

    public boolean getEat() {
        return eat;
    }

    public int getX(int index) {
        return xCoords.get(index);
    }

    public int getY(int index) {
        return yCoords.get(index);
    }

    public StringBuilder[] getTileMap() {
        return tileMap;
    }
}