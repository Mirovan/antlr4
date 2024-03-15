package ru.bigint.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;

public class Diagram {
    Path filePath = Paths.get("C:\\Users\\Max\\Desktop\\antlr\\out.htm");

    private Set<DiagObject> objects;
    private Set<Relation> relations;
    private Set<DiagObject> drawed; //Уже отрисованные

    private int zeroX = 100;
    private int zeroY = 100;
    private int defaultWidth = 100;
    private int defaultHeight = 60;
    private int defaultMarginX = 40;
    private int defaultMarginY = 20;
    private int defaultArrowWidth = 6;
    private int defaultArrowLength = 8;

    public Diagram() {
        this.objects = new LinkedHashSet<>();
        this.relations = new LinkedHashSet<>();
        this.drawed = new HashSet<>();
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
        try {
            String outData = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<body>\n" +
                    "\n" +
                    "\n" +
                    "<svg width=\"800\" height=\"600\" xmlns=\"http://www.w3.org/2000/svg\" style=\"border:1px solid #d3d3d3;\">\n\n";
            Files.write(filePath, outData.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            //Вставка объектов по порядку
            for (var rel : relations) {
                calculateRelatedObjects(rel);
            }

            //Отрисовка всех объектов
            for (var obj : objects) {
                drawObj(obj, defaultWidth, defaultHeight);
            }

            //Вставка соединительных линий
            for (var rel : relations) {
                //Вычисляем соединительные линии
                List<Coord> linePath = calculateRelationLines(rel);
                String points = "";
                for (var coord : linePath) {
                    points += coord.getX() + "," + coord.getY() + " ";
                }
                outData = "<polyline stroke=\"#000\" stroke-width=\"1px\" fill=\"none\" points=\"" + points + "\" />\n";
                Files.write(filePath, outData.getBytes(), StandardOpenOption.APPEND);
            }

            outData = "</svg>\n" +
                    "</body>\n" +
                    "</html>";
            Files.write(filePath, outData.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Вставка связанных объектов - вычисление их координат
     */
    private void calculateRelatedObjects(Relation relation) {
        DiagObject objFrom = relation.getFrom();
        DiagObject objTo = relation.getTo();

        //Если объект не имеет координат - определяем координаты для вставки
        if (!drawed.contains(objFrom)) {
            calculateObjectCoord(objFrom, zeroX, zeroY);
        }

        //Если конечный объект не имеет координат
        if (!drawed.contains(objTo)) {
            //Определяем координаты куда поставить объект в который входит стрелка исходя из направления стрелки
            calculateObjectCoord(objTo, objFrom.getCoord().getX() + defaultWidth + defaultMarginX, objFrom.getCoord().getY());
            /*
            //слева - справа
            if (relation.getRelDirectionFrom() == RelationDirection.LEFT && relation.getRelDirectionTo() == RelationDirection.RIGHT) {
                calculateObjectCoord(objTo, objFrom.getCoord().getX() + defaultWidth + defaultMarginX, objFrom.getCoord().getY());
            }
            //слева - сверху
            else if (relation.getRelDirectionFrom() == RelationDirection.LEFT && relation.getRelDirectionTo() == RelationDirection.TOP) {
                calculateObjectCoord(objTo, objFrom.getCoord().getX(), objFrom.getCoord().getY() + defaultHeight + defaultMarginY);
            } else {
                calculateObjectCoord(objTo, objFrom.getCoord().getX() + defaultWidth + defaultMarginX, objFrom.getCoord().getY());
            }
            */
        }
    }

    /**
     * obj - вставляемый объект
     * x - от какой координаты начинаем поиск для вставки
     * y - от какой координаты начинаем поиск для вставки
     */
    private void calculateObjectCoord(DiagObject obj, int x, int y) {
        //Находим свободные координаты для вставки объекта на Y-координату
        //Просматриваем все уже нарисованные объекты, находим самый нижний
        Optional<DiagObject> lastObjOpt = drawed.stream()
                .filter(item -> item.getCoord().getX() == x)
                .max(Comparator.comparing(
                        DiagObject::getCoord,
                        Comparator.comparingInt(Coord::getY)
                ));

        //Вычисляем координаты для вставки объекта
        if (lastObjOpt.isPresent()) {
            y = lastObjOpt.get().getCoord().getY() + defaultHeight + defaultMarginY;
        }
        obj.setCoord(new Coord(x, y));
        drawed.add(obj);
    }

    /**
     * Рисование соединительных линий, используется A-star алгоритм и манхеттеновские пути
     */
    private List<Coord> calculateRelationLines(Relation relation) {
        //Результирующий массив - путь
        List<Coord> result = new ArrayList<>();

        //множество уже пройденных вершин
        Set<Coord> closed = new HashSet<>();

        //множество частных решений
        PriorityQueue<LineNode> open = new PriorityQueue<>();

        //шаг
        int stepX = defaultMarginX / 2 + defaultWidth / 2;
        int stepY = defaultMarginY / 2 + defaultHeight / 2;

        //1-точка принадлежит объекту
        int startX = relation.getFrom().getCoord().getX() + defaultWidth;
        int startY = relation.getFrom().getCoord().getY() + defaultHeight / 2;
        result.add(new Coord(startX, startY));

        //2 точка отходит от объекта вправо на расстояние defaultMarginX / 2
        int x = startX + defaultMarginX / 2;
        int y = startY;
        result.add(new Coord(x, y));

        //Конечная точка (с отступом) отходит от объекта влево на расстояние defaultMarginX / 2
        int targetWithMarginX = relation.getTo().getCoord().getX() - defaultMarginX / 2;
        int targetWithMarginY = relation.getTo().getCoord().getY() + defaultHeight / 2;
        if (relation.getRelDirectionTo() == RelationDirection.LEFT) {
            targetWithMarginX = relation.getTo().getCoord().getX() - defaultMarginX / 2;
            targetWithMarginY = relation.getTo().getCoord().getY() + defaultHeight / 2;
        } else if (relation.getRelDirectionTo() == RelationDirection.TOP) {
            targetWithMarginX = relation.getTo().getCoord().getX() + defaultWidth / 2;
            targetWithMarginY = relation.getTo().getCoord().getY() - defaultMarginY / 2;
        } else if (relation.getRelDirectionTo() == RelationDirection.RIGHT) {
            targetWithMarginX = relation.getTo().getCoord().getX() + defaultWidth + defaultMarginX / 2;
            targetWithMarginY = relation.getTo().getCoord().getY() + defaultHeight / 2;
        } else if (relation.getRelDirectionTo() == RelationDirection.BOTTOM) {
            targetWithMarginX = relation.getTo().getCoord().getX() + defaultWidth / 2;
            targetWithMarginY = relation.getTo().getCoord().getY() + defaultHeight + defaultMarginY / 2;
        }

        //Точка для которой начинаем искать путь
        open.add(new LineNode(new Coord(x, y), 0, null));

        //Перебираем точки в очереди с приоритетами, самая верхняя точка с минимальной стоимостью пути cost
        while (!open.isEmpty()) {
            LineNode node = open.poll();
            closed.add(node.getCoord());

            //Если пришли к финальной точке
            if (node.getCoord().getX() == targetWithMarginX && node.getCoord().getY() == targetWithMarginY) {
                result.addAll(compressLine(node));

                //Рисуем стрелку к нужной стороне целевого объекта
                if (relation.getRelDirectionTo() == null || relation.getRelDirectionTo() == RelationDirection.LEFT) {
                    //Добавляем финальную точку куда приходит линия к основанию стрелки
                    int finalX = relation.getTo().getCoord().getX() - defaultArrowLength;
                    int finalY = relation.getTo().getCoord().getY() + defaultHeight / 2;
                    result.add(new Coord(finalX, finalY));
                    //Добавление точек для стрелки слева-направо
                    result.add(new Coord(finalX, finalY - defaultArrowWidth / 2));
                    result.add(new Coord(finalX + defaultArrowLength, finalY));
                    result.add(new Coord(finalX, finalY + defaultArrowWidth / 2));
                    result.add(new Coord(finalX, finalY));
                } else if (relation.getRelDirectionTo() == RelationDirection.TOP) {
                    //Добавляем финальную точку куда приходит линия к основанию стрелки
                    int finalX = relation.getTo().getCoord().getX() + defaultWidth / 2;
                    int finalY = relation.getTo().getCoord().getY() - defaultArrowLength;
                    result.add(new Coord(finalX, finalY));
                    //Добавление точек для стрелки слева-направо
                    result.add(new Coord(finalX + defaultArrowWidth / 2, finalY));
                    result.add(new Coord(finalX, finalY + defaultArrowLength));
                    result.add(new Coord(finalX - defaultArrowWidth / 2, finalY));
                    result.add(new Coord(finalX, finalY));
                } else if (relation.getRelDirectionTo() == RelationDirection.RIGHT) {
                    //Добавляем финальную точку куда приходит линия к основанию стрелки
                    int finalX = relation.getTo().getCoord().getX() + defaultWidth + defaultArrowLength;
                    int finalY = relation.getTo().getCoord().getY() + defaultHeight / 2;
                    result.add(new Coord(finalX, finalY));
                    //Добавление точек для стрелки слева-направо
                    result.add(new Coord(finalX, finalY - defaultArrowWidth / 2));
                    result.add(new Coord(finalX - defaultArrowLength, finalY));
                    result.add(new Coord(finalX, finalY + defaultArrowWidth / 2));
                    result.add(new Coord(finalX, finalY));
                } else if (relation.getRelDirectionTo() == RelationDirection.BOTTOM) {
                    //Добавляем финальную точку куда приходит линия к основанию стрелки
                    int finalX = relation.getTo().getCoord().getX() + defaultWidth / 2;
                    int finalY = relation.getTo().getCoord().getY() + defaultHeight + defaultArrowLength;
                    result.add(new Coord(finalX, finalY));
                    //Добавление точек для стрелки слева-направо
                    result.add(new Coord(finalX - defaultArrowWidth / 2, finalY));
                    result.add(new Coord(finalX, finalY - defaultArrowLength));
                    result.add(new Coord(finalX + defaultArrowWidth / 2, finalY));
                    result.add(new Coord(finalX, finalY));
                }

                return result;
            }

            //вверх
            tryStep(open, closed, node, node.getCoord().getX(), node.getCoord().getY() - stepY);
            //вниз
            tryStep(open, closed, node, node.getCoord().getX(), node.getCoord().getY() + stepY);
            //влево
            tryStep(open, closed, node, node.getCoord().getX() - stepX, node.getCoord().getY());
            //вправо
            tryStep(open, closed, node, node.getCoord().getX() + stepX, node.getCoord().getY());
        }

        return new ArrayList<>();
    }

    /**
     * Сжатие линии.
     * Сжатие множества соединенных точек линии в одну.
     */
    private List<Coord> compressLine(LineNode node) {
        List<Coord> res = new ArrayList<>();
        //Перебираем все точки конечной координаты node по родителям - тем самым восстанавливаем путь
        while (node.getParent() != null) {
            res.add(node.getCoord());
            //ToDo: дописать схлопывание линии в две точки по прямой вместо N точек
            node = node.getParent();
        }
        return res.reversed();
    }

    /**
     * Обновление очереди с приоритетами - точки куда можно пойти
     */
    private void tryStep(PriorityQueue<LineNode> open, Set<Coord> closed, LineNode parentNode, int x, int y) {
        //Условно можем идти только по положительны координатам
        if (x >= 0 && y >= 0) {
            //Проверяем что эту точку мы еще не посещали
            if (!closed.contains(new Coord(x, y))) {
                //Если точка доступна(не принадлежит объекту) - добавляем в очередь
                if (isPointAvailable(x, y)) {
                    open.add(new LineNode(new Coord(x, y), parentNode.getCost() + 1, parentNode));
                }
            }
        }
    }

    /**
     * Доступна ли точка для рисования линии ?
     * Доступна в том случае если не принадлежит объекту
     */
    private boolean isPointAvailable(int x, int y) {
        boolean hasIntersection = objects.stream()
                .anyMatch(item -> item.getCoord().getX() <= x && x <= item.getCoord().getX() + defaultWidth
                        && item.getCoord().getY() <= y && y <= item.getCoord().getY() + defaultHeight);
        if (hasIntersection) {
            return false;
        }
        return true;
    }

    /**
     * Рисование объекта
     * obj
     * x
     * y
     * width
     * height
     */
    private void drawObj(DiagObject obj, int width, int height) {
        try {
            int x = obj.getCoord().getX();
            int y = obj.getCoord().getY();
            String outData = "<rect x=\"" + x + "\" y=\"" + y + "\" width=\"" + width + "\" height=\"" + height + "\" rx=\"15\" style=\"fill:#eee;stroke-width:1;stroke:black\" />\n" +
                    "<text x=\"" + (x + width / 2) + "\" y=\"" + (y + height / 2) + "\" dominant-baseline=\"middle\" text-anchor=\"middle\">" + obj.getName() + "</text>    \n";
            Files.write(filePath, outData.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Добавление объекта в диаграмму (не отрисовка)
     */
    public void addObject(DiagObject obj) {
        objects.add(obj);
    }

    /**
     * Добавление связи между диаграмму (не отрисовка)
     */
    public void addRelation(Relation relation) {
        relations.add(relation);
    }
}
