package game.model.events;

import org.jetbrains.annotations.NotNull;
import game.model.GameStatus;
import game.model.field.Cat;
import game.model.field.obstructions.ObstructionType; // Make sure this import exists

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

    //region ОБСТРУКЦИИ

    /**
     * Текущий тип обструкции.
     */
    private ObstructionType currentObstructionType;

    /**
     * Предыдущий тип обструкции.
     */
    private ObstructionType oldObstructionType;

    /**
     * Получить текущий тип обструкции.
     *
     * @return текущий тип обструкции.
     */
    public ObstructionType getCurrentObstructionType() {
        return currentObstructionType;
    }

    /**
     * Установить текущий тип обструкции.
     *
     * @param type текущий тип обструкции.
     */
    public void setCurrentObstructionType(ObstructionType type) {
        this.currentObstructionType = type;
    }

    /**
     * Получить предыдущий тип обструкции.
     *
     * @return предыдущий тип обструкции.
     */
    public ObstructionType getOldObstructionType() {
        return oldObstructionType;
    }

    /**
     * Установить предыдущий тип обструкции.
     *
     * @param type предыдущий тип обструкции.
     */
    public void setOldObstructionType(ObstructionType type) {
        this.oldObstructionType = type;
    }

    //endregion
}