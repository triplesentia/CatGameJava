package game.model.field.obstructions;

import game.model.field.AbstractObstruction;
import game.model.field.Cell;
import game.model.field.Direction;
import game.model.field.Field;

import java.util.Random;

public class TempHorizontalLineObstruction extends AbstractObstruction {
    private static int STEPS_UNTIL_UNBLOCK = 3;

    @Override
    public boolean execute(Cell target, Field field) {
        boolean success = super.block(target, STEPS_UNTIL_UNBLOCK);

        if (!success) return false;

        Direction option1, option2;

        if (new Random().nextBoolean()) {
            option1 = Direction.WEST;
            option2 = Direction.EAST;
        }
        else {
            option1 = Direction.EAST;
            option2 = Direction.WEST;
        }

        if (target.getNeighborCell(option1) != null) {
            success = super.block(target.getNeighborCell(option1), STEPS_UNTIL_UNBLOCK);

            if (!success) {
                if (target.getNeighborCell(option2) != null) {
                    super.block(target.getNeighborCell(option2), STEPS_UNTIL_UNBLOCK);
                }
            }
        }

        return true;
    }
}
