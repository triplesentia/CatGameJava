package game.model.events;

import game.model.field.Field;
import game.model.field.Cell;
import game.model.field.ObstructionType;

import java.util.EventObject;

/**
 * Объект события класса поле {@link Field}.
 */
public class FieldActionEvent extends EventObject {

    //region КОНСТРУКТОР

    /**
     * Создаёт событие.
     *
     * @param source объект, на котором изначально произошло событие
     * @throws IllegalArgumentException если source равен null
     */
    public FieldActionEvent(Object source) {
        super(source);
    }

    //endregion

    //region ЯЧЕЙКА

    /**
     * Ячейка, связанная с событием поля.
     */
    private Cell cell;

    /**
     * Установить ячейку {@link FieldActionEvent#cell}.
     *
     * @param cell ячейка.
     */
    public void setCell(Cell cell) {
        this.cell = cell;
    }

    /**
     * Получить ячейку {@link FieldActionEvent#cell}.
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
    private ObstructionType obstructionType;

    /**
     * Установить тип блокировки {@link FieldActionEvent#obstructionType}.
     *
     * @param obstructionType тип блокировки.
     */
    public void setObstructionType(ObstructionType obstructionType) {
        this.obstructionType = obstructionType;
    }

    /**
     * Получить тип блокировки {@link FieldActionEvent#obstructionType}.
     *
     * @return тип блокировки.
     */
    public ObstructionType getObstructionType() {
        return obstructionType;
    }

    //endregion
}