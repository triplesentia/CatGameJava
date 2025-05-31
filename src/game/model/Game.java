package game.model;

import game.model.field.*;
import game.model.field.obstructions.PermanentOneCellObstruction;
import game.model.field.obstructions.TempHorizontalLineObstruction;
import org.jetbrains.annotations.NotNull;
import game.model.events.*;

import java.util.*;
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

        generateAvailableObstructions();
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

    private void moveCatToUnblockedNeighbor() {
        boolean success = false;
        while (!success) {
            success = getCat().move(Direction.values()[new Random().nextInt(Direction.values().length)]);
        }
    }

    //endregion

    //region ТИПЫ ПРЕПЯТСТВИЙ

    private Map<ObstructionType, Integer> availableObstructions = new HashMap<>();

    private void generateAvailableObstructions() {
        availableObstructions.clear();
        availableObstructions.put(ObstructionType.PermanentOneCell, -1);

        for (ObstructionType type : ObstructionType.values()) {
            if (type != ObstructionType.PermanentOneCell) {
                availableObstructions.put(type, 1 + (int) (Math.random() * gameField.getSideLength()));
            }
        }
    }

    private void decreaseObstruction(ObstructionType type) {
        if (!availableObstructions.containsKey(type)) return;
        int current = availableObstructions.get(type);
        if (current > 0) {
            availableObstructions.put(type, current - 1);
        }
        if (current == 1) {
            selectObstructionType(ObstructionType.PermanentOneCell);
        }
    }

    public int getObstructionCount(ObstructionType type) {
        return availableObstructions.get(type);
    }

    //endregion

    //region ВЫБОР РЕЖИМА ПРЕПЯТСТВИЯ

    private ObstructionType currentObstructionType = ObstructionType.PermanentOneCell;

    public boolean selectObstructionType(ObstructionType type) {
        if (availableObstructions.containsKey(type) && (getObstructionCount(type) > 0 || getObstructionCount(type) == -1)) {
            ObstructionType old = this.currentObstructionType;
            this.currentObstructionType = type;
            fireSelectedObstructionChanged(old, type);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean selectRandomObstructionType() {
        List<ObstructionType> types = new ArrayList<>(availableObstructions.keySet());
        types.remove(ObstructionType.PermanentOneCell);

        if (types.isEmpty()) return false;

        ObstructionType old = this.currentObstructionType;
        this.currentObstructionType = types.get((int)(Math.random() * types.size()));
        fireSelectedObstructionChanged(old, this.currentObstructionType);
        return true;
    }

    public ObstructionType getCurrentObstructionType() {
        return currentObstructionType;
    }

    //endregion

    //region БЛОКИРОВКА ЯЧЕЙКИ

    private void executeObstruction(Cell cell) {
        boolean success = obstruct(cell, getCurrentObstructionType());

        if (!success) return;

        decreaseObstruction(getCurrentObstructionType());

        updateGameStatus();

        if (getStatus() != GameStatus.GAME_IS_ON) return;

        moveCatToUnblockedNeighbor();

        gameField.update();
    }

    private boolean obstruct(Cell cell, ObstructionType type) {
        boolean result = false;
        if (type == ObstructionType.PermanentOneCell) {
            result = new PermanentOneCellObstruction().execute(cell);
        }
        else if (type == ObstructionType.TempHorizontalLine) {
            result = new TempHorizontalLineObstruction().execute(cell);
        }
        // else if (...) TODO можно добавить обработку других типов

        return result;
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
            if (getStatus() != GameStatus.GAME_IS_ON) return;
            executeObstruction(event.getCell());
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

    /**
     * Оповестить слушателей {@link Game#gameActionListeners}, что выбранный тип обструкции изменился.
     *
     * @param oldObstructionType предыдущий тип обструкции.
     * @param newObstructionType новый тип обструкции.
     */
    private void fireSelectedObstructionChanged(@NotNull ObstructionType oldObstructionType, @NotNull ObstructionType newObstructionType) {
        GameActionEvent event = new GameActionEvent(this);
        event.setOldObstructionType(oldObstructionType);
        event.setCurrentObstructionType(newObstructionType);

        for (GameActionListener listener : gameActionListeners) {
            listener.selectedObstructionChanged(event);
        }
    }

    //endregion
}
