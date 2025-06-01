package game.model.field;

import game.model.events.*;
import org.jetbrains.annotations.NotNull;
import java.util.*;

/**
 * Ячейка.
 */
public class Cell {

    //region ОБЪЕКТ В ЯЧЕЙКЕ

    private Cat object = null;

    public Cat getObject() {
        return object;
    }

    public boolean setObject(@NotNull Cat object) {
        if (!this.canSetObject() || isBlocked()) {
            return false;
        }

        boolean success = object.setPosition(this);
        if (!success) {
            return false;
        }

        this.object = object;
        return true;
    }

    public boolean canSetObject() {
        return getObject() == null && !isBlocked();
    }

    public Cat takeObject() {
        Cat result = object;
        if (result != null) {
            result.unsetPosition();
            object = null;
        }
        return result;
    }

    //endregion

    //region ОБНОВЛЕНИЕ

    void update() {
        if (getStepsUntilUnblock() > 0) {
            stepsUntilUnblock--;
            fireCellStateChanged();
        }
    }

    //endregion

    //region БЛОКИРОВКА ЯЧЕЙКИ

    public void requestObstruction() {
        fireObstructionRequested();
    }

    //endregion

    //region ПРОХОДИМОСТЬ

    /**
     * Заблокирована ли ячейка для прохода/помещения объекта.
     */
    private int stepsUntilUnblock = 0;

    /**
     * Установить состояние проходимости (package-private).
     *
     * @param stepsUntilUnblock число ходов до разблокировки, -1 — ячейка заблокирована безвременно, 0 - не заблокирована
     * @return true - если состояние успешно изменено, иначе - false
     */
    boolean setBlocked(int stepsUntilUnblock) {
        if (stepsUntilUnblock < -1 || getObject() != null) {
            return false;
        }

        this.stepsUntilUnblock = stepsUntilUnblock;
        fireCellStateChanged();
        return true;
    }

    /**
     * Получить состояние проходимости.
     *
     * @return true — ячейка заблокирована, false — проходима
     */
    public boolean isBlocked() {
        return stepsUntilUnblock != 0;
    }

    /**
     * Получить количество ходов до разблокировки.
     *
     * @return количество ходов до разблокировки.
     */
    public int getStepsUntilUnblock() {
        return stepsUntilUnblock;
    }

    //endregion

    //region СОСЕДНИЕ ЯЧЕЙКИ

    private final Map<Direction, Cell> neighbors = new EnumMap<>(Direction.class);

    public Cell getNeighborCell(@NotNull Direction direction) {
        return neighbors.get(direction);
    }

    boolean setNeighbors(Map<Direction, Cell> neighborCells) {
        if (neighborCells != null) {
            for (Map.Entry<Direction, Cell> entry : neighborCells.entrySet()) {
                if (!setNeighbor(entry.getValue(), entry.getKey())) return false;
            }
        }
        return true;
    }

    private boolean setNeighbor(@NotNull Cell neighborCell, @NotNull Direction direction) {
        if (neighbors.containsKey(direction)) {
            return neighbors.get(direction) == neighborCell;
        }

        if (neighbors.containsValue(neighborCell)) {
            return false;
        }
        neighbors.put(direction, neighborCell);
        return true;
    }

    //endregion

    //region СИГНАЛЫ

    private final List<CellActionListener> cellListeners = new ArrayList<>();

    public void addCellActionListener(CellActionListener listener) {
        cellListeners.add(listener);
    }

    public void removeCellActionListener(CellActionListener listener) {
        cellListeners.remove(listener);
    }

    protected void fireCellStateChanged() {
        CellActionEvent event = new CellActionEvent(this);
        event.setCell(this);
        for (CellActionListener listener : cellListeners) {
            listener.cellStateChanged(event);
        }
    }

    protected void fireObstructionRequested() {
        CellActionEvent event = new CellActionEvent(this);
        event.setCell(this);
        for (CellActionListener listener : cellListeners) {
            listener.obstructionRequested(event);
        }
    }

    //endregion
}