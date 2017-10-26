package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class representing image
 *
 * @author Litovka Sergii
 */
public class Image {

    private BufferedImage image;

    /**
     * Create image from other image
     *
     * @param image
     */

    public Image(BufferedImage image) {

        this.image = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics graphic = this.image.createGraphics();
        graphic.drawImage(image, 0, 0, null);
        graphic.dispose();
    }

    /**
     * Create empty image with given width and height
     *
     * @param width
     * @param height
     */

    public Image(int width, int height) {

        if (width <= 0 || height <= 0)
            throw new IllegalArgumentException("Dimensions must be positive!");

        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Create image from file
     *
     * @param file
     * @throws IOException
     */

    public Image(File file) throws IOException {
        this.image = ImageIO.read(file);
    }

    /**
     * Get image
     *
     * @return
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Set image
     *
     * @param image
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * Returns pixel RGB value
     *
     * @param x
     * @param y
     * @return RGB value
     */

    public int getRGB(int x, int y) {

        return image.getRGB(x, y);
    }

    /**
     * Sets RGB value for given pixel position
     *
     * @param x
     * @param y
     * @param rgb
     */

    public void setRGB(int x, int y, int rgb) {
        image.setRGB(x, y, rgb);
    }

    /**
     * Sets RGB value for given pixel position.
     *
     * @param x
     * @param y
     * @param red
     * @param green
     * @param blue
     */
    public void setRGB(int x, int y, int red, int green, int blue)
    {
        if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0
                || blue > 255)
            throw new IllegalArgumentException(
                    "Band values must be between 0 and 255");

        image.setRGB(x, y, (new Color(red, green, blue)).getRGB());
    }

    /**
     * Returns width of image
     *
     * @return
     */
    public int getWidth() {
        return image.getWidth();
    }

    /**
     * Returns height of image
     *
     * @return
     */
    public int getHeight() {
        return image.getHeight();
    }

    /**
     * Returns value of red channel in given position of image.
     *
     * @param x
     * @param y
     * @return
     */
    public int getRed(int x, int y)
    {
        int rgb = image.getRGB(x, y);
        return (rgb >> 16) & 0xff;
    }

    /**
     * Returns value of green channel in given position of image.
     *
     * @param x
     * @param y
     * @return
     */
    public int getGreen(int x, int y)
    {
        int rgb = image.getRGB(x, y);
        return (rgb >> 8) & 0xff;
    }

    /**
     * Returns value of blue channel in given position in image.
     *
     * @param x
     * @param y
     * @return
     */
    public int getBlue(int x, int y)
    {
        int rgb = image.getRGB(x, y);
        return (rgb) & 0xff;
    }

}
