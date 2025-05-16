import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import items.Cell;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CellTests{
    protected boolean IS_DESTROYED = false;
    protected int ROW_X, COL_Y;

//    public CellTests(int _row, int _col) {
//        super(_row, _col);
//    }

    @Test
    public void RowCoordinateLessZero (){
        int _row = -1;
        int _col = 0;

        Exception _exception = assertThrows(IllegalArgumentException.class,
                () -> new Cell(_row, _col));
        assertEquals("X = -1\n" +
                "X координата должна быть больше или равна нулю", _exception.getMessage());
    }

    @Test
    public void ColumnCoordinateLessZero (){
        int _row = 0;
        int _col = -1;

        Exception _exception = assertThrows(IllegalArgumentException.class,
                () -> new Cell(_row, _col));
        assertEquals("Y = -1\n" +
                "Y координата должна быть больше или равна нулю", _exception.getMessage());
    }

    @Test
    public void RowCoordinateGreaterZero (){
        int _row = 10;
        int _col = 0;

        Cell _expCell = new Cell(10, 0);
        Cell _cell = new Cell(_row, _col);
        assertEquals(_expCell, _cell);
    }

    @Test
    public void ColCoordinateGreaterZero (){
        int _row = 0;
        int _col = 130;

        Cell _expCell = new Cell(0, 130);
        Cell _cell = new Cell(_row, _col);
        assertEquals(_expCell, _cell);
    }

    @Test
    public void checkDestroy (){
//        int _row = 75;
//        int _col = 50;
//
//        Cell _expCell = new Cell(1,1);
//        _expCell.IS_DESTROYED = true;
//        _expCell.ROW_X = -1;
//        _expCell.COL_Y = -1;
//
//        Cell _cell = new Cell(_row, _col);
//        _cell.destroy();
//
//        assertEquals(_expCell, _cell);
    }
}
