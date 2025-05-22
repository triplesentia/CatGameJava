package items;

import java.util.EnumMap;
import java.util.Map;

public class Cell {
    private boolean frozen = false;
    private boolean occupied = false; // TODO нужен ли этот флаг если можно пользоваться геттером Кота
    private boolean destroyed = false;
    private boolean blocked = false;

    private int row_x, col_y;
    private Cat cat;

    private final Map<Side, Cell> neighbors = new EnumMap<>(Side.class);

    /**
     * Создает новую Клетку с указанными координатами.
     *
     * @param row  Координата X (строка) клетки.
     * @param col  Координата Y (столбец) клетки.
     *
     * @throws IllegalArgumentException Если передана отрицательная координата.
     */
    public Cell(int row, int col) {
        if (row < 0) throw new IllegalArgumentException("X = " + row + "\nX координата должна быть больше или равна нулю");
        if (col < 0) throw new IllegalArgumentException("Y = " + col + "\nY координата должна быть больше или равна нулю");
        this.row_x = row;
        this.col_y = col;
    }

    public boolean isFrozen() { return frozen; }
    public boolean isOccupied() { return occupied; }
    public boolean isDestroyed() { return destroyed; }
    public boolean isBlocked() { return blocked; }
    public Cat getCat() { return this.cat; }

    public int X() { return this.row_x; }
    public int Y() { return this.col_y; }

    /**
     * Установить соседа для клетки
     *
     * @param side сторона, для которой нужно установить соседа
     * @param neighbor клетка-сосед
     */
    public void setNeighbor(Side side, Cell neighbor) { neighbors.put(side, neighbor); }

    /**
     * Получить соседа для определенной стороны
     */
    public Cell getNeighbor(Side side) { return neighbors.get(side); }

    // TODO возможно убрать этот метод
    public Map<Side, Cell> getNeighbors() {
        return new EnumMap<>(neighbors);
    }

    /**
     * Установить Кота в Ячейку
     *
     * @param newCat Кот, которого нужно поставить в клетку
     */
    public void setCat(Cat newCat) {
        if (destroyed) throw new IllegalStateException("Клетка уничтожена!");
        if (blocked) throw new IllegalStateException("Клетка заблокирована!");
        if (frozen) throw new IllegalStateException("Клетка заморожена!");
        if (newCat == null) throw new NullPointerException("Кот не может быть null!");

        if (this.cat == newCat) return;

        if (this.cat != null) this.unsetCat();

        this.cat = newCat;
        this.occupied = true;
        if (newCat.getCell() != this) newCat.setCell(this);
    }

    /**
     * Убрать Кота из Ячейки
     */
    public void unsetCat() {
        if (this.cat == null) return;

        Cat oldCat = this.cat;
        this.cat = null;
        this.occupied = false;
        if (oldCat.getCell() == this) oldCat.unsetCell();
    }

    /**
     * Заблокировать Ячейку.<br>
     * Блокирование Ячейки, для последующих перемещений Кота.
     *
     * @param blockedFlag флаг блокирования
     *                   {@code true} - ячейка блокируется
     *                   {@code false} - ячейка без блокировки
     * @throws RuntimeException Если Ячейка уничтожена
     */
    public void block(boolean blockedFlag) {
        if (destroyed) throw new RuntimeException("Невозможно заблокировать/разблокировать клетку! Клетка уничтожена");
        blocked = blockedFlag;
    }

    /**
     * Заморозить Ячейку.<br>
     * Замораживание Ячейки, для последующих перемещений Кота.<br>
     * Предварительно метод для системного замораживания
     *
     * @param freezeFlag флаг заморозки
     *                   {@code true} - ячейка замораживается
     *                   {@code false} - ячейка размораживается
     * @throws RuntimeException Если Ячейка уничтожена
     */
    public void freeze(boolean freezeFlag) {
        if (destroyed) throw new RuntimeException("Невозможно заморозить/разморозить клетку! Клетка уничтожена");
        frozen = freezeFlag;
    }

    /**
     * Уничтожить Ячейку
     */
    public void destroy() {
        destroyed = true;
        row_x = -1;
        col_y = -1;

        for (Cell neighbor : neighbors.values()) {
            if (neighbor != null) {
                neighbor.neighbors.entrySet().removeIf(entry -> entry.getValue() == this);
            }
        }
        neighbors.clear();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cell other = (Cell) obj;
        return row_x == other.row_x &&
                col_y == other.col_y &&
                frozen == other.frozen &&
                occupied == other.occupied &&
                destroyed == other.destroyed &&
                blocked == other.blocked &&
                cat == other.cat;
    }
}