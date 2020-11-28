package com.snake;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class RecordsTable {
    private SimpleIntegerProperty fieldNumber;
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty date;
    private SimpleStringProperty time;
    private SimpleIntegerProperty result;
    private SimpleStringProperty mode;
    private SimpleStringProperty map;
    private SimpleStringProperty speed;

    public RecordsTable(int fieldNumber,
                 int id,
                 String name,
                 String date,
                 String time,
                 int result,
                 String mode,
                 String map,
                 String speed) {
        //класс-шаблон для таблицы рекордов
        this.fieldNumber = new SimpleIntegerProperty(fieldNumber);
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        this.result = new SimpleIntegerProperty(result);
        this.mode = new SimpleStringProperty(mode);
        this.map = new SimpleStringProperty(map);
        this.speed = new SimpleStringProperty(speed);
    }

    public int getFieldNumber() {
        return fieldNumber.get();
    }

    public int getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getTime() {
        return time.get();
    }

    public int getResult() {
        return result.get();
    }

    public String getMode() {
        return mode.get();
    }

    public String getMap() {
        return map.get();
    }

    public String getSpeed() {
        return speed.get();
    }
}
