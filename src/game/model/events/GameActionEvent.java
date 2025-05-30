package game.model.events;

import org.jetbrains.annotations.NotNull;
import game.model.GameStatus;
import game.model.field.Cat;

import java.util.EventObject;

/**
 * Объект события класса игры {@link game.model.Game}.
 */
public class GameActionEvent extends EventObject {

    //region КОНСТРУКТОР

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public GameActionEvent(Object source) {
        super(source);
    }

    //endregion

    //region КОТ

    /**
     * Кот.
     */
    private Cat cat;

    /**
     * Установить кота {@link GameActionEvent#cat}.
     *
     * @param cat кот.
     */
    public void setCat(@NotNull Cat cat) {
        this.cat = cat;
    }

    /**
     * Получить кота {@link GameActionEvent#cat}.
     *
     * @return кот.
     */
    public Cat getCat() {
        return cat;
    }

    //endregion

    //region СТАТУС ИГРЫ

    /**
     * Статус игры.
     */
    private GameStatus status;

    /**
     * Получить статус игры {@link GameActionEvent#status}.
     *
     * @return статус игры.
     */
    public GameStatus getStatus() {
        return status;
    }

    /**
     * Установить статус игры {@link GameActionEvent#status}.
     *
     * @param status статус игры.
     */
    public void setStatus(GameStatus status) {
        this.status = status;
    }

    //endregion
}