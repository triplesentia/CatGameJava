package game.ui.cell;

import game.model.field.Cat;
import game.ui.cell.CellWidget.Layer;
import game.ui.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Виджет кота для ячейки.
 */
public class CatWidget extends CellItemWidget {
    private final Cat cat;
    private final Color color;

    public CatWidget(Cat cat, Color color) {
        this.cat = cat;
        this.color = color;
    }

    @Override
    protected BufferedImage getImage() {
        // Draw a simple colored circle for the cat, or load your cat image here
        int size = getDimension().width;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(color);
        g2.fillOval(5, 5, size - 10, size - 10);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3));
        g2.drawOval(5, 5, size - 10, size - 10);
        g2.dispose();
        return img;
    }

    @Override
    public Layer getLayer() {
        return Layer.TOP;
    }

    @Override
    protected Dimension getDimension() {
        return new Dimension(40, 40); // Cat size, tweak as needed
    }
}