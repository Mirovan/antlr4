package ru.bigint.model;

public enum RelationDirection {
    LEFT("left"),
    RIGHT("right"),
    TOP("top"),
    BOTTOM("bottom");

    private String direction;

    RelationDirection(String direction) {
        this.direction = direction;
    }

    public static RelationDirection fromString(String text) {
        for (RelationDirection item : RelationDirection.values()) {
            if (item.direction.equalsIgnoreCase(text)) {
                return item;
            }
        }
        return null;
    }
}
