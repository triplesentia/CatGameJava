package game.ui;

import game.model.field.Cat;
import game.model.field.Cell;
import game.ui.cell.CatWidget;
import game.ui.cell.CellWidget;
import game.ui.cell.CellItemWidget;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Фабрика виджетов для поля и объектов.
 */
public class WidgetFactory {

    private final Map<Cell, CellWidget> cells = new HashMap<>();
    private final Map<Cat, CatWidget> catWidgets = new HashMap<>();

    /*---------- Cell ----------*/
    public CellWidget create(Cell cell) {
        CellWidget cellWidget = cells.computeIfAbsent(cell, c -> new CellWidget(c));
        Cat cat = cell.getObject();
        if (cat != null) {
            CatWidget catWidget = create(cat);
            cellWidget.addItem(catWidget);
        }
        return cellWidget;
    }

    public CellWidget getWidget(Cell cell) {
        return cells.get(cell);
    }

    public void remove(Cell cell) {
        cells.remove(cell);
    }

    /*---------- Cat ----------*/
    public CatWidget create(Cat cat) {
        return catWidgets.computeIfAbsent(cat, CatWidget::new);
    }

    public CatWidget getWidget(Cat cat) {
        return catWidgets.get(cat);
    }

    public void remove(Cat cat) {
        catWidgets.remove(cat);
    }
}