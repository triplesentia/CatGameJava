import items.Cat;
import items.Cell;
import org.junit.jupiter.api.Test;
import items.Field;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FieldTest {
    @Test
    public void createFieldWithNullCells (){

        Exception _exception = assertThrows(NullPointerException.class,
                () -> new Field(null, 1,1));
        assertEquals("Поле не может быть без ячеек!", _exception.getMessage());
    }

    @Test
    public void createFieldWithLowWidth (){
        Cell cell = new Cell(0,0);
        List<Cell> _cells = new ArrayList<Cell>();
        _cells.add(cell);

        Exception _exception = assertThrows(IllegalArgumentException.class,
                () -> new Field(_cells, 1,1));
        assertEquals("Длина поля должна быть больше трех!", _exception.getMessage());
    }

    @Test
    public void createFieldWithLowHeight (){
        Cell cell = new Cell(0,0);
        List<Cell> _cells = new ArrayList<Cell>();
        _cells.add(cell);

        Exception _exception = assertThrows(IllegalArgumentException.class,
                () -> new Field(_cells, 5,1));
        assertEquals("Ширина поля должна быть больше трех!", _exception.getMessage());
    }

    @Test
    public void freezeNullCell (){
        boolean exp = true;
        Cell cell = new Cell(0,0);
        List<Cell> _cells = new ArrayList<Cell>();
        _cells.add(cell);
        Field _field = new Field(_cells, 5,5);

        Exception _exception = assertThrows(NullPointerException.class, ()-> _field.freezeCell(null, true));
        assertEquals("Невозможно заморозить пустую ячейку!", _exception.getMessage());
    }

    @Test
    public void freezeFreezeCell (){
        boolean exp = true;
        Cell cell = new Cell(0,0);
        List<Cell> _cells = new ArrayList<Cell>();
        _cells.add(cell);
        Field _field = new Field(_cells, 5,5);
        _field.freezeCell(cell, true);

        boolean actual = _field.freezeCell(cell, true);
        assertEquals(exp, actual);
    }

    @Test
    public void freezeUnfreezeCell(){
        boolean exp = true;
        Cell cell = new Cell(0,0);
        List<Cell> _cells = new ArrayList<Cell>();
        _cells.add(cell);
        Field _field = new Field(_cells, 5,5);
        _field.freezeCell(cell, true);

        boolean actual = _field.freezeCell(cell, true);
        assertEquals(exp, actual);
    }

    @Test
    public void unfreezeFreezeCell(){
        boolean exp = false;
        Cell cell = new Cell(0,0);
        List<Cell> _cells = new ArrayList<Cell>();
        _cells.add(cell);
        Field _field = new Field(_cells, 5,5);
        _field.freezeCell(cell, true);

        boolean actual = _field.freezeCell(cell, false);
        assertEquals(exp, actual);
    }

    @Test
    public void setNullCat(){
        List<Cell> _cells = new ArrayList<Cell>();
        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 5; j++){
                _cells.add(new Cell(i,j));
            }
        }
        Field _field = new Field(_cells, 5,5);

        Exception _exception = assertThrows(NullPointerException.class, ()-> _field.setCatOnField(null));
        assertEquals("Нельзя ставить пустого кота!", _exception.getMessage());
    }

    @Test
    public void setCatWithCellOutOfField(){
        List<Cell> _cells = new ArrayList<>(25);  // Инициализация массива 25
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                _cells.add(new Cell(i, j));
            }
        }
        Field _field = new Field(_cells, 5,5);
        Cat _cat = new Cat(new Cell(6,6));

        Exception _exception = assertThrows(IllegalArgumentException.class, ()-> _field.setCatOnField(_cat));
        assertEquals("Кота невозможно заселить на поле, в которой нет его ячейки!", _exception.getMessage());
    }
}
