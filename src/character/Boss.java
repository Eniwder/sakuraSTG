package character;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;

import menu.Card;
import window.MyFrame;
import barrage.AbstractBarrage;
import etc.Main;
import etc.SE;

public class Boss extends AbstractCharacter {

	// 属性
	private int hp;

	// 動き用の変数
	private static final double RAD = Math.PI / 90;
	private static final double MOVE_RANGE = 10;
	private static final int DEFAULT_TIME = 60;
	private static final double MAX_Y = MyFrame.HEIGHT / 3+55;
	private static final double MIN_X = 30;
	private static final double MIN_Y = 60;
	private static final double SPACE = 60;
	private boolean isMoving = false, isFloat = true;
	private double rad, tx, ty;
	private double dx, dy, v0x, v0y, ax, ay;
	private int tt, dt;
	private Random rand;
	private BufferedImage mahojin;

	// 弾幕
	AbstractBarrage barrage;

	public Boss(Card card) {

		// ボス画像読み込み
		try {
			icon = ImageIO.read(new File("res/img/character/boss.png"));
			mahojin = ImageIO.read(new File("res/img/character/mahojin_bokasi.png"));
		} catch (Exception e) {
			e.printStackTrace();
			icon = mahojin = null;
		}

		iconW = icon.getWidth();
		iconH = icon.getHeight();

		x = MyFrame.WIDTH / 2 - iconW / 2;
		y = MyFrame.HEIGHT * 0.3;

		this.barrage = card.barrage;
		this.hp = card.hp;
		this.r = 40;

		ty = y;
	}

	public void move() {
		// 移動中でなければフワフワ動く
		if (!isMoving && isFloat) {
			y = Math.sin(rad) * MOVE_RANGE + ty;
			rad += RAD;
		} else {
			if (--tt >= 0) {
				int t = (dt - tt);
				x = tx - ((v0x * t) - 0.5 * ax * t * t);
				y = ty - ((v0y * t) - 0.5 * ay * t * t);
			} else {
				if (tt <= -5) {
					ty = y;
					tx = x;
					isMoving = false;
					rad = 0;
				}
			}
		}
	}

	public void calcMove(int ddx, int ddy, int time) {
		isMoving = true;
		dx = getMX() - ddx;
		dy = getMY() - ddy;
		v0x = 2 * dx / time;
		v0y = 2 * dy / time;
		ax = 2 * dx / (time * time);
		ay = 2 * dy / (time * time);
		tx = x;
		ty = y;
		tt = dt = time;
	}

	public void calcMove(int dx, int dy) {
		calcMove(dx, dy, DEFAULT_TIME);
	}

	public void randomMove(int range) {
		int dx, dy;

		do {
			dx = (int) (rand.nextDouble() * range * 2) - range;
			dx += (dx > 0 ? MIN_X : -MIN_X) + getMX();
		} while ((dx - SPACE) <= 0 || (dx + iconW/2 + SPACE) >= (MyFrame.WIDTH));

		do {
			dy = (int) (rand.nextDouble() * range) - range / 2;
			dy += (dy > 0 ? MIN_Y : -MIN_Y) + getMY();
		} while ((dy - SPACE) <= 0 || dy >= MAX_Y);

		calcMove(dx, dy);
	}

	public void shot() {
		barrage.move();
	}

	@Override
	public void hit() {
		SE.start("hit");
		autoHit();
	}

	public void autoHit() {
		if (--hp <= 0) {
			isLive = false;
			SE.start("atack");
		}
	}

	public void init(Player sakura, long seed) {
		Main.rp.setSeed(seed);
		rand = new Random(seed);
		barrage.init(this, sakura);
		barrage.setRandom(seed);
	}

	public int getHp() {
		return hp;
	}
	public void setHP(int hp){
		this.hp=hp;
	}

	public BufferedImage getMahojin() {
		return mahojin;
	}

	public void setFloat(boolean f) {
		isFloat = f;
	}

	public void clearMahojin() {
		mahojin = dummy;
	}

	public void warp(int x,int y){
		this.x=this.tx=x;
		this.y=this.ty=y;
	}


}
