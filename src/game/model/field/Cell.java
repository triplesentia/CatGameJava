package game.model.field;

import game.model.events.*;
import game.model.field.obstructions.PermanentOneCellObstruction;
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

    //region БЛОКИРОВКА ЯЧЕЙКИ

    public boolean obstruct(ObstructionType type) {
        boolean result = false;
        if (type == ObstructionType.PermanentOneCell) {
            result = new PermanentOneCellObstruction().execute(this);
        }
        // TODO можно добавить обработку других типов

        if (result) {
            fireObstructionExecuted(type);
        }

        return result;
    }

    //endregion

    //region ПРОХОДИМОСТЬ

    /**
     * Заблокирована ли ячейка для прохода/помещения объекта.
     */
    private boolean isBlocked = false;

    /**
     * Установить состояние проходимости (package-private).
     *
     * @param blocked true — ячейка заблокирована, false — проходима
     * @return true если состояние успешно изменено, иначе false
     */
    boolean setBlocked(boolean blocked) {
        if (getObject() != null) {
            return false;
        }

        this.isBlocked = blocked;
        fireCellStateChanged();
        return true;
    }

    /**
     * Получить состояние проходимости.
     *
     * @return true — ячейка заблокирована, false — проходима
     */
    public boolean isBlocked() {
        return isBlocked;
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

    protected void fireObstructionExecuted(ObstructionType type) {
        CellActionEvent event = new CellActionEvent(this);
        event.setCell(this);
        event.setObstructionType(type);
        for (CellActionListener listener : cellListeners) {
            listener.obstructionExecuted(event);
        }
    }

    //endregion
}