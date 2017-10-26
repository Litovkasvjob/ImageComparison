package utils;

import model.Image;
import model.Rectangle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Class to manipulate with images
 *
 * @author Litovka Sergii
 */
public class ImageUtils {

    /**
     * Receive images
     *
     * @param scanner
     * @return List of files with images
     */

    public static List<File> enterFileNames(Scanner scanner) throws FileNotFoundException {
        List<File> files = new ArrayList<File>(2);
        for (int i = 0; i < 2; i++) {
            System.out.println("Enter name of  file");
            String fileName = null;
            File file = null;

            if (scanner.hasNext()) {
                fileName = scanner.next();
                file = new File(fileName);
                if (!file.exists()) {
                    throw new FileNotFoundException("Such file is not existed");
                }
            } else {
                throw new FileNotFoundException("You enter uncorrect name");
            }
            files.add(file);
        }
        return files;
    }


    /**
     * Subtracts two images and returns the result.
     *
     * @param imageOld
     * @param imageNew
     * @param shapeColor color to set in different pixel
     * @return image - result of subtraction
     */
    public static Image subtractImages(Image imageOld, Image imageNew, int shapeColor) {
        if (imageOld.getWidth() != imageNew.getWidth()
                || imageOld.getHeight() != imageNew.getHeight())
            throw new IllegalArgumentException(
                    "Both images should have the same resolution");

        Image result = new Image(imageOld.getWidth(), imageOld.getHeight());

        boolean difference = false;

        for (int w = 0; w < imageOld.getWidth(); w++) {
            for (int h = 0; h < imageOld.getHeight(); h++) {

                difference = calculateDifferenceInPixels(imageOld, imageNew, w, h);

                if (difference) {
                    result.setRGB(w, h, shapeColor); // change pixel color to wantedColor (black)
                }
            }
        }
        return result;
    }

    /**
     * Get list of rectangle
     *
     * @param image
     * @param shapeColor color of difference
     * @param setColor   color to change
     * @return
     */

    public static List<Rectangle> getRectangles(Image image, int shapeColor, int setColor) {
        List<Rectangle> rectangles = new ArrayList<>();

        for (int x = 0; x < image.getWidth(); x++)
            for (int y = 0; y < image.getHeight(); y++)
                if (image.getRGB(x, y) == shapeColor)
                    rectangles.add(getRectangleForShape(image, x, y,
                            setColor));

        return rectangles;
    }

    /**
     * Get Rectangle for shape
     *
     * @param image
     * @param x
     * @param y
     * @param setColor
     * @return Rectangle
     */
    private static Rectangle getRectangleForShape(Image image, int x, int y,
                                                  int setColor) {
        int shapeColor = image.getRGB(x, y);
        List<Integer> pointsX = new ArrayList<>();
        List<Integer> pointsY = new ArrayList<>();

        getShapePoints(image, x, y, shapeColor, setColor, pointsX, pointsY);

        Integer[][] points = new Integer[2][];

        points[0] = pointsX.toArray(new Integer[pointsX.size()]);
        points[1] = pointsY.toArray(new Integer[pointsY.size()]);

        return createRectangleForPoints(points);
    }

