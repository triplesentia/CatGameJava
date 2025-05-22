import items.Cat;
import items.Cell;
import items.Field;
import items.Side;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FieldTest {
    // Вспомогательный метод для создания тестового поля
    private static Cell getCell(List<Cell> cells, int x, int y) {
        for (Cell cell : cells) {
            if (cell.X() == x && cell.Y() == y) {
                return cell;
            }
        }
        return null;
    }
    private Field createTestField(int sizeX, int sizeY) {
        List<Cell> cells = new ArrayList<>();
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                cells.add(new Cell(x, y));
            }
        }

        for (Cell cell : cells) {
            int x = cell.X();
            int y = cell.Y();

            if (x > 0) cell.setNeighbor(Side.LEFT, getCell(cells, x - 1, y));
            if (x < sizeX - 1) cell.setNeighbor(Side.RIGHT, getCell(cells, x + 1, y));
            if (y > 0) cell.setNeighbor(Side.TOP, getCell(cells, x, y - 1));
            if (y < sizeY - 1) cell.setNeighbor(Side.BOTTOM, getCell(cells, x, y + 1));
        }
        return new Field(cells, sizeX, sizeY);
    }

    @Test
    public void createFieldWithNullCells() {
        Exception exception = assertThrows(NullPointerException.class,
                () -> new Field(null, 1, 1));
        assertEquals("Поле не может быть без ячеек!", exception.getMessage());
    }

    @Test
    public void createFieldWithLowWidth() {
        Cell cell = new Cell(0, 0);
        List<Cell> cells = new ArrayList<>();
        cells.add(cell);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Field(cells, 1, 1));
        assertEquals("Длина поля должна быть больше трех!", exception.getMessage());
    }

    @Test
    public void createFieldWithLowHeight() {
        Cell cell = new Cell(0, 0);
        List<Cell> cells = new ArrayList<>();
        cells.add(cell);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Field(cells, 5, 1));
        assertEquals("Ширина поля должна быть больше трех!", exception.getMessage());
    }

    @Test
    public void createValidField() {
        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cells.add(new Cell(i, j));
            }
        }
        Field field = new Field(cells, 4, 4);
        assertNotNull(field);
        assertEquals(4, field.getSideX());
        assertEquals(4, field.getSideY());
        assertEquals(16, field.getCells().size());
    }

    @Test
    public void freezeNullCell() {
        Field field = createTestField(5, 5);

        Exception exception = assertThrows(NullPointerException.class,
                () -> field.freezeCell(null, true));
        assertEquals("Невозможно заморозить пустую ячейку!", exception.getMessage());
    }

    @Test
    public void freezeCellOutOfField() {
        Field field = createTestField(5, 5);
        Cell outOfFieldCell = new Cell(10, 10);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> field.freezeCell(outOfFieldCell, true));
        assertEquals("Невозможно заморозить ячейку вне поля!", exception.getMessage());
    }

    @Test
    public void freezeFreezeCell() {
        Field field = createTestField(5, 5);
        Cell cell = field.getCells().get(0);

        field.freezeCell(cell, true);
        assertTrue(cell.isFrozen());
    }

    @Test
    public void unfreezeFreezeCell() {
        Field field = createTestField(5, 5);
        Cell cell = field.getCells().get(0);

        field.freezeCell(cell, true);
        field.freezeCell(cell, false);
        assertFalse(cell.isFrozen());
    }

    @Test
    public void isCatClosedWhenNoCat() {
        Field field = createTestField(5, 5);
        assertThrows(NullPointerException.class, field::isCatClosed);
    }

    @Test
    public void isCatClosedWhenFunctional() {
        Field field = createTestField(5, 5);
        Cell cell = field.getCells().get(12); // Центральная клетка
        Cat cat = new Cat(cell);
        field.setCat(cat);

        assertFalse(field.isCatClosed());
    }

    @Test
    public void isCatClosedWhenNotFunctional() {
        Field field = createTestField(5, 5);
        Cell cell = field.getCells().get(0);
        // Блокируем все соседние клетки
        for (Side side : Side.values()) {
            Cell neighbor = cell.getNeighbor(side);
            if (neighbor != null) {
                neighbor.block(true);
            }
        }
        Cat cat = new Cat(cell);
        field.setCat(cat);

        assertTrue(field.isCatClosed());
    }

    @Test
    public void isCatOnBorderWhenNoCat() {
        Field field = createTestField(5, 5);
        assertThrows(NullPointerException.class, field::isCatOnBorder);
    }

    @Test
    public void isCatOnBorderWhenOnBorder() {
        Field field = createTestField(5, 5);
        Cell borderCell = field.getCells().getFirst();
        Cat cat = new Cat(borderCell);
        field.setCat(cat);

        assertTrue(field.isCatOnBorder());
    }

    @Test
    public void isCatOnBorderWhenInCenter() {
        Field field = createTestField(5, 5);
        Cell centerCell = field.getCells().get(12);
        Cat cat = new Cat(centerCell);
        field.setCat(cat);

        assertFalse(field.isCatOnBorder());
    }

    @Test
    public void setNullCat() {
        Field field = createTestField(5, 5);

        Exception exception = assertThrows(NullPointerException.class,
                () -> field.setCat(null));
        assertEquals("Нельзя ставить пустого кота!", exception.getMessage());
    }

    @Test
    public void setCatWithCellOutOfField() {
        Field field = createTestField(5, 5);
        Cat cat = new Cat(new Cell(6, 6));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> field.setCat(cat));
        assertEquals("Кота невозможно заселить на поле, в которой нет его ячейки!", exception.getMessage());
    }

    @Test
    public void setValidCat() {
        Field field = createTestField(5, 5);
        Cell cell = field.getCells().getFirst();
        Cat cat = new Cat(cell);

        field.setCat(cat);
        assertEquals(cat, field.getCat());
        assertEquals(cell, cat.getCell());
    }

    @Test
    public void setCatAlreadyOnField() {
        Field field = createTestField(5, 5);
        Cell cell1 = field.getCells().get(0);
        Cell cell2 = field.getCells().get(1);
        Cat cat1 = new Cat(cell1);
        Cat cat2 = new Cat(cell2);

        field.setCat(cat1);
        field.setCat(cat2);

        assertEquals(cat2, field.getCat());
    }

    @Test
    public void unsetCatWhenNoCat() {
        Field field = createTestField(5, 5);
        assertDoesNotThrow(field::unsetCat);
    }

    @Test
    public void unsetCatWhenCatExists() {
        Field field = createTestField(5, 5);
        Cell cell = field.getCells().getFirst();
        Cat cat = new Cat(cell);
        field.setCat(cat);

        field.unsetCat();
        assertNull(field.getCat());
    }

    @Test
    public void getCells() {
        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cells.add(new Cell(i, j));
            }
        }
        Field field = new Field(cells, 4, 4);
        assertEquals(cells, field.getCells());
    }

    @Test
    public void getSideX() {
        Field field = createTestField(5, 7);
        assertEquals(5, field.getSideX());
    }

    @Test
    public void getSideY() {
        Field field = createTestField(5, 7);
        assertEquals(7, field.getSideY());
    }

    @Test
    public void getCatWhenNoCat() {
        Field field = createTestField(5, 5);
        assertNull(field.getCat());
    }

    @Test
    public void getCatWhenCatExists() {
        Field field = createTestField(5, 5);
        Cell cell = field.getCells().get(0);
        Cat cat = new Cat(cell);
        field.setCat(cat);

        assertEquals(cat, field.getCat());
    }
}