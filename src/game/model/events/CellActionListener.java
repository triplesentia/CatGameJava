package game.model.events;

import org.jetbrains.annotations.NotNull;
import game.model.field.Cell;

import java.util.EventListener;

/**
 * Интерфейс слушателя события класса ячейка {@link Cell}.
 */
public interface CellActionListener extends EventListener {

    /**
     * Ячейка изменила состояние.
     *
     * @param event объект события класса ячейка.
     */
    void cellStateChanged(@NotNull CellActionEvent event);

    /**
     * Была применена блокировка.
     *
     * @param event объект события класса ячейка.
     */
    void obstructionRequested(@NotNull CellActionEvent event);
}