package ru.bigint;

import ru.bigint.model.DiagObject;
import ru.bigint.model.Diagram;
import ru.bigint.model.Relation;

public class CustomArchicodeBaseListener extends ArchicodeBaseListener {
    private Diagram diagram;

    public CustomArchicodeBaseListener(Diagram diagram) {
        this.diagram = diagram;
    }

    @Override
    public void exitRelation(ArchicodeParser.RelationContext ctx) {
        DiagObject fromObj = new DiagObject(ctx.object(0).ID().getText());
        DiagObject toObj = new DiagObject(ctx.object(1).ID().getText());
        Relation relation = new Relation(fromObj, toObj, "");
        diagram.getObjects().add(fromObj);
        diagram.getObjects().add(toObj);
        diagram.getRelations().add(relation);
    }

    @Override
    public void exitStatement(ArchicodeParser.StatementContext ctx) {
        super.exitStatement(ctx);
    }
}
