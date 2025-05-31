package game.model.field.obstructions;

import game.model.field.AbstractObstruction;
import game.model.field.Cell;
import game.model.field.Field;

public class PermanentOneCellObstruction extends AbstractObstruction {

    @Override
    public boolean execute(Cell target, Field field) {
        return super.block(target, -1);
    }
}
