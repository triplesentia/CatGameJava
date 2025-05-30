package game.model.events;

import org.jetbrains.annotations.NotNull;
import game.model.field.Cat;

import java.util.EventListener;

/**
 * Интерфейс слушателя события класса кот {@link Cat}.
 */
public interface CatActionListener extends EventListener {

    /**
     * Кот переместился.
     *
     * @param event объект события класса кот.
     */
    void catIsMoved(@NotNull CatActionEvent event);
}