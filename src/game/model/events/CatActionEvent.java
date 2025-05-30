package game.model.events;

import game.model.field.Cell;
import org.jetbrains.annotations.NotNull;
import game.model.field.Cat;

import java.util.EventObject;

/**
 * Объект события класса кот {@link Cat}.
 */
public class CatActionEvent extends EventObject {

    //region КОНСТРУКТОР

    /**
     * Создаёт событие.
     *
     * @param source объект, на котором изначально произошло событие
     * @throws IllegalArgumentException если source равен null
     */
    public CatActionEvent(Object source) {
        super(source);
    }

    //endregion

    //region КОТ

    /**
     * Кот.
     */
    private Cat cat;

    /**
     * Установить кота {@link CatActionEvent#cat}.
     *
     * @param cat кот.
     */
    public void setCat(@NotNull Cat cat) {
        this.cat = cat;
    }

    /**
     * Получить кота {@link CatActionEvent#cat}.
     *
     * @return кот.
     */
    public Cat getCat() {
        return cat;
    }

    //endregion

    //region СТАРАЯ ПОЗИЦИЯ

    /**
     * Ячейка, откуда переместился кот {@link CatActionEvent#cat}.
     */
    private Cell fromCell;

    /**
     * Установить ячейку {@link CatActionEvent#fromCell}, откуда переместился кот {@link CatActionEvent#cat}.
     *
     * @param fromCell ячейка, откуда переместился кот.
     */
    public void setFromCell(Cell fromCell) {
        this.fromCell = fromCell;
    }

    /**
     * Получить ячейку {@link CatActionEvent#fromCell}, откуда переместился кот {@link CatActionEvent#cat}.
     *
     * @return ячейка, откуда переместился кот.
     */
    public Cell getFromCell() {
        return fromCell;
    }

    //endregion

    //region НОВАЯ ПОЗИЦИЯ

    /**
     * Ячейка, куда переместился кот {@link CatActionEvent#cat}.
     */
    private Cell toCell;

    /**
     * Установить ячейку {@link CatActionEvent#toCell}, куда переместился кот {@link CatActionEvent#cat}.
     *
     * @param toCell ячейка, куда переместился кот.
     */
    public void setToCell(Cell toCell) {
        this.toCell = toCell;
    }

    /**
     * Получить ячейку {@link CatActionEvent#toCell}, куда переместился кот {@link CatActionEvent#cat}.
     *
     * @return ячейка, куда переместился кот.
     */
    public Cell getToCell() {
        return toCell;
    }

    //endregion
}