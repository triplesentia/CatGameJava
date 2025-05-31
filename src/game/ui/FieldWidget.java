package game.ui;

import game.model.field.*;
import game.model.field.Point;
import game.model.events.*;
import game.ui.cell.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

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

        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;

        // First pass: compute all positions and bounds
        Map<Cell, Point> positions = new HashMap<>();
        for (int x = -n + 1; x < n; x++) {
            for (int y = -n + 1; y < n; y++) {
                if (Math.abs(x + y) > n - 1) continue;
                Cell cell = field.getCell(new Point(x, y));
                if (cell == null) continue;

                double pixelX = hexWidth * (x + y / 2.0);
                double pixelY = hexHeight * y;

                // Track bounds
                if (pixelX < minX) minX = pixelX;
                if (pixelY < minY) minY = pixelY;
                if (pixelX > maxX) maxX = pixelX;
                if (pixelY > maxY) maxY = pixelY;

                positions.put(cell, new Point((int) pixelX, (int) pixelY));
            }
        }

        // Second pass: layout cells with minX/minY offset to (0,0)
        for (Map.Entry<Cell, Point> entry : positions.entrySet()) {
            Cell cell = entry.getKey();
            Point pt = entry.getValue();

            CellWidget cellWidget = widgetFactory.create(cell);
            int px = (int)(pt.getX() - minX);
            int py = (int)(pt.getY() - minY - (double) (HEX_HEIGHT - CELL_SIZE) /2);
            cellWidget.setBounds(px, py, CELL_SIZE, (int)hexHeight);
            add(cellWidget);

            // Add cat if present
            Cat cat = cell.getObject();
            if (cat != null) {
                CatWidget catWidget = widgetFactory.create(cat);
                cellWidget.addItem(catWidget);
            }
        }

        setPreferredSize(new Dimension(
                (int)(maxX - minX + CELL_SIZE),
                (int)(maxY - minY + hexHeight + 30)
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