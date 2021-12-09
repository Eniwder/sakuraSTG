package character;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import barrage.Bullet;


public abstract class AbstractCharacter {
	// キャラの属性
	public double x, y;
	protected BufferedImage icon = null;
	protected static BufferedImage dummy,temp;
	public int iconW,iconH;
	protected final List<Bullet> bl = new LinkedList<>();
	private final List<Bullet> rem = new LinkedList<>();
	public int r;
	public boolean isLive=true;

	static{
		try {
			dummy = ImageIO.read(new File("res/img/character/dummy.png"));
		} catch (Exception e) {
			e.printStackTrace();
			dummy = null;
		}

	}


	public BufferedImage getImage() {
		return icon;
	}

	public final int getX() {
		return (int) x;
	}

	public final int getMX(){
		return (int)x+iconW/2;
	}

	public final int getY() {
		return (int) y;
	}
	public final int getMY() {
		return (int) y+iconH/2;
	}
	public final int getR() {
		return r;
	}
	public final List<Bullet> getBullets(){
		return bl;
	}

	public final List<Bullet> getRemoveBullets(){
		return rem;
	}

	abstract public void hit();

}
