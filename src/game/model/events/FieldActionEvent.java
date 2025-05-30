package game.model.events;

import game.model.field.Field;
import game.model.field.Cell;
import java.util.EventObject;

/**
 * Объект события класса поле {@link Field}.
 */
public class FieldActionEvent extends EventObject {
    public FieldActionEvent(Object source) {
        super(source);
    }
    private Cell cell;
    public void setCell(Cell cell) { this.cell = cell; }
    public Cell getCell() { return cell; }
}