import items.Side;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import items.Cell;
import items.Cat;
import java.util.Map;

public class CellTest {
    private Cell cell;
    private Cat cat;

    @BeforeEach
    public void setUp() {
        cell = new Cell(0, 0);
        cat = new Cat(cell); // Теперь кот создается с ячейкой
    }

    @Test
    public void constructorNegativeRow() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Cell(-1, 0));
        assertEquals("X = -1\nX координата должна быть больше или равна нулю", exception.getMessage());
    }

    @Test
    public void constructorNegativeColumn() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Cell(0, -1));
        assertEquals("Y = -1\nY координата должна быть больше или равна нулю", exception.getMessage());
    }

    @Test
    public void isFrozenInitiallyFalse() {
        assertFalse(cell.isFrozen());
    }

    @Test
    public void isOccupiedInitiallyFalse() {
        Cell emptyCell = new Cell(1, 1);
        assertFalse(emptyCell.isOccupied());
    }

    @Test
    public void isDestroyedInitiallyFalse() {
        assertFalse(cell.isDestroyed());
    }

    @Test
    public void isBlockedInitiallyFalse() {
        assertFalse(cell.isBlocked());
    }

    @Test
    public void getCatWithCat() {
        assertEquals(cat, cell.getCat());
    }

    @Test
    public void X_ReturnsCorrectRow() {
        assertEquals(0, cell.X());
    }

    @Test
    public void Y_ReturnsCorrectColumn() {
        assertEquals(0, cell.Y());
    }

    @Test
    public void setNeighborAddsNeighborCorrectly() {
        Cell neighbor = new Cell(0, 1);
        cell.setNeighbor(Side.RIGHT, neighbor);
        assertEquals(neighbor, cell.getNeighbor(Side.RIGHT));
    }

    @Test
    public void getNeighborNoNeighbor() {
        assertNull(cell.getNeighbor(Side.LEFT));
    }

    @Test
    public void getNeighborsReturnsCopyOfNeighbors() {
        Cell neighbor = new Cell(0, 1);
        cell.setNeighbor(Side.RIGHT, neighbor);

        Map<Side, Cell> neighbors = cell.getNeighbors();
        assertEquals(neighbor, neighbors.get(Side.RIGHT));
        
        neighbors.remove(Side.RIGHT);
        assertNotNull(cell.getNeighbor(Side.RIGHT));
    }

    @Test
    public void setCatSetsCatCorrectly() {
        Cell newCell = new Cell(1, 0);
        Cat newCat = new Cat(newCell);
        
        newCell.setCat(newCat);
        assertEquals(newCat, newCell.getCat());
        assertTrue(newCell.isOccupied());
    }

    @Test
    public void setCatNullCat() {
        assertThrows(NullPointerException.class, () -> cell.setCat(null));
    }

    @Test
    public void setCatDestroyedCell() {
        cell.destroy();
        Cell newCell = new Cell(1, 1);
        Cat newCat = new Cat(newCell);
        assertThrows(RuntimeException.class, () -> cell.setCat(newCat));
    }

    @Test
    public void setCatBlockedCell() {
        cell.block(true);
        Cell newCell = new Cell(1, 1);
        Cat newCat = new Cat(newCell);
        assertThrows(RuntimeException.class, () -> cell.setCat(newCat));
    }

    @Test
    public void setCatFrozenCell() {
        cell.freeze(true);
        Cell newCell = new Cell(1, 1);
        Cat newCat = new Cat(newCell);
        assertThrows(RuntimeException.class, () -> cell.setCat(newCat));
    }

    @Test
    public void setCatSameCa() {
        cell.setCat(cat);
        assertEquals(cat, cell.getCat());
    }

    @Test
    public void unsetCatRemovesCatCorrectly() {
        cell.unsetCat();
        assertNull(cell.getCat());
        assertFalse(cell.isOccupied());
        assertNull(cat.getCell());
    }

    @Test
    public void unsetCatNoCat() {
        Cell emptyCell = new Cell(1, 1);
        emptyCell.unsetCat();
        assertNull(emptyCell.getCat());
    }

    @Test
    public void blockSetsBlockedFlagCorrectly() {
        cell.block(true);
        assertTrue(cell.isBlocked());

        cell.block(false);
        assertFalse(cell.isBlocked());
    }

    @Test
    public void blockDestroyedCell() {
        cell.destroy();
        assertThrows(RuntimeException.class, () -> cell.block(true));
    }

    @Test
    public void freezeSetsFrozenFlagCorrectly() {
        cell.freeze(true);
        assertTrue(cell.isFrozen());

        cell.freeze(false);
        assertFalse(cell.isFrozen());
    }

    @Test
    public void freezeDestroyedCell() {
        cell.destroy();
        assertThrows(RuntimeException.class, () -> cell.freeze(true));
    }

    @Test
    public void destroySetsDestroyedFlagAndClearsNeighbors() {
        Cell neighbor = new Cell(0, 1);
        cell.setNeighbor(Side.RIGHT, neighbor);
        neighbor.setNeighbor(Side.LEFT, cell);

        cell.destroy();
        assertTrue(cell.isDestroyed());
        assertEquals(-1, cell.X());
        assertEquals(-1, cell.Y());
        assertTrue(cell.getNeighbors().isEmpty());
        assertNull(neighbor.getNeighbor(Side.LEFT));
    }

    @Test
    public void equalsSameObject() {
        assertEquals(cell, cell);
    }

    @Test
    public void equalsNullObject() {
        assertNotEquals(cell, null);
    }

    @Test
    public void equalsDifferentClass() {
        assertNotEquals(cell, new Object());
    }

    @Test
    public void equalsDifferentCoordinates() {
        Cell other = new Cell(1, 1);
        assertNotEquals(cell, other);
    }

    @Test
    public void equalsDifferentState() {
        Cell other = new Cell(0, 0);
        other.block(true);
        assertNotEquals(cell, other);
    }

    @Test
    public void equalsDifferentCat() {
        Cell cell1 = new Cell(0, 0);
        Cat cat1 = new Cat(cell1);

        Cell cell2 = new Cell(0, 0);
        Cat cat2 = new Cat(cell2);

        assertNotEquals(cell1, cell2);
    }

    @Test
    public void catConstructorNullCell() {
        assertThrows(NullPointerException.class, () -> new Cat(null));
    }

    @Test
    public void catConstructorSetsCellAndCatCorrectly() {
        Cell newCell = new Cell(1, 1);
        Cat newCat = new Cat(newCell);

        assertEquals(newCell, newCat.getCell());
        assertEquals(newCat, newCell.getCat());
        assertTrue(newCell.isOccupied());
    }
}