package items;

import javax.security.auth.Destroyable;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Cell implements Destroyable {
    boolean IS_FROZEN = false;
    boolean IS_OCCUPIED = false;
    boolean IS_DESTROYED = false;
    boolean IS_BLOCKED = false;

    private int ROW_X, COL_Y;
    Cat CAT;

    private final Map<Side, Cell> neighbors = new EnumMap<>(Side.class);

    public Cell(int _row, int _col) {
        if (_row < 0) throw new IllegalArgumentException("X = " + _row + "\nX координата должна быть больше или равна нулю");
        if (_col < 0) throw new IllegalArgumentException("Y = " + _col + "\nY координата должна быть больше или равна нулю");
        this.ROW_X = _row;
        this.COL_Y = _col;
    }

    public void setNeighbor(Side side, Cell neighbor) {
        neighbors.put(side, neighbor);
    }

    public Cell getNeighbor(Side side) {
        return neighbors.get(side);
    }

    public Map<Side, Cell> getNeighbors() {
        return new EnumMap<>(neighbors);
    }

    public boolean setCat(Cat _cat) {
        if (IS_DESTROYED) throw new RuntimeException("Невозможно установить кота! Клетка уничтожена");
        if (IS_BLOCKED) throw new RuntimeException("Невозможно установить кота! Клетка заблокирована");
        if (IS_FROZEN) throw new RuntimeException("Невозможно установить кота! Клетка заморожена");

        if (!IS_OCCUPIED) {
            CAT = _cat;
            IS_OCCUPIED = true;
            CAT.setCell(this);
            return true;
        }
        return false;
    }

    public void unsetCat() {
        if (this.CAT == null) throw new RuntimeException("Невозможно убрать кота, он отсутствует в клетке!");
        if (IS_DESTROYED) throw new RuntimeException("Невозможно убрать кота! Клетка уничтожена");
        if (IS_BLOCKED) throw new RuntimeException("Невозможно убрать кота! Клетка заблокирована");
        if (IS_FROZEN) throw new RuntimeException("Невозможно убрать кота! Клетка заморожена");

        if (IS_OCCUPIED) {
            CAT.unsetCell();
            CAT = null;
            IS_OCCUPIED = false;
        }
    }

    public boolean block(boolean _blocked) {
        if (IS_DESTROYED) throw new RuntimeException("Невозможно заблокировать/разблокировать клетку! Клетка уничтожена");
        return (IS_BLOCKED = _blocked);
    }

    public boolean freeze(boolean _freeze) {
        if (IS_DESTROYED) throw new RuntimeException("Невозможно заморозить/разморозить клетку! Клетка уничтожена");
        return (IS_FROZEN = _freeze);
    }

    public int X() { return this.ROW_X; }
    public int Y() { return this.COL_Y; }


    @Override
    public void destroy() {
        IS_DESTROYED = true;
        ROW_X = -1;
        COL_Y = -1;

        for (Cell neighbor : neighbors.values()) {
            if (neighbor != null) {
                neighbor.neighbors.entrySet().removeIf(entry -> entry.getValue() == this);
            }
        }
        neighbors.clear();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cell other = (Cell) obj;
        return ROW_X == other.ROW_X &&
                COL_Y == other.COL_Y &&
                IS_FROZEN == other.IS_FROZEN &&
                IS_OCCUPIED == other.IS_OCCUPIED &&
                IS_DESTROYED == other.IS_DESTROYED &&
                IS_BLOCKED == other.IS_BLOCKED &&
                CAT == other.CAT;
    }
}