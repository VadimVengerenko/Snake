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
    private FileWriter fileWriter;
    private int result;
    private Button returnToMenuButton;
    private Menu menu;

    public Records(Menu menu, Settings settings, Group root, int result) {
        this.menu = menu;
        this.result = result;
        recordsFile = new File("src/com/snake/records.json");
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

    public void setPlayerName(String playerName) {
        readFile();
        findPlayerName(playerName);
    }

    public void readFile() {
        StringBuilder jsonFileInfo = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(recordsFile.getAbsoluteFile()));
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
            jsonArray = jsonObject.getJSONArray("records");
        }
    }

    public void findPlayerName(String playerName) {
        if (!jsonArray.isEmpty()) {
            boolean nameFound = false;
            int playerId = 0;
            for (int index = 0; index < jsonArray.length(); index++) {
                if (jsonArray.getJSONObject(index).getInt("player_id") > playerId) {
                    playerId = jsonArray.getJSONObject(index).getInt("player_id");
                }
                if (jsonArray.getJSONObject(index).getString("name").equals(playerName)) {
                    if (Integer.parseInt(String.valueOf(jsonArray.getJSONObject(index).getString("mode").charAt(0))) == settings.getNumOfPlayers()) {
                        if (jsonArray.getJSONObject(index).get("map").toString().charAt(jsonArray.getJSONObject(index).getString("map").length() - 1) == settings.getMapPathname().charAt(settings.getMapPathname().length() - 5)) {
                            if (Integer.parseInt(String.valueOf(jsonArray.getJSONObject(index).getString("speed").charAt(1))) == settings.getSpeed()) {
                                if (jsonArray.getJSONObject(index).getInt("result") < result) {
                                    jsonArray.getJSONObject(index).put("result", result);
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("records", jsonArray);
                                    writeResult(jsonObject);
                                }
                            } else {
                                writeNewResult(jsonArray.getJSONObject(index).getInt("player_id"),
                                        jsonArray.getJSONObject(index).getString("mode"),
                                        jsonArray.getJSONObject(index).getString("map"),
                                        "x" + settings.getSpeed(),
                                        playerName);
                            }
                        } else {
                            writeNewResult(jsonArray.getJSONObject(index).getInt("player_id"),
                                    jsonArray.getJSONObject(index).getString("mode"),
                                    "map " + settings.getMapPathname().charAt(settings.getMapPathname().length() - 5),
                                    "x" + settings.getSpeed(),
                                    playerName);
                        }
                    } else {
                        if (settings.getNumOfPlayers() == 1) {
                            writeNewResult(jsonArray.getJSONObject(index).getInt("player_id"),
                                    settings.getNumOfPlayers() + " player",
                                    "map " + settings.getMapPathname().charAt(settings.getMapPathname().length() - 5),
                                    "x" + settings.getSpeed(),
                                    playerName);
                        } else {
                            writeNewResult(jsonArray.getJSONObject(index).getInt("player_id"),
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
            if (!nameFound) {
                if (settings.getNumOfPlayers() == 1) {
                    writeNewResult(playerId + 1,
                            settings.getNumOfPlayers() + " player",
                            "map " + settings.getMapPathname().charAt(settings.getMapPathname().length() - 5),
                            "x" + settings.getSpeed(),
                            playerName);
                } else {
                    writeNewResult(playerId + 1,
                            settings.getNumOfPlayers() + " players",
                            "map " + settings.getMapPathname().charAt(settings.getMapPathname().length() - 5),
                            "x" + settings.getSpeed(),
                            playerName);
                }
            }
        } else {
            if (settings.getNumOfPlayers() == 1) {
                writeNewResult(1,
                        settings.getNumOfPlayers() + " player",
                        "map " + settings.getMapPathname().charAt(settings.getMapPathname().length() - 5),
                        "x" + settings.getSpeed(),
                        playerName);
            } else {
                writeNewResult(1,
                        settings.getNumOfPlayers() + " players",
                        "map " + settings.getMapPathname().charAt(settings.getMapPathname().length() - 5),
                        "x" + settings.getSpeed(),
                        playerName);
            }
        }
    }

    public void writeNewResult(int playerId, String mode, String map, String speed, String playerName) {
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
        writeResult(jsonObject);
    }

    public void writeResult(JSONObject jsonObject) {
        try {
            fileWriter = new FileWriter(recordsFile.getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileWriter.write(jsonObject.toString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRecordsEvents() {
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
        EventHandler<MouseEvent> returnToMenuButtonEventHandler = mouseEvent -> {
            menu.displayMenu();
            menu.setMenuEvents();
        };
        returnToMenuButton.setOnMouseClicked(returnToMenuButtonEventHandler);
    }
}