    /**
     * Get shape points
     *
     * @param image
     * @param x
     * @param y
     * @param shapeColor
     * @param setColor
     * @param pointsX
     * @param pointsY
     */
    private static void getShapePoints(Image image, int x, int y, int shapeColor,
                                       int setColor, List<Integer> pointsX, List<Integer> pointsY) {
        pointsX.add(x);
        pointsY.add(y);
        image.setRGB(x, y, setColor);

        // Left
        if (x > 0 && image.getRGB(x - 1, y) == shapeColor)
            getShapePoints(image, x - 1, y, shapeColor, setColor, pointsX,
                    pointsY);
        // Left top
        if (x > 0 && y > 0 && image.getRGB(x - 1, y - 1) == shapeColor)
            getShapePoints(image, x - 1, y - 1, shapeColor, setColor,
                    pointsX, pointsY);
        // Top
        if (y > 0 && image.getRGB(x, y - 1) == shapeColor)
            getShapePoints(image, x, y - 1, shapeColor, setColor, pointsX,
                    pointsY);
        // Right top
        if (x < image.getWidth() - 1 && y > 0
                && image.getRGB(x + 1, y - 1) == shapeColor)
            getShapePoints(image, x + 1, y - 1, shapeColor, setColor,
                    pointsX, pointsY);
        // Right
        if (x < image.getWidth() - 1 && image.getRGB(x + 1, y) == shapeColor)
            getShapePoints(image, x + 1, y, shapeColor, setColor, pointsX,
                    pointsY);
        // Right down
        if (x < image.getWidth() - 1 && y < image.getHeight() - 1
                && image.getRGB(x + 1, y + 1) == shapeColor)
            getShapePoints(image, x + 1, y + 1, shapeColor, setColor,
                    pointsX, pointsY);
        // Down
        if (y < image.getHeight() - 1 && image.getRGB(x, y + 1) == shapeColor)
            getShapePoints(image, x, y + 1, shapeColor, setColor, pointsX,
                    pointsY);
        // Left down
        if (x > 0 && y < image.getHeight() - 1
                && image.getRGB(x - 1, y + 1) == shapeColor)
            getShapePoints(image, x - 1, y + 1, shapeColor, setColor,
                    pointsX, pointsY);

        // check of near 2 pixel

        // Left
        if (x > 1 && image.getRGB(x - 2, y) == shapeColor)
            getShapePoints(image, x - 2, y, shapeColor, setColor, pointsX,
                    pointsY);
        // Left top
        if (x > 1 && y > 1 && image.getRGB(x - 2, y - 2) == shapeColor)
            getShapePoints(image, x - 2, y - 2, shapeColor, setColor,
                    pointsX, pointsY);
        // Top
        if (y > 1 && image.getRGB(x, y - 2) == shapeColor)
            getShapePoints(image, x, y - 2, shapeColor, setColor, pointsX,
                    pointsY);
        // Right top
        if (x < image.getWidth() - 2 && y > 1
                && image.getRGB(x + 2, y - 2) == shapeColor)
            getShapePoints(image, x + 2, y - 2, shapeColor, setColor,
                    pointsX, pointsY);
        // Right
        if (x < image.getWidth() - 2 && image.getRGB(x + 2, y) == shapeColor)
            getShapePoints(image, x + 2, y, shapeColor, setColor, pointsX,
                    pointsY);
        // Right down
        if (x < image.getWidth() - 2 && y < image.getHeight() - 2
                && image.getRGB(x + 2, y + 2) == shapeColor)
            getShapePoints(image, x + 2, y + 2, shapeColor, setColor,
                    pointsX, pointsY);
        // Down
        if (y < image.getHeight() - 2 && image.getRGB(x, y + 2) == shapeColor)
            getShapePoints(image, x, y + 2, shapeColor, setColor, pointsX,
                    pointsY);
        // Left down
        if (x > 1 && y < image.getHeight() - 2
                && image.getRGB(x - 2, y + 2) == shapeColor)
            getShapePoints(image, x - 2, y + 2, shapeColor, setColor,
                    pointsX, pointsY);
        // check of near 3 pixel

        // Left
        if (x > 2 && image.getRGB(x - 3, y) == shapeColor)
            getShapePoints(image, x - 3, y, shapeColor, setColor, pointsX,
                    pointsY);
        // Left top
        if (x > 2 && y > 2 && image.getRGB(x - 3, y - 3) == shapeColor)
            getShapePoints(image, x - 3, y - 3, shapeColor, setColor,
                    pointsX, pointsY);
        // Top
        if (y > 2 && image.getRGB(x, y - 3) == shapeColor)
            getShapePoints(image, x, y - 3, shapeColor, setColor, pointsX,
                    pointsY);
        // Right top
        if (x < image.getWidth() - 3 && y > 2
                && image.getRGB(x + 3, y - 3) == shapeColor)
            getShapePoints(image, x + 3, y - 3, shapeColor, setColor,
                    pointsX, pointsY);
        // Right
        if (x < image.getWidth() - 3 && image.getRGB(x + 3, y) == shapeColor)
            getShapePoints(image, x + 3, y, shapeColor, setColor, pointsX,
                    pointsY);
        // Right down
        if (x < image.getWidth() - 3 && y < image.getHeight() - 3
                && image.getRGB(x + 3, y + 3) == shapeColor)
            getShapePoints(image, x + 3, y + 3 , shapeColor, setColor,
                    pointsX, pointsY);
        // Down
        if (y < image.getHeight() - 3 && image.getRGB(x, y + 3) == shapeColor)
            getShapePoints(image, x, y + 3, shapeColor, setColor, pointsX,
                    pointsY);
        // Left down
        if (x > 2 && y < image.getHeight() - 3
                && image.getRGB(x - 3, y + 3) == shapeColor)
            getShapePoints(image, x - 3, y + 3, shapeColor, setColor,
                    pointsX, pointsY);
    }

