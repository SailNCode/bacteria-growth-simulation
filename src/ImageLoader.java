import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class ImageLoader {
    public static BufferedImage loadImage(String imageFileName) {
        try (InputStream stream = ImageLoader.class.getResourceAsStream("/resources/images/" + imageFileName)) {
            if (stream == null) {
                throw new RuntimeException("Image not found!");
            }

            return ImageIO.read(stream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
