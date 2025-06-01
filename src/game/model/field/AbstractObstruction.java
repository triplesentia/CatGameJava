package game.model.field;

public abstract class AbstractObstruction {

    public abstract boolean execute(Cell target, Field field);

    protected boolean block(Cell target, int stepsUntilUnblock) {
        if (target.isBlocked() && (target.getStepsUntilUnblock() == -1 || target.getStepsUntilUnblock() >= stepsUntilUnblock && stepsUntilUnblock != -1)) return false;
        return target.setBlocked(stepsUntilUnblock);
    }

    protected boolean unblock(Cell target) {
        return target.setBlocked(0);
    }
}
