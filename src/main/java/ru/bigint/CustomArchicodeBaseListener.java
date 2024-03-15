package ru.bigint;

import ru.bigint.model.DiagObject;
import ru.bigint.model.Diagram;
import ru.bigint.model.RelationDirection;
import ru.bigint.model.Relation;

import java.util.Optional;

public class CustomArchicodeBaseListener extends ArchicodeBaseListener {
    private Diagram diagram;

    public CustomArchicodeBaseListener(Diagram diagram) {
        this.diagram = diagram;
    }

    @Override
    public void exitRelation(ArchicodeParser.RelationContext ctx) {
        DiagObject fromObj = null;
        DiagObject toObj = null;
        RelationDirection relDirectionFrom = null;
        RelationDirection relDirectionTo = null;
        RelationDirection toObjectPriorityPosition = null;

        if (ctx.object() != null && ctx.object().size() == 2) {
            fromObj = findOrCreate(ctx.object(0).ID().getText());
            toObj = findOrCreate(ctx.object(1).ID().getText());
        }
        if (ctx.relationDirections() != null
                && ctx.relationDirections().direction().size() == 2) {
            relDirectionFrom = RelationDirection.fromString(ctx.relationDirections().direction(0).getText());
            relDirectionTo = RelationDirection.fromString(ctx.relationDirections().direction(1).getText());
        }
        if (ctx.priorityPosition() != null) {
            toObjectPriorityPosition = RelationDirection.fromString(ctx.priorityPosition().getText());
        }

        Relation relation = new Relation(fromObj, toObj, relDirectionFrom, relDirectionTo, toObjectPriorityPosition);
        diagram.addObject(fromObj);
        diagram.addObject(toObj);
        diagram.addRelation(relation);
    }

    private DiagObject findOrCreate(String text) {
        Optional<DiagObject> obj = diagram.getObjects().stream()
                .filter(item -> item.getName().equals(text))
                .findFirst();
        return obj.orElseGet(() -> new DiagObject(text));
    }

    @Override
    public void exitStatement(ArchicodeParser.StatementContext ctx) {
        super.exitStatement(ctx);
    }
}
