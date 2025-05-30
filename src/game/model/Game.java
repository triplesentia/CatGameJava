package game.model;

import org.jetbrains.annotations.NotNull;
import game.model.events.*;
import game.model.field.Field;
import game.model.field.Cat;

import java.util.ArrayList;
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

        if (getRobot() == null) {
            throw new RuntimeException("No robot found");
        }

        getRobot().addRobotActionListener(new RobotObserver());
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

        if (!getRobot().isCapable()) {
            if (getRobot().isTeleported()) {
                status = GameStatus.WIN;
            }
            else {
                status = GameStatus.LOSS;
            }
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

    //region РОБОТ

    /**
     * Получить робота {@link Field#getRobot()}.
     *
     * @return робот.
     */
    public Robot getRobot() {
        return gameField.get();
    }

    //endregion

    //region СЛУШАТЕЛИ

    /**
     * Класс, реализующий наблюдение за событиями {@link RobotActionListener}.
     */
    private class RobotObserver implements RobotActionListener {

        @Override
        public void robotIsMoved(@NotNull RobotActionEvent event) {
            fireRobotIsMoved(event.getRobot());
            updateGameStatusWithDelay();
        }

        @Override
        public void robotChangedBattery(@NotNull RobotActionEvent event) {
            updateGameStatusWithDelay();
        }
    }

    /**
     * Класс, реализующий наблюдение за событиями {@link FieldActionListener}.
     */
    private class FieldObserver implements FieldActionListener {

        @Override
        public void robotIsTeleported(@NotNull FieldActionEvent event) {
            fireRobotIsTeleported();
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
     * Оповестить слушателей {@link Game#gameActionListeners}, что робот переместился.
     *
     * @param robot робот, который переместился.
     */
    private void fireRobotIsMoved(@NotNull Robot robot) {
        GameActionEvent event = new GameActionEvent(this);
        event.setRobot(robot);

        for (GameActionListener listener : gameActionListeners) {
            listener.robotIsMoved(event);
        }
    }

    /**
     * Оповестить слушателей {@link Game#gameActionListeners}, что робот телепортировался.
     */
    private void fireRobotIsTeleported() {
        GameActionEvent event = new GameActionEvent(this);
        event.setRobot(getRobot());

        for (GameActionListener listener : gameActionListeners) {
            listener.robotIsTeleported(event);
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
