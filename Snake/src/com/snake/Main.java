package com.snake;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("snake_icon.png"))); //иконка приложения, отображаемая на панели задач
        stage.initStyle(StageStyle.UNDECORATED); //окно без рамок
        stage.setTitle("Snake"); //заголовок окна
        stage.setMaximized(true); //размер stage на весь экран
        //создается и отображается меню
        Menu menu = new Menu(stage);
        menu.displayMenu();
        menu.setMenuEvents(); //установка обработчиков событий в меню
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    public Stage getStage(){
        return stage;
    }
}