package foolqq.tool;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class QQWindowTool {

	public static boolean isEqual(int x, int y, BufferedImage image, BufferedImage point) {
		int pointW = point.getWidth();
		int pointY = point.getHeight();

		for (int m = 0; m < pointW; m++)
			for (int n = 0; n < pointY; n++) {
				if (image.getRGB(x + m, y + n) != point.getRGB(m, n)) {
					return false;
				}
			}

		return true;
	}

	public static String getImgName(String name) {

		return name.replaceAll(".png", "");

	}

	public static BufferedImage getScreen(Robot robot) {

		return robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

	}
}
