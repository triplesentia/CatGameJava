package game.model.field;

import org.jetbrains.annotations.NotNull;
import game.model.events.*;

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
                cell.addCellActionListener(new CellObserver());
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

    //region КОТ

    /**
     * Получить кота на поле.
     *
     * @return кот на поле, или null если ни один кот не найден.
     */
    public Cat getCat() {
        for (var entry : cells.entrySet()) {
            Cat cat = entry.getValue().getObject();
            if (cat != null) {
                return cat;
            }
        }
        return null;
    }

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
    public Cell getCell(@NotNull Point point) {
        return cells.get(point);
    }

    /**
     * Проверяет, находится ли ячейка на периметре (краю) поля.
     *
     * @param cell ячейка для проверки
     * @return true если ячейка на периметре, иначе false
     */
    public boolean isPerimeterCell(@NotNull Cell cell) {
        // Найти координату этой ячейки
        for (Map.Entry<Point, Cell> entry : cells.entrySet()) {
            if (entry.getValue() == cell) {
                Point p = entry.getKey();
                int n = sideLength - 1;
                // На периметре, если x == -n, x == n, y == -n, y == n, или x + y == -n, x + y == n
                int x = p.getX();
                int y = p.getY();
                return x == -n || x == n || y == -n || y == n || (x + y) == -n || (x + y) == n;
            }
        }
        return false; // Ячейка не найдена на этом поле
    }

    //endregion

    //region СЛУШАТЕЛИ

    private class CellObserver implements CellActionListener {
        @Override
        public void cellStateChanged(@NotNull CellActionEvent event) {
            fireFieldCellStateChanged(event);
        }
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
    public void removeFieldActionListener(FieldActionListener listener) {
        fieldListListener.remove(listener);
    }

    /**
     * Оповестить слушателей {@link Field#fieldListListener}, что ячейка изменила состояние.
     *
     * @param event событие ячейки.
     */
    private void fireFieldCellStateChanged(@NotNull CellActionEvent event) {
        FieldActionEvent fieldEvent = new FieldActionEvent(this);
        fieldEvent.setCell(event.getCell());
        for (FieldActionListener listener : fieldListListener) {
            listener.fieldCellStateChanged(fieldEvent);
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

        return sideLength == field.sideLength &&
                Objects.equals(cells, field.cells);
    }

    @Override
    public String toString() {
        return "Field{" + "cells=" + cells + ", sideLength=" + sideLength + '}';
    }

    //endregion
}
