package ru.bigint.model;

public class Relation {
    private DiagObject from;
    private DiagObject to;
    private String type;
    private RelationDirection relDirectionFrom;
    private RelationDirection relDirectionTo;
    private RelationDirection toObjectPriorityPosition;

    public Relation(DiagObject from, DiagObject to, RelationDirection relDirectionFrom, RelationDirection relDirectionTo) {
        this.from = from;
        this.to = to;
        this.relDirectionFrom = relDirectionFrom != null ? relDirectionFrom : RelationDirection.RIGHT;
        this.relDirectionTo = relDirectionTo != null ? relDirectionTo : RelationDirection.LEFT;
    }

    public Relation(DiagObject from, DiagObject to, RelationDirection relDirectionFrom, RelationDirection relDirectionTo, RelationDirection toObjectPriorityPosition) {
        this.from = from;
        this.to = to;
        this.relDirectionFrom = relDirectionFrom != null ? relDirectionFrom : RelationDirection.RIGHT;
        this.relDirectionTo = relDirectionTo != null ? relDirectionTo : RelationDirection.LEFT;
        this.toObjectPriorityPosition = toObjectPriorityPosition != null ? toObjectPriorityPosition : RelationDirection.RIGHT;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RelationDirection getRelDirectionFrom() {
        return relDirectionFrom;
    }

    public void setRelDirectionFrom(RelationDirection relDirectionFrom) {
        this.relDirectionFrom = relDirectionFrom;
    }

    public RelationDirection getRelDirectionTo() {
        return relDirectionTo;
    }

    public void setRelDirectionTo(RelationDirection relDirectionTo) {
        this.relDirectionTo = relDirectionTo;
    }

    public RelationDirection getToObjectPriorityPosition() {
        return toObjectPriorityPosition;
    }

    public void setToObjectPriorityPosition(RelationDirection toObjectPriorityPosition) {
        this.toObjectPriorityPosition = toObjectPriorityPosition;
    }
}