    /**
     * Create rectangle for points
     *
     * @param points
     * @return Rectangle
     */
    private static Rectangle createRectangleForPoints(Integer[][] points) {

        Rectangle rectangle = new Rectangle();

        Arrays.sort(points[0]);
        Arrays.sort(points[1]);

        rectangle.setX(points[0][0]);
        rectangle.setY(points[1][0]);


        rectangle.setLength(points[0][points[0].length - 1] - points[0][0]);
        rectangle.setHeight(points[1][points[1].length - 1] - points[1][0]);

        return rectangle;
    }

    /**
     * Calculate difference between Pixels
     *
     * @param imageOld image
     * @param imageNew image
     * @param x        coordinate
     * @param y        coordinate
     * @return true if difference more than 10%
     */

    public static boolean calculateDifferenceInPixels(Image imageOld, Image imageNew, int x, int y) {

        boolean flag = false;
        //difference more than 10%
        double differThan = 10d / 100;

        int distanceOld = imageOld.getRGB(x, y);
        int distanceNew = imageNew.getRGB(x, y);

        int difference = (int) (distanceOld * differThan);

        if (Math.abs(distanceNew) > Math.abs(distanceOld) + Math.abs(difference)
                || Math.abs(distanceNew) < Math.abs(distanceOld) - Math.abs(difference)) {
            flag = true;
        }

        return flag;
    }

    /**
     * Save image to file.
     *
     * @param filename
     * @throws IOException
     */
    public static void save(Image image, String filename) throws IOException {
        ImageIO.write(image.getImage(), "png", new File(filename));
    }

    /**
     * Draw Rectangle On Image
     * @param imageNew
     * @param list
     * @param rectangleColor
     * @return
     */
    public static Image drawRectangleOnImage(Image imageNew, List<Rectangle> list, Color rectangleColor) {
        BufferedImage buff = imageNew.getImage();
        for (Rectangle rectangle : list) {

            Graphics g = buff.createGraphics();
            g.setColor(rectangleColor);
            g.drawRect(rectangle.getX(), rectangle.getY(), rectangle.getLength(), rectangle.getHeight());
        }
        return new Image(buff);
    }

    /**
     * Enter FileName to save image
     *
     * @param scanner
     * @return name
     * @throws FileNotFoundException
     */

    public static String enterFinishFileName(Scanner scanner) throws FileNotFoundException {
        System.out.println("Enter name of subtraction file to save");
        String name = null;
        if (scanner.hasNext()) {
            name = scanner.next();
        } else {
            throw new FileNotFoundException("You enter uncorrect name");
        }
        return name;
    }


}
