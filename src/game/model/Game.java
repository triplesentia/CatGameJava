package game.model;

import game.model.field.Direction;
import org.jetbrains.annotations.NotNull;
import game.model.events.*;
import game.model.field.Field;
import game.model.field.Cat;

import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;

/**
 * Игра.
 */
public class Game {

    //region КОНСТРУКТОРЫ

    public Game(int fieldSideLength) {
        start(fieldSideLength);
    }

    //endregion

    //region СТАТУС ИГРЫ

    /**
     * Старт новой игры
     *
     * @param fieldSideLength длина стороны поля
     */
    public void start(int fieldSideLength) {
        setStatus(GameStatus.GAME_IS_ON);

        gameField = new Field(fieldSideLength);
        gameField.addFieldActionListener(new FieldObserver());

        if (getCat() == null) {
            throw new RuntimeException("No cat found");
        }

        getCat().addCatActionListener(new CatObserver());
    }

    /**
     * Статус игры.
     */
    private GameStatus gameStatus;

    /**
     * Задержка обновления статуса игры.
     */
    private final int GAME_STATUS_UPDATE_DELAY = 5;

    /**
     * Получить текущий статус игры {@link Game#gameStatus}
     *
     * @return текущий статус игры
     */
    public GameStatus getStatus() {
        return gameStatus;
    }

    /**
     * Установить текущий статус игры
     *
     * @param status новый статус игры
     */
    private void setStatus(GameStatus status) {
        if (gameStatus != status) {
            gameStatus = status;
            fireGameStatusIsChanged(gameStatus);
        }
    }

    /**
     * Обновить состояние игры с задержкой {@link Game#GAME_STATUS_UPDATE_DELAY}.
     */
    private void updateGameStatusWithDelay() {
        Timer timer = new Timer(GAME_STATUS_UPDATE_DELAY, e -> updateGameStatus());
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Обновить состояние игры.
     */
    private void updateGameStatus() {
        GameStatus status = GameStatus.GAME_IS_ON;

        if (gameField.isPerimeterCell(getCat().getPosition())) {
            status = GameStatus.LOSS;
        }
        else if (!gameField.canCatEscapeToPerimeter()) {
            status = GameStatus.WIN;
        }

        setStatus(status);
    }

    //endregion

    //region ПОЛЕ

    /**
     * Игровое поле.
     */
    private Field gameField;

    /**
     * Получить игровое поле {@link Game#gameField}.
     *
     * @return игровое поле.
     */
    public Field getGameField() {
        return gameField;
    }

    //endregion

    //region КОТ

    /**
     * Получить кота {@link Field#getCat()}.
     *
     * @return кот.
     */
    public Cat getCat() {
        return gameField.getCat();
    }

    //endregion

    //region СЛУШАТЕЛИ

    /**
     * Класс, реализующий наблюдение за событиями {@link CatActionListener}.
     */
    private class CatObserver implements CatActionListener {

        @Override
        public void catIsMoved(@NotNull CatActionEvent event) {
            fireCatIsMoved(event.getCat());
            updateGameStatusWithDelay();
        }
    }

    /**
     * Класс, реализующий наблюдение за событиями {@link FieldActionListener}.
     */
    private class FieldObserver implements FieldActionListener {

        @Override
        public void fieldCellStateChanged(@NotNull FieldActionEvent event) {
            updateGameStatusWithDelay();
        }

        @Override
        public void fieldObstructionExecuted(@NotNull FieldActionEvent event) {
            updateGameStatus();
            getCat().move(Direction.values()[new Random().nextInt(Direction.values().length)]);
        }
    }

    //endregion

    //region СИГНАЛЫ

    /**
     * Список слушателей, подписанных на события игры.
     */
    private final ArrayList<GameActionListener> gameActionListeners = new ArrayList<>();

    /**
     * Добавить нового слушателя за событиями игры.
     *
     * @param listener слушатель.
     */
    public void addGameActionListener(@NotNull GameActionListener listener) {
        gameActionListeners.add(listener);
    }

    /**
     * Удалить слушателя за событиями игры.
     *
     * @param listener слушатель.
     */
    public void removeGameActionListener(@NotNull GameActionListener listener) {
        gameActionListeners.remove(listener);
    }

    /**
     * Оповестить слушателей {@link Game#gameActionListeners}, что кот переместился.
     *
     * @param cat кот, который переместился.
     */
    private void fireCatIsMoved(@NotNull Cat cat) {
        GameActionEvent event = new GameActionEvent(this);
        event.setCat(cat);

        for (GameActionListener listener : gameActionListeners) {
            listener.catIsMoved(event);
        }
    }

    /**
     * Оповестить слушателей {@link Game#gameActionListeners}, что статус игры изменился.
     *
     * @param status статус игры.
     */
    private void fireGameStatusIsChanged(@NotNull GameStatus status) {
        GameActionEvent event = new GameActionEvent(this);
        event.setStatus(status);

        for (GameActionListener listener : gameActionListeners) {
            listener.gameStatusChanged(event);
        }
    }

    //endregion
}
