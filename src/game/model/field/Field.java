package game.model.field;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Поле.
 */
public class Field {

    //region КОНСТРУКТОРЫ

    /**
     * Конструктор.
     *
     * @param sideLength длина стороны шестиугольника в ячейках
     * @throws IllegalArgumentException если передана невалидная сторона
     */
    public Field(int sideLength) {
        if (sideLength <= 0) {
            throw new IllegalArgumentException("Field side must be more than 0");
        }

        this.sideLength = sideLength;

        buildField();
    }

    /**
     * Построить игровое поле.
     */
    private void buildField() {
        for (int x = -sideLength + 1; x < sideLength; x++) {
            for (int y = -sideLength + 1; y < sideLength; y++) {
                if (Math.abs(x + y) > sideLength - 1) continue;
                Point p = new Point(x, y);
                Cell cell = new Cell();
                cells.put(p, cell);
            }
        }

        for (Map.Entry<Point, Cell> entry : cells.entrySet()) {
            Point p = entry.getKey();
            Cell cell = entry.getValue();
            Map<Direction, Cell> neighborCells = new EnumMap<>(Direction.class);

            for (Direction dir : Direction.values()) {
                Point np = p.to(dir, 1);
                Cell neighbor = cells.get(np);
                if (neighbor != null) {
                    neighborCells.put(dir, neighbor);
                }
            }

            boolean success = cell.setNeighbors(neighborCells);
            assert success : "Cell (" + cell + ") not successfully set";
        }
    }

    //endregion

    //region СВОЙСТВА

    //region СТОРОНА

    /**
     * Сторона поля.
     */
    private final int sideLength;

    /**
     * Получить длину стороны поля {@link Field#sideLength}.
     *
     * @return сторона поля.
     */
    public int getSideLength() {
        return sideLength;
    }

    //endregion

    //endregion

    //region ЯЧЕЙКИ

    /**
     * Ячейки поля.
     */
    private final Map<Point, Cell> cells = new HashMap<>();

    /**
     * Получить ячейку по заданной координате.
     *
     * @param point координата.
     * @return ячейка.
     */
    public AbstractCell getCell(@NotNull Point point) {
        return cells.get(point);
    }

    //endregion

    //region РОБОТ

    /**
     * Получить робота на поле.
     *
     * @return робот на поле.
     */
    public Robot getRobot() {
        for (var cell : cells.entrySet()) {
            Robot robot = cell.getValue().getBigObject();
            if (robot != null) {
                return robot;
            }
        }
        return null;
    }

    //endregion

    //region СИГНАЛЫ

    /**
     * Список слушателей, подписанных на события поля.
     */
    private final ArrayList<FieldActionListener> fieldListListener = new ArrayList<>();

    /**
     * Добавить нового слушателя за событиями поля.
     *
     * @param listener слушатель.
     */
    public void addFieldActionListener(FieldActionListener listener) {
        fieldListListener.add(listener);
    }

    /**
     * Удалить слушателя за событиями поля.
     *
     * @param listener слушатель.
     */
    public void removeFieldCellActionListener(FieldActionListener listener) {
        fieldListListener.remove(listener);
    }

    /**
     * Оповестить слушателей {@link Field#fieldListListener}, что робот телепортировался.
     *
     * @param teleport телепорт.
     */
    private void fireRobotIsTeleported(@NotNull AbstractCell teleport) {
        FieldActionEvent event = new FieldActionEvent(this);
        event.setRobot(((ExitCell) teleport).getTeleportedRobot());
        event.setTeleport(teleport);

        for (FieldActionListener listener : fieldListListener) {
            listener.robotIsTeleported(event);
        }
    }

    //endregion

    //region OBJECT

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Field field = (Field) o;

        return sideLength == field.sideLength && height == field.height &&
                Objects.equals(cells, field.cells) &&
                Objects.equals(exitCell, field.exitCell);
    }

    @Override
    public String toString() {
        return "Field{" + "cells=" + cells + ", width=" + sideLength + ", height=" + height + ", exitPoint=" + exitCell + '}';
    }

    //endregion
}
