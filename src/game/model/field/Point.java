package game.model.field;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;

/**
 * Точка-координата на игровом поле {@link Field}
 */
public class Point {

    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    /**
     * Получить новую точку с заданным смещением в заданном направлении.
     *
     * @param direction направление.
     * @param delta     смещение.
     * @return новая точка с заданным смещением в заданном направлении.
     */
    public Point to(@NotNull Direction direction, int delta) {
        return new Point(x + direction.dx * delta, y + direction.dy * delta);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point p = (Point) o;
        return x == p.x && y == p.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Point{" + "x=" + x + ", y=" + y + '}';
    }
}
