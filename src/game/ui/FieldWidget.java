package game.ui;

import game.model.field.*;
import game.model.field.Point;
import game.model.events.*;
import game.ui.cell.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class FieldWidget extends JPanel {
    private final Field field;
    private final WidgetFactory widgetFactory;

    public FieldWidget(@NotNull Field field, @NotNull WidgetFactory widgetFactory) {
        this.field = field;
        this.widgetFactory = widgetFactory;
        setLayout(null); // Absolute positioning!
        fillField();
        subscribeOnCat();
        field.addFieldActionListener(new FieldController());
    }

    private static final int CELL_SIZE = 60;
    private static final int HEX_HEIGHT = (int) (Math.sqrt(3) / 2 * CELL_SIZE);

    private void fillField() {
        int n = field.getSideLength();
        double hexWidth = CELL_SIZE;
        double hexHeight = Math.sqrt(3) / 2 * CELL_SIZE;

        for (int x = -n + 1; x < n; x++) {
            for (int y = -n + 1; y < n; y++) {
                if (Math.abs(x + y) > n - 1) continue;
                Cell cell = field.getCell(new Point(x, y));
                if (cell == null) continue;

                CellWidget cellWidget = widgetFactory.create(cell);
                double pixelX = hexWidth * (x + y / 2.0) + 2 * hexWidth;
                double pixelY = hexHeight * y + 2 * hexHeight;

                cellWidget.setBounds((int) pixelX, (int) pixelY, CELL_SIZE, (int) hexHeight + 1);
                add(cellWidget);

                // If this cell has the cat, add the cat widget
                Cat cat = cell.getObject();
                if (cat != null) {
                    CatWidget catWidget = widgetFactory.create(cat);
                    cellWidget.addItem(catWidget);
                }
            }
        }
        setPreferredSize(new Dimension(
                (int) (hexWidth * (2 * n)), // enough for max horizontal
                (int) (hexHeight * (2 * n))
        ));
    }

    /**
     * Subscribe to cat events so the UI updates when the cat moves.
     */
    private void subscribeOnCat() {
        Cat cat = field.getCat();
        if (cat != null) {
            cat.addCatActionListener(new CatController());
        }
    }

    /**
     * Handles cat movement and other cat events.
     */
    private class CatController implements CatActionListener {
        @Override
        public void catIsMoved(@NotNull CatActionEvent event) {
            Cat cat = event.getCat();
            Cell fromCell = event.getFromCell();
            Cell toCell = event.getToCell();

            CatWidget catWidget = widgetFactory.getWidget(cat);
            if (catWidget == null) return;

            CellWidget fromCellWidget = widgetFactory.getWidget(fromCell);
            CellWidget toCellWidget = widgetFactory.getWidget(toCell);

            if (fromCellWidget != null) {
                fromCellWidget.removeItem(catWidget);
            }
            if (toCellWidget != null) {
                toCellWidget.addItem(catWidget);
            }
            catWidget.requestFocus();
        }
    }

    /**
     * Handles field events, such as cell obstruction.
     */
    private class FieldController implements FieldActionListener {
        @Override
        public void fieldCellStateChanged(@NotNull FieldActionEvent event) {
            Cell cell = event.getCell();
            CellWidget cellWidget = widgetFactory.getWidget(cell);
            if (cellWidget != null) {
                cellWidget.repaint();
            }
        }

        @Override
        public void fieldObstructionExecuted(@NotNull FieldActionEvent event) {
            Cell cell = event.getCell();
            CellWidget cellWidget = widgetFactory.getWidget(cell);
            if (cellWidget != null) {
                cellWidget.repaint();
            }
        }
    }
}