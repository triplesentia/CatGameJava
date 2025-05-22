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
    public void constructor_NegativeRow_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Cell(-1, 0));
        assertEquals("X = -1\nX координата должна быть больше или равна нулю", exception.getMessage());
    }

    @Test
    public void constructor_NegativeColumn_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Cell(0, -1));
        assertEquals("Y = -1\nY координата должна быть больше или равна нулю", exception.getMessage());
    }

    @Test
    public void isFrozen_InitiallyFalse_ReturnsFalse() {
        assertFalse(cell.isFrozen());
    }

    @Test
    public void isOccupied_InitiallyFalse_ReturnsFalse() {
        Cell emptyCell = new Cell(1, 1); // Новая ячейка без кота
        assertFalse(emptyCell.isOccupied());
    }

    @Test
    public void isDestroyed_InitiallyFalse_ReturnsFalse() {
        assertFalse(cell.isDestroyed());
    }

    @Test
    public void isBlocked_InitiallyFalse_ReturnsFalse() {
        assertFalse(cell.isBlocked());
    }

    @Test
    public void getCat_WithCat_ReturnsCat() {
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
    public void setNeighbor_AddsNeighborCorrectly() {
        Cell neighbor = new Cell(0, 1);
        cell.setNeighbor(Side.RIGHT, neighbor);
        assertEquals(neighbor, cell.getNeighbor(Side.RIGHT));
    }

    @Test
    public void getNeighbor_NoNeighbor_ReturnsNull() {
        assertNull(cell.getNeighbor(Side.LEFT));
    }

    @Test
    public void getNeighbors_ReturnsCopyOfNeighbors() {
        Cell neighbor = new Cell(0, 1);
        cell.setNeighbor(Side.RIGHT, neighbor);

        Map<Side, Cell> neighbors = cell.getNeighbors();
        assertEquals(neighbor, neighbors.get(Side.RIGHT));

        // Проверяем, что это копия
        neighbors.remove(Side.RIGHT);
        assertNotNull(cell.getNeighbor(Side.RIGHT));
    }

    @Test
    public void setCat_SetsCatCorrectly() {
        Cell newCell = new Cell(1, 0);
        Cat newCat = new Cat(newCell); // Кот создается с новой ячейкой

        // Перемещаем кота в тестируемую ячейку
        newCell.setCat(newCat);
        assertEquals(newCat, newCell.getCat());
        assertTrue(newCell.isOccupied());
    }

    @Test
    public void setCat_NullCat_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> cell.setCat(null));
    }

    @Test
    public void setCat_DestroyedCell_ThrowsRuntimeException() {
        cell.destroy();
        Cell newCell = new Cell(1, 1);
        Cat newCat = new Cat(newCell);
        assertThrows(RuntimeException.class, () -> cell.setCat(newCat));
    }

    @Test
    public void setCat_BlockedCell_ThrowsRuntimeException() {
        cell.block(true);
        Cell newCell = new Cell(1, 1);
        Cat newCat = new Cat(newCell);
        assertThrows(RuntimeException.class, () -> cell.setCat(newCat));
    }

    @Test
    public void setCat_FrozenCell_ThrowsRuntimeException() {
        cell.freeze(true);
        Cell newCell = new Cell(1, 1);
        Cat newCat = new Cat(newCell);
        assertThrows(RuntimeException.class, () -> cell.setCat(newCat));
    }

    @Test
    public void setCat_SameCat_DoesNothing() {
        cell.setCat(cat); // Кот уже находится в этой ячейке
        assertEquals(cat, cell.getCat());
    }

    @Test
    public void unsetCat_RemovesCatCorrectly() {
        cell.unsetCat();
        assertNull(cell.getCat());
        assertFalse(cell.isOccupied());
        assertNull(cat.getCell()); // Проверяем, что у кота тоже обнулилась ячейка
    }

    @Test
    public void unsetCat_NoCat_DoesNothing() {
        Cell emptyCell = new Cell(1, 1);
        emptyCell.unsetCat(); // Не должно вызывать исключений
        assertNull(emptyCell.getCat());
    }

    @Test
    public void block_SetsBlockedFlagCorrectly() {
        cell.block(true);
        assertTrue(cell.isBlocked());

        cell.block(false);
        assertFalse(cell.isBlocked());
    }

    @Test
    public void block_DestroyedCell_ThrowsRuntimeException() {
        cell.destroy();
        assertThrows(RuntimeException.class, () -> cell.block(true));
    }

    @Test
    public void freeze_SetsFrozenFlagCorrectly() {
        cell.freeze(true);
        assertTrue(cell.isFrozen());

        cell.freeze(false);
        assertFalse(cell.isFrozen());
    }

    @Test
    public void freeze_DestroyedCell_ThrowsRuntimeException() {
        cell.destroy();
        assertThrows(RuntimeException.class, () -> cell.freeze(true));
    }

    @Test
    public void destroy_SetsDestroyedFlagAndClearsNeighbors() {
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
    public void equals_SameObject_ReturnsTrue() {
        assertEquals(cell, cell);
    }

    @Test
    public void equals_NullObject_ReturnsFalse() {
        assertNotEquals(cell, null);
    }

    @Test
    public void equals_DifferentClass_ReturnsFalse() {
        assertNotEquals(cell, new Object());
    }

    @Test
    public void equals_DifferentCoordinates_ReturnsFalse() {
        Cell other = new Cell(1, 1);
        assertNotEquals(cell, other);
    }

    @Test
    public void equals_DifferentState_ReturnsFalse() {
        Cell other = new Cell(0, 0);
        other.block(true);
        assertNotEquals(cell, other);
    }

    @Test
    public void equals_DifferentCat_ReturnsFalse() {
        Cell cell1 = new Cell(0, 0);
        Cat cat1 = new Cat(cell1);

        Cell cell2 = new Cell(0, 0);
        Cat cat2 = new Cat(cell2);

        assertNotEquals(cell1, cell2);
    }

    @Test
    public void catConstructor_NullCell_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Cat(null));
    }

    @Test
    public void catConstructor_SetsCellAndCatCorrectly() {
        Cell newCell = new Cell(1, 1);
        Cat newCat = new Cat(newCell);

        assertEquals(newCell, newCat.getCell());
        assertEquals(newCat, newCell.getCat());
        assertTrue(newCell.isOccupied());
    }
}