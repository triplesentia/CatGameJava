package items;

import java.util.List;

public class Field {
    List<Cell> CELLS;
    int SIDE_X;
    int SIDE_Y;
    Cat CAT;

    public Field(List<Cell> cells, int sideX, int sideY) {
        if (cells == null) throw new NullPointerException("Поле не может быть без ячеек!");
        if (sideX <= 3) throw new IllegalArgumentException("Длина поля должна быть больше трех!");
        if (sideY <= 3) throw new IllegalArgumentException("Ширина поля должна быть больше трех!");
        CELLS = cells;
        SIDE_X = sideX;
        SIDE_Y = sideY;
    }

    public boolean freezeCell (Cell _cell, Boolean _freeze){
        if (_cell == null) throw new NullPointerException("Невозможно заморозить пустую ячейку!");
        if (_cell.X() < 0 || _cell.Y() < 0 || _cell.X() >= this.SIDE_X || _cell.Y() >= this.SIDE_Y) throw new IllegalArgumentException("Невозможно заморозить ячейку вне поля!");
        return _cell.freeze(_freeze);
    }

    public boolean isCatClosed () { return !(CAT.isCatFunctional());}

    public boolean isCatOnBorder () {return CAT.getPosX() == 0 || CAT.getPosX() == SIDE_X - 1 || CAT.getPosY() == 0 || CAT.getPosY() == SIDE_Y - 1;}

    public void setCatOnField (Cat _cat) {
        if (_cat == null) throw new NullPointerException("Нельзя ставить пустого кота!");
        boolean setCat = false;
        for (Cell cell : CELLS) {
            if (cell.X() == _cat.getPosX() && cell.Y() == _cat.getPosY()) {
                setCat = true;
            }
        }

        if (!setCat) throw new IllegalArgumentException("Кота невозможно заселить на поле, в которой нет его ячейки!");
        CAT = _cat;
    }

}