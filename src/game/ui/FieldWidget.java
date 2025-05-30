package game.ui;

import game.model.field.*;
import game.model.field.Point;
import game.ui.cell.CellWidget;
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
                // key line: pixel math
                double pixelX = hexWidth * (x + y / 2.0) + 2 * hexWidth; // +2*hexWidth for padding
                double pixelY = hexHeight * y + 2 * hexHeight; // +2*hexHeight for padding

                cellWidget.setBounds((int)pixelX, (int)pixelY, CELL_SIZE, (int)hexHeight + 1);
                add(cellWidget);
            }
        }
        setPreferredSize(new Dimension(
                (int)(hexWidth * (2 * n)), // enough for max horizontal
                (int)(hexHeight * (2 * n))
        ));
    }
}