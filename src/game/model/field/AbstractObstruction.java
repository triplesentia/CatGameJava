package game.model.field;

public abstract class AbstractObstruction {

    public abstract boolean execute(Cell target);

    protected boolean block(Cell target, int stepsUntilUnblock) {
        return target.setBlocked(stepsUntilUnblock);
    }

    protected boolean unblock(Cell target) {
        return target.setBlocked(0);
    }
}
