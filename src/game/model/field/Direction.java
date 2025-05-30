package game.model.field;

/**
 * Направления.
 */
public enum Direction {
    /**
     * Запад.
     */
    WEST,

    /**
     * Восток.
     */
    EAST,

    /**
     * Северо-запад.
     */
    NORTH_WEST,

    /**
     * Северо-восток.
     */
    NORTH_EAST,

    /**
     * Юго-запад.
     */
    SOUTH_WEST,

    /**
     * Юго-восток.
     */
    SOUTH_EAST;

    /**
     * Противоположное направление.
     */
    private Direction opposite;

    static {
        WEST.opposite = EAST;
        EAST.opposite = WEST;
        NORTH_WEST.opposite = SOUTH_EAST;
        NORTH_EAST.opposite = SOUTH_WEST;
        SOUTH_WEST.opposite = NORTH_EAST;
        SOUTH_EAST.opposite = NORTH_WEST;
    }

    /**
     * Получить противоположное направление {@link Direction#opposite}.
     *
     * @return противоположное направление.
     */
    public Direction getOppositeDirection() {
        return opposite;
    }
}
