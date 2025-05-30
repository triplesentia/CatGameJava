package game.model.field;

import org.jetbrains.annotations.NotNull;
import game.model.events.CatActionEvent;
import game.model.events.CatActionListener;

import java.util.ArrayList;

/**
 * Кот.
 */
public class Cat {

    //region ПОЗИЦИЯ

    /**
     * Позиция кота.
     */
    private Cell position;

    /**
     * Получить позицию кота.
     *
     * @return позиция кота.
     */
    public Cell getPosition() {
        return position;
    }

    /**
     * Установить позицию кота.
     *
     * @param position позиция.
     * @return установлена ли позиция.
     */
    public boolean setPosition(@NotNull Cell position) {
        if (!canSetPosition(position)) {
            return false;
        }
        this.position = position;
        return true;
    }

    /**
     * Может ли кот располагаться в указанной позиции.
     *
     * @param cell позиция.
     * @return может ли кот располагаться в указанной позиции.
     */
    protected boolean canSetPosition(@NotNull Cell cell) {
        return getPosition() == null;
    }

    /**
     * Удалить позицию у кота.
     */
    public void unsetPosition() {
        this.position = null;
    }

    //endregion

    //region ПЕРЕМЕЩЕНИЕ

    /**
     * Переместить кота в заданном направлении.
     *
     * @param direction направление.
     */
    public boolean move(@NotNull Direction direction) {
        Cell newPosition = getPosition().getNeighborCell(direction);

        if (newPosition == null || !newPosition.canSetObject()) {
            return false;
        }

        Cell oldPosition = getPosition();

        oldPosition.takeObject();

        boolean success = newPosition.setObject(this);

        if (!success) {
            throw new RuntimeException("Cat can't move to the " + newPosition);
        }

        fireCatIsMoved(oldPosition, newPosition);

        return true;
    }

    //endregion

    //region СИГНАЛЫ

    /**
     * Список слушателей, подписанных на события кота.
     */
    private final ArrayList<CatActionListener> catListListener = new ArrayList<>();

    /**
     * Добавить нового слушателя за событиями кота.
     *
     * @param listener слушатель.
     */
    public void addCatActionListener(CatActionListener listener) {
        catListListener.add(listener);
    }

    /**
     * Удалить слушателя за событиями кота.
     *
     * @param listener слушатель.
     */
    public void removeCatActionListener(CatActionListener listener) {
        catListListener.remove(listener);
    }

    /**
     * Оповестить слушателей, что кот переместился.
     *
     * @param oldPosition ячейка откуда переместился кот.
     * @param newPosition ячейка куда переместился кот.
     */
    private void fireCatIsMoved(@NotNull Cell oldPosition, @NotNull Cell newPosition) {
        CatActionEvent event = new CatActionEvent(this);
        event.setCat(this);
        event.setFromCell(oldPosition);
        event.setToCell(newPosition);

        for (CatActionListener listener : catListListener) {
            listener.catIsMoved(event);
        }
    }

    //endregion
}