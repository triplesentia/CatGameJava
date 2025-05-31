package game.ui.cell;

import game.model.field.Cat;
import game.ui.utils.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CatWidget extends CellItemWidget {

    private final Cat cat;
    private static final int CAT_SIZE = 48; // Should be <= CELL_SIZE

    public CatWidget(Cat cat) {
        this.cat = cat;
    }

    @Override
    protected BufferedImage getImage() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(ImageUtils.IMAGE_PATH + "cat.png"));
            image = ImageUtils.resizeImage(image, CAT_SIZE, CAT_SIZE);
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
        return new Dimension(CAT_SIZE, CAT_SIZE);
    }
}