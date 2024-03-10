package ru.bigint.model;

public class DiagObject {
    private String name;
    private String type;
    private Coord coord;

    public Coord getCoord() {
        return coord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public DiagObject(String name) {
        this.name = name;
    }
}
