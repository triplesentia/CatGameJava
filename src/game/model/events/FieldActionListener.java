package game.model.events;

import game.model.field.Field;
import org.jetbrains.annotations.NotNull;

/**
 * Интерфейс слушателя события поля {@link Field}.
 */
public interface FieldActionListener {

    /**
     * Ячейка на поле изменила состояние.
     *
     * @param event объект события поля.
     */
    void fieldCellStateChanged(@NotNull FieldActionEvent event);

    /**
     * На поле была применена блокировка.
     *
     * @param event объект события поля.
     */
    void fieldObstructionExecuted(@NotNull FieldActionEvent event);
}