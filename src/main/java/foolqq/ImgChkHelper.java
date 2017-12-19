package foolqq;

import static foolqq.tool.QQWindowTool.isEqual;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImgChkHelper {

	public static boolean validImage(String screen, String img) throws IOException {

		BufferedImage screenImage = ImageIO.read(new File(screen));
		BufferedImage point = ImageIO.read(new File(img));
		int width = screenImage.getWidth();
		int height = screenImage.getHeight();
		for (int x = 10; x < width - 200; ++x) {
			for (int y = 10; y < height - 200; ++y) {
				if (isEqual(x, y, screenImage, point)) {
					return true;
				}
			}
		}

		return false;
	}

}
