package game.ui.cell;

import game.model.field.Cat;
import game.ui.utils.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Виджет кота для ячейки.
 *
 * @see Cat
 */
public class CatWidget extends CellItemWidget {

    private final Cat cat;

    public CatWidget(Cat cat) {
        this.cat = cat;
    }

    @Override
    protected BufferedImage getImage() {
        BufferedImage image = null;
        try {
            // Make sure your file is named "cat.png" and located in IMAGE_PATH!
            image = ImageIO.read(new File(ImageUtils.IMAGE_PATH + "cat.png"));
            // You can adjust the size as needed
            image = ImageUtils.resizeImage(image, 100, 100);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public CellWidget.Layer getLayer() {
        return CellWidget.Layer.TOP;
    }

    @Override
    protected Dimension getDimension() {
        return new Dimension(100, 100);
    }
}