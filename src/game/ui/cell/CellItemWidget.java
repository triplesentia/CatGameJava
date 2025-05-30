package game.ui.cell;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Абстрактный виджет для объекта в ячейке.
 */
public abstract class CellItemWidget extends JPanel {

    public enum State {
        DEFAULT,
        SMALL
    }

    protected State cellItemState = State.DEFAULT;

    public CellItemWidget() {
        setState(State.DEFAULT);
        setOpaque(false);
    }

    void setState(State state) {
        cellItemState = state;
        setPreferredSize(getDimension());
        repaint();
        revalidate();
    }

    public State getState() {
        return cellItemState;
    }

    protected abstract BufferedImage getImage();

    public abstract CellWidget.Layer getLayer();

    protected abstract Dimension getDimension();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(getImage(), 0, 0, null);
    }
}