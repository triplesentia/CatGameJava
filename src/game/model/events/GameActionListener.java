package game.model.events;

import org.jetbrains.annotations.NotNull;

/**
 * Интерфейс слушателя события класса игры {@link game.model.Game}.
 */
public interface GameActionListener extends java.util.EventListener {

    /**
     * Кот переместился.
     *
     * @param event объект события класса игры.
     */
    void catIsMoved(@NotNull GameActionEvent event);

    /**
     * Статус игры изменился.
     *
     * @param event объект события класса игры.
     */
    void gameStatusChanged(@NotNull GameActionEvent event);

    /**
     * Тип выбранного препятствия изменился.
     *
     * @param event объект события класса игры.
     */
    void selectedObstructionChanged(@NotNull GameActionEvent event);
}