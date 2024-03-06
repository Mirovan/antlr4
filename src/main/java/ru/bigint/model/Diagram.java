package ru.bigint.model;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Diagram {
    private Set<DiagObject> objects;
    private Set<DiagObject> drawed; //Уже отрисованные
    private Set<Relation> relations;

    public Diagram() {
        this.objects = new HashSet<>();
        this.relations = new HashSet<>();
    }

    public Set<DiagObject> getObjects() {
        return objects;
    }

    public void setObjects(Set<DiagObject> objects) {
        this.objects = objects;
    }

    public Set<Relation> getRelations() {
        return relations;
    }

    public void setRelations(Set<Relation> relations) {
        this.relations = relations;
    }

    public void draw() {
        for (var obj : objects) {
            Optional<Relation> relationOpt = relations.stream()
                    .filter(item -> item.getFrom().equals(obj))
                    .findFirst();
            if (relationOpt.isPresent()) {
                System.out.println();
            }
        }
    }
}
