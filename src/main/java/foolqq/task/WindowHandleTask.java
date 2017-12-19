package foolqq.task;

import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import foolqq.BaseQQWindowContext;
import foolqq.model.QQMsg;
import foolqq.model.QQWindow;
import static foolqq.tool.QQWindowTool.*;

public class WindowHandleTask implements Runnable {

	private BaseQQWindowContext context;

	private Map<String, QQWindow> map;

	private Robot robot;

	private static String msgHeadRegExp = "(.*)\\((.+)\\)\\s+([0-9]{1,2}:[0-9]{2}:[0-9]{2})";

	public WindowHandleTask(BaseQQWindowContext context, Map<String, QQWindow> map, Robot robot) {
		this.context = context;
		this.map = map;
		this.robot = robot;
	}

	@Override
	public void run() {
		synchronized (context) {
			map.clear();
			BufferedImage image = getScreen(robot);
			int width = image.getWidth();
			int height = image.getHeight();
			File[] f = new File(".").listFiles();
			for (int i = 0; i < f.length; i++) {
				if (f[i].getName().endsWith(".png") && !f[i].getName().equals("point.png")) {
					BufferedImage img = null;
					try {
						img = ImageIO.read(new File(f[i].getName()));
					} catch (IOException e) {
						e.printStackTrace();
					}

					String name = getImgName(f[i].getName());
					QQWindow win = new QQWindow(name, img);

					for (int x = 10; x < width - 200; ++x) {
						for (int y = 10; y < height - 200; ++y) {
							if (isEqual(x, y, image, img)) {
								win.setX(x);
								win.setY(y);
								break;
							}
						}
					}

					if (win.getX() == 0 || win.getY() == 0)
						continue;

					map.put(name, win);
					String msg = context.readQQMsg(name);
					if (msg != null && msg.trim().length() > 0) {
						List<QQMsg> stack = getMsgStack(msg.trim().split("\n"));
						for (QQMsg m : stack) {
							context.onMessage(name, m);
						}

						context.clearQQMsg();
					}
				}
			}
		}

	}

	private List<QQMsg> getMsgStack(String[] msgs) {

		List<QQMsg> stack = new ArrayList<QQMsg>();
		for (int j = 0; j < msgs.length; j++) {
			if (msgs[j].matches(msgHeadRegExp)) {
				Pattern pat = Pattern.compile(msgHeadRegExp);
				Matcher mat = pat.matcher(msgs[j]);
				QQMsg qqMsg = new QQMsg();
				if (mat.find()) {

					String trim = mat.group(1).trim();
					int i = trim.indexOf("】");
					if (i != -1) {
						qqMsg.setNick(trim.substring(i+1));
 					} else {
						qqMsg.setNick(trim);
 					}

					qqMsg.setQqOrEmail(mat.group(2).trim());
					qqMsg.setTime(mat.group(3).trim());
					stack.add(qqMsg);
				}
			} else {
				if (stack.size() > 0) {
					QQMsg last = stack.get(stack.size() - 1);
					last.setContent(last.getContent() + msgs[j] + "\n");
				}
			}
		}

		return stack;
	}

}
