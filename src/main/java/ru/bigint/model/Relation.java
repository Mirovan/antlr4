package ru.bigint.model;

public class Relation {
    private DiagObject from;
    private DiagObject to;
    private String type;

    public Relation(DiagObject from, DiagObject to, String type) {
        this.from = from;
        this.to = to;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DiagObject getFrom() {
        return from;
    }

    public void setFrom(DiagObject from) {
        this.from = from;
    }

    public DiagObject getTo() {
        return to;
    }

    public void setTo(DiagObject to) {
        this.to = to;
    }
}
