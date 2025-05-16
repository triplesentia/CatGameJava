package items;

public class Cat {
    Cell CELL;
    Field FIELD;

    public Cat(Cell _cell) {
        if (_cell == null) throw new NullPointerException("Кот не может быть в пустой ячейке!");
//        if (_field == null) throw new NullPointerException("Кот не может быть в пустом поле!");
        CELL = _cell;
        CELL.setCat(this);
    }
    
    public void setCell(Cell _cell) {
        if (_cell == null) throw new NullPointerException("Кот не может быть в пустой ячейке!");
        this.CELL = _cell;
    }

    public void unsetCell() {this.CELL = null;}

    public boolean isCatFunctional() {
        for (Side side : Side.values()) {
            Cell nextCell = CELL.getNeighbor(side);
            if (nextCell != null && !nextCell.IS_BLOCKED) {
                return true;
            }
        }
        return false;
    }

    public void move(Side side) {
        Cell NextCell = CELL.getNeighbor(side);
        if (side != null) {
            if (NextCell!=null) {
                this.CELL.unsetCat();
                NextCell.setCat(this);
            }
        } else throw new NullPointerException("Кот не может двигаться в неизвестном направлении!");
    }
}
