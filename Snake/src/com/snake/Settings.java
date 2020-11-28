package com.snake;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Settings {
    private Button returnToMenuButton;
    private Slider speedSlider;
    private RadioButton radioButton1player;
    private RadioButton radioButton2players;
    private RadioButton radioButtonCharacter1;
    private RadioButton radioButtonCharacter2;
    private RadioButton radioButtonMap1;
    private RadioButton radioButtonMap2;
    private ToggleGroup numOfPlayersChoice;
    private ToggleGroup characterChoice;
    private ToggleGroup mapChoice;
    private Menu menu;
    private int numOfPlayers;
    private String mapPathname;
    private String characterPath;
    private int speed;

    public Settings(Menu menu) {
        this.menu = menu;
        //создаются элементы для управления настройками и задаются их параметры
        //Radio buttons для выбора игрока
        radioButton1player = new RadioButton("1 игрок");
        radioButton2players = new RadioButton("2 игрока");
        //Radio buttons для выбора персонажа
        radioButtonCharacter1 = new RadioButton("персонаж 1");
        radioButtonCharacter2 = new RadioButton("персонаж 2");
        //Radio buttons для выбора карты
        radioButtonMap1 = new RadioButton("карта 1");
        radioButtonMap2 = new RadioButton("карта 2");
        //Slider для выбора скорости игры
        speedSlider = new Slider(1, 5, 1);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMinorTickCount(0);
        speedSlider.setMajorTickUnit(2);
        speedSlider.setSnapToTicks(true);
        speedSlider.setBlockIncrement(2);
        speedSlider.setTranslateX(572);
        speedSlider.setTranslateY(170);
        speedSlider.setFocusTraversable(false);
        numOfPlayersChoice = new ToggleGroup();
        characterChoice = new ToggleGroup();
        mapChoice = new ToggleGroup();
        //Radio buttons устанавливаются в начальное положение
        radioButton1player.fire();
        radioButtonCharacter1.fire();
        radioButtonMap1.fire();
        //настройки по умолчанию
        speed = 1;
        numOfPlayers = 1;
        mapPathname = "src/com/snake/Maps/map1.txt";
        characterPath = "Sprites/snake-graphics_small1.png";
        //отменяется фокусировка на Radio buttons
        radioButton1player.setFocusTraversable(false);
        radioButton2players.setFocusTraversable(false);
        radioButtonCharacter1.setFocusTraversable(false);
        radioButtonCharacter2.setFocusTraversable(false);
        radioButtonMap1.setFocusTraversable(false);
        radioButtonMap2.setFocusTraversable(false);
        //Radio buttons объединяются в группы по соответствующей настройке
        radioButton1player.setToggleGroup(numOfPlayersChoice);
        radioButton2players.setToggleGroup(numOfPlayersChoice);
        radioButtonCharacter1.setToggleGroup(characterChoice);
        radioButtonCharacter2.setToggleGroup(characterChoice);
        radioButtonMap1.setToggleGroup(mapChoice);
        radioButtonMap2.setToggleGroup(mapChoice);
        //расположение Radio buttons в настройках
        radioButton1player.setTranslateY(230);
        radioButton1player.setTranslateX(572);
        radioButton2players.setTranslateY(250);
        radioButton2players.setTranslateX(572);
        radioButtonCharacter1.setTranslateY(290);
        radioButtonCharacter1.setTranslateX(572);
        radioButtonCharacter2.setTranslateY(310);
        radioButtonCharacter2.setTranslateX(572);
        radioButtonMap1.setTranslateY(350);
        radioButtonMap1.setTranslateX(572);
        radioButtonMap2.setTranslateY(370);
        radioButtonMap2.setTranslateX(572);
        //создается кнопка возврата в меню и задаются ее параметры
        returnToMenuButton = new Button("НАЗАД");
        returnToMenuButton.setFocusTraversable(false);
        returnToMenuButton.setLayoutX(384);
        returnToMenuButton.setLayoutY(107.5);
    }

    public void displaySettings() {
        //отображение меню настроек
        Label settingsLabel = new Label("НАСТРОЙКИ");
        settingsLabel.setTextFill(Color.GOLD);
        settingsLabel.setTranslateX(583);
        settingsLabel.setTranslateY(99);
        settingsLabel.setFont(Font.font(30));
        Label speedChoiceLabel = new Label("Скорость игры:");
        speedChoiceLabel.setTextFill(Color.RED);
        speedChoiceLabel.setTranslateX(572);
        speedChoiceLabel.setTranslateY(150);
        Label numOfPlayersChoiceLabel = new Label("Количество игроков:");
        numOfPlayersChoiceLabel.setTextFill(Color.RED);
        numOfPlayersChoiceLabel.setTranslateX(572);
        numOfPlayersChoiceLabel.setTranslateY(210);
        Label characterChoiceLabel = new Label("Выбор персонажа:");
        characterChoiceLabel.setTextFill(Color.RED);
        characterChoiceLabel.setTranslateX(572);
        characterChoiceLabel.setTranslateY(270);
        Label mapChoiceLabel = new Label("Выбор карты:");
        mapChoiceLabel.setTextFill(Color.RED);
        mapChoiceLabel.setTranslateX(572);
        mapChoiceLabel.setTranslateY(330);
        Group root = new Group();
        //создается сцена меню настроек, на нее добавляются все вышесозданные элементы и задаются ее параметры
        Scene settingsScene = new Scene(root, menu.getStage().getWidth(), menu.getStage().getHeight());
        settingsScene.setFill(Color.LIGHTGREEN);
        menu.getStage().setScene(settingsScene); //устанавливается сцена меню настроек
        root.getChildren().addAll(returnToMenuButton,
                settingsLabel,
                speedSlider,
                radioButton1player,
                radioButton2players,
                radioButtonCharacter1,
                radioButtonCharacter2,
                radioButtonMap1,
                radioButtonMap2,
                speedChoiceLabel,
                numOfPlayersChoiceLabel,
                characterChoiceLabel,
                mapChoiceLabel);
    }

    public void setSettingsEvents() {
        //устанавливаются обработчики событий нажатия на соответствующие элементы меню настроек
        EventHandler<MouseEvent> returnToMenuButtonEventHandler = mouseEvent -> {
            //возврат в меню
            menu.displayMenu();
            menu.setMenuEvents();
        };
        returnToMenuButton.setOnMouseClicked(returnToMenuButtonEventHandler);
        numOfPlayersChoice.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            //выбор кол-ва игроков
            if (t1 == radioButton1player) {
                numOfPlayers = 1;
            }
            if (t1 == radioButton2players) {
                numOfPlayers = 2;
            }
        });
        characterChoice.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            //выбор персонажа

            /*if (t1 == radioButtonCharacter1) {
                characterPath = "Sprites/snake-graphics_small1.png";
            }
            if (t1 == radioButtonCharacter2) {
                characterPath = "Sprites/snake-graphics_small2.png";
            }*/
            StringBuilder buffer;
            buffer = new StringBuilder("Sprites/snake-graphics_small");
            for (int index = 55; index < t1.toString().length() - 1; index++) {
                buffer.append(t1.toString().charAt(index));
            }
            buffer.append(".png");
            characterPath = buffer.toString();
        });
        mapChoice.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            //выбор карты

            /*if (t1 == radioButtonMap1) {
                mapPathname = "src/com/snake/Maps/map1.txt";
            }
            if (t1 == radioButtonMap2) {
                mapPathname = "src/com/snake/Maps/map2.txt";
            }*/
            StringBuilder buffer;
            buffer = new StringBuilder("src/com/snake/Maps/map");
            for (int index = 52; index < t1.toString().length() - 1; index++) {
                buffer.append(t1.toString().charAt(index));
            }
            buffer.append(".txt");
            mapPathname = buffer.toString();
        });
        speedSlider.valueProperty().addListener((observableValue, number, t1) -> speed = t1.intValue());
    }

    public String getMapPathname() {
        return mapPathname;
    }

    public String getCharacterPath() {
        return characterPath;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public int getSpeed() {
        return speed;
    }
}