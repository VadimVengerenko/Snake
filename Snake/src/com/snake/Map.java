package com.snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.*;

public class Map {
    private File map;
    private StringBuilder[] tileMap;

    public Map(String mapPathname) {
        map = new File(mapPathname); //открывается файл с картой
        tileMap = new StringBuilder[32];
        for (int index = 0; index < tileMap.length; index++) {
            tileMap[index] = new StringBuilder();
        }
    }

    public StringBuilder[] getTileMap() {
        return tileMap;
    }

    public boolean drawMap(GraphicsContext graphicsContext) {
        final int SIZE = 16; //размер одного спрайта
        //константы смещений спрайтов карты в изображении со спрайтами
        final int WALLOFFSETX = 0;
        final int WALLOFFSETY = 0;
        final int LANDOFFSETX = 33;
        final int LANDOFFSETY = 33;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(map.getAbsoluteFile()));
            for (StringBuilder stringBuilder : tileMap) {
                try {
                    stringBuilder.insert(0, bufferedReader.readLine()); //карта переписывается в массив StringBuilder'ов
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        Image mapSprites = new Image(Main.class.getResourceAsStream("Sprites/snake.png")); //изображение со спрайтами карты
        for (int row = 0; row < tileMap.length; row++) { //спрайты карты размещаются на игровом поле в соответствии с символьным представлением карты
            for (int column = 0; column < tileMap[row].length(); column++) {
                final char WALL = 'W';
                final char LAND = 'L';
                switch (tileMap[row].charAt(column)) {
                    case WALL:
                        graphicsContext.drawImage(mapSprites, WALLOFFSETX, WALLOFFSETY, SIZE, SIZE, column * SIZE, row * SIZE, SIZE, SIZE);
                        break;
                    case LAND:
                        graphicsContext.drawImage(mapSprites, LANDOFFSETX, LANDOFFSETY, SIZE, SIZE, column * SIZE, row * SIZE, SIZE, SIZE);
                        break;
                    default:
                        return false;
                }
            }
        }
        return true;
    }
}