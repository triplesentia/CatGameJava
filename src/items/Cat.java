package items;

public class Cat {
    private Cell cell;

    /**
     * Создает нового Кота в указанной Ячейке.
     *
     * @param newCell  Ячейка, в которой стоит Кот.
     * @throws NullPointerException Если передана пустая Ячейка.
     */
    public Cat(Cell newCell) {
        if (newCell == null) throw new NullPointerException("Кот не может быть в пустой ячейке!");
        cell = newCell;
        cell.setCat(this);
    }

    /**
     * Получить Ячейку, в которой находится Кот
     */
    public Cell getCell() {return cell;}

    /**
     * Установить Ячейку для Кота
     *
     * @param newCell Клетка, в которой находится Кот
     * @throws NullPointerException Если Ячейка уничтожена
     */
    public void setCell(Cell newCell) {
        if (this.cell == newCell) return;

        if (newCell == null) throw new NullPointerException("Кот не может быть в пустой ячейке!");

        if (this.cell != null) this.unsetCell();

        this.cell = newCell;
        if (this.cell != null && newCell.getCat() != this) newCell.setCat(this);
    }

    /**
     * Убрать Кота из Ячейки
     */
    public void unsetCell() {
        if (this.cell == null) return;

        Cell oldCell = this.cell;
        this.cell = null;
        if (oldCell.getCat() == this) oldCell.unsetCat();
    }

    /**
     * Дееспособен ли Кот<br>
     * Проверка есть ли у Кота возможность перемещения в соседние Ячейки.
     */
    public boolean isFunctional() {
        for (Side side : Side.values()) {
            Cell nextCell = cell.getNeighbor(side);
            if (nextCell != null && !nextCell.isBlocked()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Перемещает кота в соседнюю ячейку в указанном направлении.
     *
     * @param side Направление движения.
     *
     * @throws NullPointerException Если направление равно null.
     * @throws IllegalStateException Если соседняя ячейка недоступна (например, заблокирована или уничтожена).
     *
     */
    public void move(Side side) {
        Cell NextCell = cell.getNeighbor(side);
        if (side != null) {
            if (NextCell!=null) {
                this.cell.unsetCat();
                NextCell.setCat(this);
            }
        } else throw new NullPointerException("Кот не может двигаться в неизвестном направлении!");
    }
}
