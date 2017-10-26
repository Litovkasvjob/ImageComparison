import model.Image;
import model.Rectangle;
import utils.ImageUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Main application class
 *
 * @author Litovka Sergii
 */
public class App {

    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        List<File> files = ImageUtils.enterFileNames(scanner);

        Image imageOld = new Image(files.get(0));
        Image imageNew = new Image(files.get(1));

        Image image = ImageUtils.subtractImages(imageOld, imageNew, new Color(0, 0, 0).getRGB());

        List<Rectangle> list = ImageUtils.getRectangles(image, new Color(0, 0, 0).getRGB(), new Color(255, 0, 0).getRGB());

        Image im = ImageUtils.drawRectangleOnImage(imageNew, list, new Color(255, 0, 0));

        String name = ImageUtils.enterFinishFileName(scanner);

        ImageUtils.save(im, name);


    }
}




