package game.model.field;

/**
 * Направления (ось x, y — axial).
 */
public enum Direction {
    WEST(-1, 0),
    EAST(1, 0),
    NORTH_WEST(0, -1),
    NORTH_EAST(1, -1),
    SOUTH_WEST(-1, 1),
    SOUTH_EAST(0, 1);

    public final int dx, dy;

    private Direction opposite;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    static {
        WEST.opposite = EAST;
        EAST.opposite = WEST;
        NORTH_WEST.opposite = SOUTH_EAST;
        NORTH_EAST.opposite = SOUTH_WEST;
        SOUTH_WEST.opposite = NORTH_EAST;
        SOUTH_EAST.opposite = NORTH_WEST;
    }

    public Direction getOppositeDirection() {
        return opposite;
    }
}