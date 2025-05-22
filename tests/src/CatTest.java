import items.Cat;
import items.Cell;
import items.Side;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CatTest {
    @Test
    public void createCatWithNullCell() {
        Exception exception = assertThrows(NullPointerException.class,
                () -> new Cat(null));
        assertEquals("Кот не может быть в пустой ячейке!", exception.getMessage());
    }

    @Test
    public void setCatInNullCell() {
        Cat cat = new Cat(new Cell(0, 0));
        Exception exception = assertThrows(NullPointerException.class,
                () -> cat.setCell(null));
        assertEquals("Кот не может быть в пустой ячейке!", exception.getMessage());
    }

    @Test
    public void moveToUnknownSide() {
        Cat cat = new Cat(new Cell(0, 0));
        Exception exception = assertThrows(NullPointerException.class,
                () -> cat.move(null));
        assertEquals("Кот не может двигаться в неизвестном направлении!", exception.getMessage());
    }

    @Test
    public void constructorSetsCellAndCat() {
        Cell cell = new Cell(1, 1);
        Cat cat = new Cat(cell);

        assertEquals(cell, cat.getCell());
        assertEquals(cat, cell.getCat());
    }

    @Test
    public void getCellReturnsCorrectCell() {
        Cell cell = new Cell(2, 2);
        Cat cat = new Cat(cell);

        assertEquals(cell, cat.getCell());
    }

    @Test
    public void setCellUpdatesCellCorrectly() {
        Cell initialCell = new Cell(0, 0);
        Cell newCell = new Cell(1, 1);
        Cat cat = new Cat(initialCell);

        cat.setCell(newCell);

        assertEquals(newCell, cat.getCell());
        assertNull(initialCell.getCat());
        assertEquals(cat, newCell.getCat());
    }

    @Test
    public void setCellToSameCell() {
        Cell cell = new Cell(0, 0);
        Cat cat = new Cat(cell);

        cat.setCell(cell);

        assertEquals(cell, cat.getCell());
        assertEquals(cat, cell.getCat());
    }

    @Test
    public void unsetCellRemovesCatFromCell() {
        Cell cell = new Cell(0, 0);
        Cat cat = new Cat(cell);

        cat.unsetCell();

        assertNull(cat.getCell());
        assertNull(cell.getCat());
    }

    @Test
    public void isFunctionalWhenMovementPossible() {
        Cell cell = new Cell(0, 0);
        Cell neighbor = new Cell(0, 1);
        cell.setNeighbor(Side.RIGHT, neighbor);
        Cat cat = new Cat(cell);

        assertTrue(cat.isFunctional());
    }

    @Test
    public void isFunctionalWhenNoMovementPossible() {
        Cell cell = new Cell(0, 0);
        Cat cat = new Cat(cell);

        assertFalse(cat.isFunctional());
    }

    @Test
    public void moveSuccessfullyChangesCells() {
        Cell initialCell = new Cell(0, 0);
        Cell targetCell = new Cell(0, 1);
        initialCell.setNeighbor(Side.RIGHT, targetCell);
        Cat cat = new Cat(initialCell);

        cat.move(Side.RIGHT);

        assertEquals(targetCell, cat.getCell());
        assertEquals(cat, targetCell.getCat());
        assertNull(initialCell.getCat());
    }

    @Test
    public void moveToBlockedCell() {
        Cell initialCell = new Cell(0, 0);
        Cell blockedCell = new Cell(0, 1);
        blockedCell.block(true);
        initialCell.setNeighbor(Side.RIGHT, blockedCell);
        Cat cat = new Cat(initialCell);

        assertThrows(IllegalStateException.class, () -> cat.move(Side.RIGHT));
    }

    @Test
    public void moveToNullNeighbor() {
        Cell initialCell = new Cell(0, 0);
        Cat cat = new Cat(initialCell);
        cat.move(Side.RIGHT);
        assertEquals(initialCell, cat.getCell());
    }
}