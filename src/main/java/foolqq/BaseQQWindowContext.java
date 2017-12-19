package foolqq;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import foolqq.listener.GlobalKeyListener;
import foolqq.model.QQMsg;
import foolqq.model.QQWindow;
import foolqq.task.WindowHandleTask;
import static foolqq.tool.ClipboardTool.*;
import static foolqq.tool.QQWindowTool.*;

public abstract class BaseQQWindowContext {

	private Map<String, QQWindow> map = new HashMap<String, QQWindow>();

	private Robot robot;

	private int interval = 200;

	private int checkInterval = 5;

	private BufferedImage pImage;

	public int getCheckInterval() {
		return checkInterval;
	}

	public void setCheckInterval(int checkInterval) {
		this.checkInterval = checkInterval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public BaseQQWindowContext(File point) throws AWTException, IOException, NativeHookException {
		robot = new Robot();
		pImage = ImageIO.read(point);
		WindowHandleTask wintask = new WindowHandleTask(this, map, robot);
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(wintask, checkInterval, checkInterval,
				TimeUnit.SECONDS);
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
		logger.setUseParentHandlers(false);
		GlobalScreen.registerNativeHook();
		GlobalScreen.addNativeKeyListener(new GlobalKeyListener());
	}

	public abstract void onMessage(String name, QQMsg msg);

	public synchronized void clearQQMsg() {
		robot.keyPress(KeyEvent.VK_F10);
		robot.keyRelease(KeyEvent.VK_F10);
	}

	public synchronized String readQQMsg(String name) {
		int x = map.get(name).getX();
		int y = map.get(name).getY();
		if (x == 0 || y == 0)
			return null;
		BufferedImage image = getScreen(robot);
		int height = image.getHeight();
		for (int i = x - 100; i < x + 200; ++i) {
			for (int j = y + 100; j < height - 100; ++j) {
				if (isEqual(i, j, image, pImage)) {
					robot.mouseMove(i + 261, j - 100+86);
//					robot.delay(interval);
 					robot.mousePress(InputEvent.BUTTON1_MASK);
					robot.mouseRelease(InputEvent.BUTTON1_MASK);
//					robot.delay(interval);
					robot.keyPress(KeyEvent.VK_CONTROL);
					robot.keyPress(KeyEvent.VK_A);
					robot.keyRelease(KeyEvent.VK_A);
					robot.keyRelease(KeyEvent.VK_CONTROL);
//					robot.delay(interval);
					robot.keyPress(KeyEvent.VK_CONTROL);
					robot.keyPress(KeyEvent.VK_C);
					robot.keyRelease(KeyEvent.VK_C);
					robot.keyRelease(KeyEvent.VK_CONTROL);
//					robot.delay(interval);
//					robot.mousePress(InputEvent.BUTTON1_MASK);
//					robot.mouseRelease(InputEvent.BUTTON1_MASK);
//
//					robot.mousePress(InputEvent.BUTTON3_MASK);
//					robot.mouseRelease(InputEvent.BUTTON3_MASK);
//					robot.delay(interval);
//					robot.mouseMove(i + 261+95, j - 100+86+149);
////					robot.delay(interval);
//					robot.mousePress(InputEvent.BUTTON1_MASK);
//					robot.mouseRelease(InputEvent.BUTTON1_MASK);
					return getSystemClipboard();
				}
			}
		}
		return null;
	}

	public synchronized void writeQQMsg(String name, Object msg) {
		int x = map.get(name).getX();
		int y = map.get(name).getY();
		if (x == 0 || y == 0)
			return;
		BufferedImage image = getScreen(robot);
		int height = image.getHeight();
		for (int i = x - 100; i < x + 200; ++i) {
			for (int j = y + 100; j < height - 100; ++j) {
				if (isEqual(i, j, image, pImage)) {
					robot.mouseMove(i + 150, j + 50);
					robot.delay(interval);
					robot.mousePress(InputEvent.BUTTON1_MASK);
					robot.mouseRelease(InputEvent.BUTTON1_MASK);
					robot.delay(interval);
					if (msg instanceof String) {
						setSysClipboardText((String) msg);
					} else if (msg instanceof File) {
						Image imgObj = null;
						try {
							imgObj = ImageIO.read((File) msg);
						} catch (IOException e) {
							e.printStackTrace();
						}
						setClipboardImage(imgObj);

					} else if (msg instanceof Image) {
						setClipboardImage((Image) msg);
					}

					robot.keyPress(KeyEvent.VK_CONTROL);
					robot.keyPress(KeyEvent.VK_A);
					robot.keyRelease(KeyEvent.VK_A);
					robot.delay(interval);
					robot.keyPress(KeyEvent.VK_V);
					robot.keyRelease(KeyEvent.VK_V);
					robot.keyRelease(KeyEvent.VK_CONTROL);
					robot.delay(interval);
					robot.keyPress(KeyEvent.VK_ENTER);
					robot.keyRelease(KeyEvent.VK_ENTER);
					break;
				}
			}
		}
	}

}
