package game.ui.cell;

import game.Main;
import game.model.field.Cell;
import game.ui.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.HashMap;
import java.util.Map;

/**
 * Виджет ячейки (гексагональный).
 *
 * @see Cell
 */
public class CellWidget extends JPanel {

    public enum Layer {
        TOP, BOTTOM
    }

    private final Map<Layer, CellItemWidget> items = new HashMap<>();
    private static final int CELL_SIZE = 80; // Suitable for hex grid

    private final Cell cell; // Reference to the model cell

    public CellWidget(Cell cell) {
        this.cell = cell;
        setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        setOpaque(false);
        setLayout(null);

        // Make the hex clickable
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                System.out.println("Clicked on CellWidget...");
                if (isPointInHex(e.getX(), e.getY())) {
                    cell.obstruct(Main.OBSTRUCTION_TYPE);
                }
            }
        });
    }

    private boolean isPointInHex(int px, int py) {
        // Simple hit-test for point inside hexagon
        Path2D hex = createHexShape(CELL_SIZE / 2, CELL_SIZE / 2, CELL_SIZE / 2 - 2);
        return hex.contains(px, py);
    }

    public Cell getCell() {
        return cell;
    }

    public void addItem(CellItemWidget item) {
        if (items.size() > 2) throw new IllegalArgumentException();

        if (items.containsKey(Layer.BOTTOM)) {
            item.setState(CellItemWidget.State.SMALL);
        } else {
            item.setState(CellItemWidget.State.DEFAULT);
        }

        if (items.containsKey(Layer.TOP)) {
            item.setState(CellItemWidget.State.DEFAULT);
            items.get(Layer.TOP).setState(CellItemWidget.State.SMALL);
        }

        items.put(item.getLayer(), item);

        // Center the item widget
        Dimension dim = item.getPreferredSize();
        int x = (CELL_SIZE - dim.width) / 2;
        int y = (CELL_SIZE - dim.height) / 2;
        item.setBounds(x, y, dim.width, dim.height);

        add(item);
        repaint();
    }

    public void removeItem(CellItemWidget item) {
        if (items.containsValue(item)) {
            if (item.getLayer() == Layer.BOTTOM && items.containsKey(Layer.TOP)) {
                items.get(Layer.TOP).setState(CellItemWidget.State.DEFAULT);
            }
            if (item.getLayer() == Layer.TOP && items.containsKey(Layer.BOTTOM)) {
                items.get(Layer.BOTTOM).setState(CellItemWidget.State.DEFAULT);
            }
            remove(item);
            items.remove(item.getLayer());
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw hexagon
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Path2D hex = createHexShape(CELL_SIZE / 2, CELL_SIZE / 2, CELL_SIZE / 2 - 2);
        if (cell != null && cell.isBlocked()) {
            g2.setColor(Color.DARK_GRAY);
        } else {
            g2.setColor(ImageUtils.BACKGROUND_COLOR);
        }
        g2.fill(hex);

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.draw(hex);
    }

    private Path2D createHexShape(int cx, int cy, int r) {
        Path2D path = new Path2D.Double();
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i - 30);
            double x = cx + r * Math.cos(angle);
            double y = cy + r * Math.sin(angle);
            if (i == 0) path.moveTo(x, y);
            else path.lineTo(x, y);
        }
        path.closePath();
        return path;
    }
}