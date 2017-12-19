package foolqq.model;

import java.awt.image.BufferedImage;

public class QQWindow {
	
	private int x;
	
	private int y;
	
	private String name; 
	
	private BufferedImage image;
 
	public QQWindow(String name, BufferedImage image) {
		super();
		this.name = name;
		this.image = image;
	}
	
	
    public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}


	public QQWindow(String name ) {
		super();
		this.name = name; 
	}
 
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "QQWindow [x=" + x + ", y=" + y + ", name=" + name + "]";
	}

}
