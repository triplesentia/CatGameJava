package items;

import java.util.List;

public class Field {
    private final List<Cell> cells;
    private final int side_x;
    private final int side_y;
    private Cat cat;

    /**
     * Инициализирует игровое Поле с заданными ячейками и размерами.
     *
     * @param cellsList Список ячеек
     * @param sideX  Длина Поля (X-координата). Требование: sideX > 3.
     * @param sideY  Ширина Поля (Y-координата). Требование: sideY > 3.
     *
     * @throws NullPointerException     При отсутствии Ячеек в Поле
     * @throws IllegalArgumentException При нарушении условий sideX > 3 или sideY > 3.
     */
    public Field(List<Cell> cellsList, int sideX, int sideY) {
        if (cellsList == null) throw new NullPointerException("Поле не может быть без ячеек!");
        if (sideX <= 3) throw new IllegalArgumentException("Длина поля должна быть больше трех!");
        if (sideY <= 3) throw new IllegalArgumentException("Ширина поля должна быть больше трех!");
        cells = cellsList;
        side_x = sideX;
        side_y = sideY;
    }

    public List<Cell> getCells() { return cells; }
    public int getSideX() { return side_x; }
    public int getSideY() { return side_y; }
    public Cat getCat() { return cat; }

    /**
     * Замораживает/размораживает Ячейку на Поле
     *
     * @param cell Ячейка для заморозки/разморозки
     * @param freezeFlag Флаг заморозки
     *
     * @throws NullPointerException     Если Ячейка пустая
     * @throws IllegalArgumentException Если Ячейки нет в Поле
     */
    public void freezeCell (Cell cell, Boolean freezeFlag){
        if (cell == null) throw new NullPointerException("Невозможно заморозить пустую ячейку!");
        if (cell.X() < 0 || cell.Y() < 0 || cell.X() >= this.side_x || cell.Y() >= this.side_y) throw new IllegalArgumentException("Невозможно заморозить ячейку вне поля!");
        cell.freeze(freezeFlag);
    }

    /**
     * Проверяет закрыт ли Кот
     */
    public boolean isCatClosed () { return !(cat.isFunctional());}

    /**
     * Проверяет находится ли Кот рядом с краем Поля
     */
    public boolean isCatOnBorder () {return cat.getCell().X() == 0 || cat.getCell().X() == side_x - 1 || cat.getCell().Y() == 0 || cat.getCell().Y() == side_y - 1;}

    /**
     * Устанавливает Кота на Поле
     *
     * @param newCat Кот
     *
     * @throws NullPointerException     При отсутствии Кота
     * @throws IllegalArgumentException При отсутствии на Поле Ячейки, в которую заселен Кот
     */
    public void setCat (Cat newCat) {
        if (newCat == null) throw new NullPointerException("Нельзя ставить пустого кота!");
        boolean setCat = false;
        for (Cell cell : cells) {
            if (cell.X() == newCat.getCell().X() && cell.Y() == newCat.getCell().Y()) {
                setCat = true;
            }
        }

        if (!setCat) throw new IllegalArgumentException("Кота невозможно заселить на поле, в которой нет его ячейки!");
        cat = newCat;
    }

    /**
     * Удаляет кота с поля, если он там находится.
     *
     * @throws IllegalStateException Если поле заблокировано или уничтожено,
     *                              и невозможно изменить состояние кота.
     */
    public void unsetCat() {
        if (cat == null) { return; }
        cat = null;
    }
}