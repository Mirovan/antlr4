package ru.bigint;

import ru.bigint.model.DiagObject;
import ru.bigint.model.Diagram;
import ru.bigint.model.Relation;

import java.util.Optional;

public class CustomArchicodeBaseListener extends ArchicodeBaseListener {
    private Diagram diagram;

    public CustomArchicodeBaseListener(Diagram diagram) {
        this.diagram = diagram;
    }

    @Override
    public void exitRelation(ArchicodeParser.RelationContext ctx) {
        DiagObject fromObj = findOrCreate(ctx.object(0).ID().getText());
        DiagObject toObj = findOrCreate(ctx.object(1).ID().getText());
        Relation relation = new Relation(fromObj, toObj, "");
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
