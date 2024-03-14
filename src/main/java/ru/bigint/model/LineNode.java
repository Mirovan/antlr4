package ru.bigint.model;

/**
 * Точка на манхеттеновской линии для соединения двух объектов
 */
public class LineNode implements Comparable<LineNode> {
    private Coord coord;
    private int cost;
    private LineNode parent;

    public LineNode(Coord coord, int cost, LineNode parent) {
        this.coord = coord;
        this.cost = cost;
        this.parent = parent;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public LineNode getParent() {
        return parent;
    }

    public void setParent(LineNode parent) {
        this.parent = parent;
    }

    @Override
    public int compareTo(LineNode o) {
        return Integer.compare(this.cost, o.cost);
    }
}
