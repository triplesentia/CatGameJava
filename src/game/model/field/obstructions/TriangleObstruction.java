package game.model.field.obstructions;

import game.model.field.AbstractObstruction;
import game.model.field.Cell;
import game.model.field.Direction;
import game.model.field.Field;
import game.model.field.Cat;

import java.util.HashSet;
import java.util.Set;

public class TriangleObstruction extends AbstractObstruction {
    private static final int STEPS_UNTIL_UNBLOCK = 2;

    @Override
    public boolean execute(Cell target, Field field) {
        int totalCells = field.getCellCount(); // e.g. 61 for side=5
        int maxTriangleCells = Math.max(1, totalCells / 6);

        // Find maximum N such that N*(N+1)/2 <= maxTriangleCells
        int rows = 1;
        while (triangleCellCount(rows + 1) <= maxTriangleCells) {
            rows++;
        }

        Set<Cell> triangleCells = new HashSet<>();
        for (int r = 0; r < rows; r++) {
            // Each row starts r steps NORTH_EAST from target
            Cell rowStart = target;
            for (int i = 0; i < r && rowStart != null; i++) {
                rowStart = rowStart.getNeighborCell(Direction.NORTH_EAST);
            }
            // Each row has (rows - r) cells, going EAST
            Cell curr = rowStart;
            for (int c = 0; c < (rows - r) && curr != null; c++) {
                triangleCells.add(curr);
                curr = curr.getNeighborCell(Direction.EAST);
            }
        }

        // Cat check
        Cat cat = field.getCat();
        Cell catCell = (cat != null) ? cat.getPosition() : null;
        if (catCell != null && triangleCells.contains(catCell)) {
            return false;
        }

        // Block all triangle cells, success if the clicked cell was blocked
        boolean success = false;
        for (Cell c : triangleCells) {
            if (super.block(c, STEPS_UNTIL_UNBLOCK)) {
                if (c == target) success = true;
            }
        }
        return success;
    }

    private int triangleCellCount(int rows) {
        return rows * (rows + 1) / 2;
    }
}