package ru.bigint.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Diagram {
    private Queue<DiagObject> objects;
    private Queue<Relation> relations;
    private Set<DiagObject> drawed; //Уже отрисованные

    private int defaultWidth = 100;
    private int defaultHeight = 60;
    private int defaultMarginX = 40;
    private int defaultMarginY = 20;

    public Diagram() {
        this.objects = new LinkedList<>();
        this.relations = new LinkedList<>();
        this.drawed = new HashSet<>();
    }

    public Queue<DiagObject> getObjects() {
        return objects;
    }

    public void setObjects(Queue<DiagObject> objects) {
        this.objects = objects;
    }

    public Queue<Relation> getRelations() {
        return relations;
    }

    public void setRelations(Queue<Relation> relations) {
        this.relations = relations;
    }

    public void draw() {
        String outData = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "\n" +
                "<svg width=\"800\" height=\"600\" xmlns=\"http://www.w3.org/2000/svg\" style=\"border:1px solid #d3d3d3;\">\n\n";

        for (var obj : objects) {
            //Связи объекта с дочерними
            List<Relation> objRelations = relations.stream()
                    .filter(item -> item.getFrom().equals(obj))
                    .toList();

            //Рисуем сам объект
            outData += drawObj(obj, 0, 0, defaultWidth, defaultHeight);

            //Рисуем связи объекта
            int x = defaultWidth + defaultMarginX;
            int y = 0;
            for (var rel : objRelations) {
                outData += drawObj(rel.getTo(), x, y, defaultWidth, defaultHeight);
                y += defaultHeight + defaultMarginY;
            }
        }

        outData += "</svg>\n" +
                "</body>\n" +
                "</html>";

        try {
            Path path = Paths.get("C:\\Users\\Max\\Desktop\\antlr\\out.htm");
            Files.write(path, outData.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Рисование объекта
     * x
     * y
     * width
     * heaight
     */
    private String drawObj(DiagObject obj, int x, int y, int width, int height) {
        if (!drawed.contains(obj)) {
            drawed.add(obj);
            return "<rect x=\"" + x + "\" y=\"" + y + "\" width=\"" + width + "\" height=\"" + height + "\" rx=\"15\" style=\"fill:#eee;stroke-width:1;stroke:black\" />\n" +
                    "<text x=\"" + (x+width/2) + "\" y=\"" + (y+height/2) + "\" dominant-baseline=\"middle\" text-anchor=\"middle\">" + obj.getName() + "</text>    \n";
        }
        return "";
    }
}
