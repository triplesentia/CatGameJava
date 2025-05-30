package game.model.events;

import game.model.field.Cell;
import org.jetbrains.annotations.NotNull;

import java.util.EventObject;

/**
 * Объект события класса ячейка {@link Cell}.
 */
public class CellActionEvent extends EventObject {

    //region КОНСТРУКТОР

    /**
     * Создаёт событие.
     *
     * @param source объект, на котором изначально произошло событие
     * @throws IllegalArgumentException если source равен null
     */
    public CellActionEvent(Object source) {
        super(source);
    }

    //endregion

    //region ЯЧЕЙКА

    /**
     * Ячейка, в которой произошло изменение.
     */
    private Cell cell;

    /**
     * Установить ячейку {@link CellActionEvent#cell}.
     *
     * @param cell ячейка.
     */
    public void setCell(@NotNull Cell cell) {
        this.cell = cell;
    }

    /**
     * Получить ячейку {@link CellActionEvent#cell}.
     *
     * @return ячейка.
     */
    public Cell getCell() {
        return cell;
    }

    //endregion

    //region ТИП БЛОКИРОВКИ

    /**
     * Тип блокировки, связанный с событием.
     */
    private Cell.ObstructionType obstructionType;

    /**
     * Установить тип блокировки {@link CellActionEvent#obstructionType}.
     *
     * @param obstructionType тип блокировки.
     */
    public void setObstructionType(Cell.ObstructionType obstructionType) {
        this.obstructionType = obstructionType;
    }

    /**
     * Получить тип блокировки {@link CellActionEvent#obstructionType}.
     *
     * @return тип блокировки.
     */
    public Cell.ObstructionType getObstructionType() {
        return obstructionType;
    }

    //endregion
}