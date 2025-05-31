package game.model.field.obstructions;

import game.model.field.AbstractObstruction;
import game.model.field.Cell;

public class PermanentOneCellObstruction extends AbstractObstruction {

    @Override
    public boolean execute(Cell target) {
        return super.block(target, -1);
    }
}
