package game.ui.cell;

import game.Main;
import game.model.field.Cell;

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
    private static final int CELL_SIZE = 60;
    private static final int HEX_HEIGHT = (int) (Math.sqrt(3) / 2 * CELL_SIZE);

    private final Cell cell; // Reference to the model cell

    private boolean isHovered = false;

    public CellWidget(Cell cell) {
        this.cell = cell;
        setPreferredSize(new Dimension(CELL_SIZE, HEX_HEIGHT));
        setOpaque(false);
        setLayout(null);

        // Make the hex clickable
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                System.out.println("Clicked...");
                if (isPointInHex(e.getX(), e.getY())) {
                    cell.requestObstruction();
                }
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    private boolean isPointInHex(int px, int py) {
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
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int r = CELL_SIZE / 2 - 2;
        int cx = CELL_SIZE / 2;
        int cy = HEX_HEIGHT / 2;
        Path2D hex = createHexShape(cx, cy, r);

        // Fill color logic
        Color fillColor;
        if (cell != null && cell.isBlocked()) {
            if (cell.getStepsUntilUnblock() == -1) {
                fillColor = new Color(52, 255, 0, 180);
            }
            else if (cell.getStepsUntilUnblock() == 1) {
                fillColor = new Color(255, 0, 0, 180);
            }
            else {
                fillColor = new Color(255, 145, 0, 180);
            }
        } else if (isHovered) {
            fillColor = new Color(180, 220, 255, 200); // light blue on hover
        } else {
            fillColor = new Color(255, 255, 230); // soft pastel yellow
        }

        g2.setColor(fillColor);
        g2.fill(hex);

        // Optional: Soft highlight/shadow inside
        if (isHovered && (cell == null || !cell.isBlocked())) {
            g2.setColor(new Color(100, 180, 255, 80));
            g2.setStroke(new BasicStroke(4));
            g2.draw(hex);
        }

        // Draw outline
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.draw(hex);

        g2.dispose();
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