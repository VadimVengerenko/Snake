package com.snake;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("snake_icon.png")));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Snake");
        stage.setMaximized(true);
        Menu menu = new Menu(stage);
        menu.displayMenu();
        menu.setMenuEvents();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}