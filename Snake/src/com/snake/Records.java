package com.snake;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Records {
    private JSONArray jsonArray;
    private Settings settings;
    private TextField nameField;
    private File recordsFile;
    private int result;
    private Button returnToMenuButton;
    private Menu menu;

    public Records(Menu menu, Settings settings, Group root, int result) {
        this.menu = menu;
        this.result = result;
        recordsFile = new File("src/com/snake/records.json"); //открывается файл с рекордами
        this.settings = settings;
        nameField = new TextField();
        nameField.setTranslateX(566);
        nameField.setTranslateY(220);
        root.getChildren().add(nameField);
    }

    public Records(Menu menu) {
        recordsFile = new File("src/com/snake/records.json");
        this.menu = menu;
        Label recordsLabel = new Label("РЕКОРДЫ");
        recordsLabel.setTextFill(Color.GOLD);
        recordsLabel.setTranslateX(605);
        recordsLabel.setTranslateY(99);
        recordsLabel.setFont(Font.font(30));
        returnToMenuButton = new Button("НАЗАД");
        returnToMenuButton.setFocusTraversable(false);
        returnToMenuButton.setLayoutX(384);
        returnToMenuButton.setLayoutY(107.5);
        Group root = new Group();
        readFile();
        ArrayList<RecordsTable> values = new ArrayList<>();
        if (!jsonArray.isEmpty()) {
            //производится вывод рекордов в таблицу в соответствии с классом-шаблоном для вывода RecordsTable
            for (int index = 0; index < jsonArray.length(); index++) {
                values.add(new RecordsTable(
                        index + 1,
                        jsonArray.getJSONObject(index).getInt("player_id"),
                        jsonArray.getJSONObject(index).getString("name"),
                        jsonArray.getJSONObject(index).getString("date"),
                        jsonArray.getJSONObject(index).getString("time"),
                        jsonArray.getJSONObject(index).getInt("result"),
                        jsonArray.getJSONObject(index).getString("mode"),
                        jsonArray.getJSONObject(index).getString("map"),
                        jsonArray.getJSONObject(index).getString("speed")));
            }
        }
        ObservableList<RecordsTable> records = FXCollections.observableArrayList(
                values
        );
        TableView<RecordsTable> table = new TableView<>(records);
        table.setPrefWidth(552);
        table.setPrefHeight(200);
        table.setTranslateX(364);
        table.setTranslateY(150);
        TableColumn<RecordsTable, String> fieldNumberColumn = new TableColumn<>("№");
        fieldNumberColumn.setCellValueFactory(new PropertyValueFactory<>("fieldNumber"));
        table.getColumns().add(fieldNumberColumn);
        TableColumn<RecordsTable, String> idColumn = new TableColumn<>("ID игрока");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        table.getColumns().add(idColumn);
        TableColumn<RecordsTable, String> nameColumn = new TableColumn<>("Имя");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.getColumns().add(nameColumn);
        TableColumn<RecordsTable, String> dateColumn = new TableColumn<>("Дата");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        table.getColumns().add(dateColumn);
        TableColumn<RecordsTable, String> timeColumn = new TableColumn<>("Время");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        table.getColumns().add(timeColumn);
        TableColumn<RecordsTable, String> resultColumn = new TableColumn<>("Результат");
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("result"));
        table.getColumns().add(resultColumn);
        TableColumn<RecordsTable, String> modeColumn = new TableColumn<>("Режим");
        modeColumn.setCellValueFactory(new PropertyValueFactory<>("mode"));
        table.getColumns().add(modeColumn);
        TableColumn<RecordsTable, String> mapColumn = new TableColumn<>("Карта");
        mapColumn.setCellValueFactory(new PropertyValueFactory<>("map"));
        table.getColumns().add(mapColumn);
        TableColumn<RecordsTable, String> speedColumn = new TableColumn<>("Скорость");
        speedColumn.setCellValueFactory(new PropertyValueFactory<>("speed"));
        table.getColumns().add(speedColumn);
        root.getChildren().addAll(table, returnToMenuButton, recordsLabel);
        Scene recordsScene = new Scene(root, menu.getStage().getWidth(), menu.getStage().getHeight());
        recordsScene.setFill(Color.LIGHTGREEN);
        menu.getStage().setScene(recordsScene);
    }

    public boolean setPlayerName(String playerName) {
        readFile(); //чтение файла
        return findPlayerName(playerName);
    }

    public void readFile() {
        StringBuilder jsonFileInfo = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(recordsFile.getAbsoluteFile())); //рекорды считываются
            try {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    jsonFileInfo.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        jsonArray = new JSONArray();
        if (jsonFileInfo.length() != 0) {
            JSONObject jsonObject = new JSONObject(jsonFileInfo.toString());
            jsonArray = jsonObject.getJSONArray("records"); //рекорды заносятся в массив в формате JSON
        }
    }

    public boolean findPlayerName(String playerName) {
        if (!jsonArray.isEmpty()) { //рекорды отсутствуют
            boolean nameFound = false;
            int playerId = 0;
            for (int index = 0; index < jsonArray.length(); index++) { //цикл поиска имени данного игрока
                if (jsonArray.getJSONObject(index).getInt("player_id") > playerId) {
                    playerId = jsonArray.getJSONObject(index).getInt("player_id"); //формируется уникальный ID игрока в случае, если такое имя не найдется в массиве рекордов
                }
                if (jsonArray.getJSONObject(index).getString("name").equals(playerName)) { //имя совпало с именем одного из рекордов
                    if (Integer.parseInt(String.valueOf(jsonArray.getJSONObject(index).getString("mode").charAt(0))) == settings.getNumOfPlayers()) { //текущий режим игры совпал с режимом в найденном рекорде
                        if (jsonArray.getJSONObject(index).get("map").toString().charAt(jsonArray.getJSONObject(index).getString("map").length() - 1) == settings.getMapPathname().charAt(settings.getMapPathname().length() - 5)) { //текущая карта игры совпала с картой в найденном рекорде
                            if (Integer.parseInt(String.valueOf(jsonArray.getJSONObject(index).getString("speed").charAt(1))) == settings.getSpeed()) { //совпали скорости
                                if (jsonArray.getJSONObject(index).getInt("result") < result) { //если текущий результат больше найденного, то он заменяет результат найденного рекорда
                                    jsonArray.getJSONObject(index).put("result", result);
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("records", jsonArray);
                                    return writeResult(jsonObject);
                                }
                            } else { //если одно из полей рекорда не совпадает с соответствующим значением для данного игрока, то это считается новым рекордом (ID игрока сохраняется)
                                return writeNewResult(jsonArray.getJSONObject(index).getInt("player_id"),
                                        jsonArray.getJSONObject(index).getString("mode"),
                                        jsonArray.getJSONObject(index).getString("map"),
                                        "x" + settings.getSpeed(),
                                        playerName);
                            }
                        } else {
                            return writeNewResult(jsonArray.getJSONObject(index).getInt("player_id"),
                                    jsonArray.getJSONObject(index).getString("mode"),
                                    "map " + settings.getMapPathname().charAt(settings.getMapPathname().length() - 5),
                                    "x" + settings.getSpeed(),
                                    playerName);
                        }
                    } else {
                        if (settings.getNumOfPlayers() == 1) {
                            return writeNewResult(jsonArray.getJSONObject(index).getInt("player_id"),
                                    settings.getNumOfPlayers() + " player",
                                    "map " + settings.getMapPathname().charAt(settings.getMapPathname().length() - 5),
                                    "x" + settings.getSpeed(),
                                    playerName);
                        } else {
                            return writeNewResult(jsonArray.getJSONObject(index).getInt("player_id"),
                                    settings.getNumOfPlayers() + " players",
                                    "map " + settings.getMapPathname().charAt(settings.getMapPathname().length() - 5),
                                    "x" + settings.getSpeed(),
                                    playerName);
                        }
                    }
                    nameFound = true;
                    break;
                }
            }
            if (!nameFound) { //если данное имя не найдено в массиве рекордов, то это новый рекорд
                if (settings.getNumOfPlayers() == 1) {
                    return writeNewResult(playerId + 1,
                            settings.getNumOfPlayers() + " player",
                            "map " + settings.getMapPathname().charAt(settings.getMapPathname().length() - 5),
                            "x" + settings.getSpeed(),
                            playerName);
                } else {
                    return writeNewResult(playerId + 1,
                            settings.getNumOfPlayers() + " players",
                            "map " + settings.getMapPathname().charAt(settings.getMapPathname().length() - 5),
                            "x" + settings.getSpeed(),
                            playerName);
                }
            }
        } else {
            if (settings.getNumOfPlayers() == 1) {
                return writeNewResult(1,
                        settings.getNumOfPlayers() + " player",
                        "map " + settings.getMapPathname().charAt(settings.getMapPathname().length() - 5),
                        "x" + settings.getSpeed(),
                        playerName);
            } else {
                return writeNewResult(1,
                        settings.getNumOfPlayers() + " players",
                        "map " + settings.getMapPathname().charAt(settings.getMapPathname().length() - 5),
                        "x" + settings.getSpeed(),
                        playerName);
            }
        }
        return true;
    }

    public boolean writeNewResult(int playerId, String mode, String map, String speed, String playerName) {
        //в файл записывается новый рекорд
        SimpleDateFormat formaterDate = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat formaterTime = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        JSONObject newResult = new JSONObject();
        newResult.put("player_id", playerId);
        newResult.put("name", playerName);
        newResult.put("date", formaterDate.format(date));
        newResult.put("time", formaterTime.format(date));
        newResult.put("result", result);
        newResult.put("mode", mode);
        newResult.put("map", map);
        newResult.put("speed", speed);
        jsonArray.put(newResult);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("records", jsonArray);
        return writeResult(jsonObject);
    }

    public boolean writeResult(JSONObject jsonObject) {
        //в файл записывается рекорд
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(recordsFile.getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            fileWriter.write(jsonObject.toString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void setRecordsEvents() {
        //устанавливается обработчик событий на поле ввода имени игрока, если запущена игра
        EventHandler<KeyEvent> nameFieldEventHandler = keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                setPlayerName(nameField.getText());
                menu.displayMenu();
                menu.setMenuEvents();
            }
        };
        nameField.setOnKeyPressed(nameFieldEventHandler);
    }

    public void setRecordsMenuEvents() {
        //устанавливается обработчик событий на кнопку возврата в меню из пункта рекордов
        EventHandler<MouseEvent> returnToMenuButtonEventHandler = mouseEvent -> {
            menu.displayMenu();
            menu.setMenuEvents();
        };
        returnToMenuButton.setOnMouseClicked(returnToMenuButtonEventHandler);
    }
}