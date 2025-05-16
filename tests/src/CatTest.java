import items.Cat;
import items.Cell;
import items.Field;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CatTest {
    @Test
    public void createCatWithNullCell (){
        Exception _exception = assertThrows(NullPointerException.class,
                () -> new Cat(null));
        assertEquals("Кот не может быть в пустой ячейке!", _exception.getMessage());
    }

    @Test
    public void setCatInNullCell (){
        Cat cat = new Cat(new Cell(0,0));
        Exception _exception = assertThrows(NullPointerException.class,
                () -> cat.setCell(null));
        assertEquals("Кот не может быть в пустой ячейке!", _exception.getMessage());
    }

    @Test
    public void moveToUnknownSide (){
        Cat cat = new Cat(new Cell(0,0));
        Exception _exception = assertThrows(NullPointerException.class,
                () -> cat.move(null));
        assertEquals("Кот не может двигаться в неизвестном направлении!", _exception.getMessage());
    }
}
