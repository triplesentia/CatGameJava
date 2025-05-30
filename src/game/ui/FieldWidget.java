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
        int diameter = 2 * n - 1;

        for (int q = -n + 1; q < n; q++) {
            for (int r = -n + 1; r < n; r++) {
                if (Math.abs(q + r) > n - 1) continue;
                Cell cell = field.getCell(new Point(q, r));
                if (cell == null) continue;

                CellWidget cellWidget = new CellWidget(cell);
                int col = q + n - 1;
                int row = r + n - 1;
                int x = CELL_SIZE * col + (row * CELL_SIZE) / 2;
                int y = HEX_HEIGHT * row;

                cellWidget.setBounds(x, y, CELL_SIZE, CELL_SIZE);
                add(cellWidget);
            }
        }

        setPreferredSize(new Dimension(CELL_SIZE * diameter + CELL_SIZE, HEX_HEIGHT * diameter + HEX_HEIGHT));
    }
}