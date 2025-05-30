package game.model.field;

public abstract class AbstractObstruction {

    public abstract boolean execute(Cell target);

    protected boolean block(Cell target) {
        return target.setBlocked(true);
    }

    protected boolean unblock(Cell target) {
        return target.setBlocked(false);
    }
}
