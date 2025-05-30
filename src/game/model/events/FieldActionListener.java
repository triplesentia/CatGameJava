package game.model.events;

import game.model.field.Field;
import org.jetbrains.annotations.NotNull;

/**
 * Интерфейс слушателя события поля {@link Field}.
 */
public interface FieldActionListener {
    void fieldCellStateChanged(@NotNull FieldActionEvent event);
}