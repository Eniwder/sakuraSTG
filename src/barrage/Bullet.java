package barrage;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import window.MyFrame;
import character.Player;

public class Bullet {
	static Player sakura;

	public static final int B_KIND = 5;
	public static final int B_SPE = 6;
	public static final int COLOR_KINDS = 4;
	public static final int BLUE = 0, RED = 1, YELLOW = 2, GREEN = 3,SPE = 0,SHADOW=1,EF=1;
	public static final int STAR = 0,SMALL = 1, MIDDLE = 2, BIG = 3, SOVAL = 4,MOKONA = 5,YUE=6,SWORD=7,SAKURA=8,GLOW=9,DARK=10;

	protected static BufferedImage[][] icon = new BufferedImage[B_KIND+B_SPE][COLOR_KINDS];
	public static int[] W = new int[B_KIND+B_SPE];
	public static int[] H = new int[B_KIND+B_SPE];
	public static int[] R = new int[B_KIND+B_SPE];
	public static int[] ER = new int[B_KIND+B_SPE];

	//フラグ、種類、カウンタ、色、状態、少なくとも消さない時間
	public int kind, count, state, col, till;
	//座標、角度、速度、ベースの角度、一時記憶スピード、加速度
	public double x, y, angle, speed, base_angle, rem_spd;

	// 種類、色、最低時間、初期座標x、y、初期角度、速度、目印
	public Bullet(int kind, int col, int till, double x, double y, double base_angle, double speed, int state) {
		this.kind = kind;
		this.col = col;
		this.till = till;
		this.x = x - W[kind] / 2;
		this.y = y - H[kind] / 2;
		this.base_angle = base_angle;
		this.speed = speed;
		this.state = state;
	}

	public static void setPlayer(Player p) {
		sakura = p;
	}

	public static void init() {

		try {
			// 自機ショット
			icon[STAR][STAR] = ImageIO.read(new File("res/img/bullet/bullet00.png"));
			W[STAR] = icon[STAR][STAR].getWidth();
			H[STAR] = icon[STAR][STAR].getHeight();
			R[STAR] = (W[STAR] > H[STAR] ? H[STAR] : W[STAR]);
			R[STAR] -= R[STAR] / 3;
			icon[STAR][EF] = ImageIO.read(new File("res/img/bullet/ef.png"));

			// 跳モコナ
			icon[MOKONA][0] = ImageIO.read(new File("res/img/bullet/mokona.png"));
			W[MOKONA] = icon[MOKONA][0].getWidth();
			H[MOKONA] = icon[MOKONA][0].getHeight();
			R[MOKONA] = (W[MOKONA] < H[MOKONA] ? H[MOKONA] : W[MOKONA])*2 / 5;
			ER[MOKONA] = -1;

			// 幻ユエ
			icon[YUE][0] = ImageIO.read(new File("res/img/character/boss.png"));
			W[YUE] = icon[YUE][0].getWidth();
			H[YUE] = icon[YUE][0].getHeight();
			R[YUE] = 40;
			ER[YUE] = -1;

			// 影ユエ
			icon[YUE][1] = ImageIO.read(new File("res/img/character/boss_shadow.png"));
			W[YUE] = icon[YUE][1].getWidth();
			H[YUE] = icon[YUE][1].getHeight();

			// 剣ソード
			icon[SWORD][0] = ImageIO.read(new File("res/img/bullet/sword.png"));
			W[SWORD] = icon[SWORD][0].getWidth();
			H[SWORD] = icon[SWORD][0].getHeight();
			R[SWORD] = 40;
			if (H[SWORD] != W[SWORD]) {
				ER[SWORD] = (W[SWORD] < H[SWORD] ? W[SWORD] : H[SWORD])/2;
			} else {
				ER[SWORD] = -1;
			}

			// 鏡さくら
			icon[SAKURA][0] = ImageIO.read(new File("res/img/character/sakura.png"));
			W[SAKURA] = icon[SAKURA][0].getWidth();
			H[SAKURA] = icon[SAKURA][0].getHeight();
			R[SAKURA] = 20;
			ER[SAKURA] = -1;

			// 闇
			icon[DARK][0] = ImageIO.read(new File("res/img/bullet/dark.png"));
			W[DARK] = icon[DARK][0].getWidth();
			H[DARK] = icon[DARK][0].getHeight();
			R[DARK] = 0;
			ER[DARK] = -1;


			// リサイズ可能な光玉
			icon[GLOW][GREEN] = ImageIO.read(new File("res/img/bullet/glowG.png"));
			icon[GLOW][RED] = ImageIO.read(new File("res/img/bullet/glowR.png"));
			icon[GLOW][BLUE] = ImageIO.read(new File("res/img/bullet/glowB.png"));
			icon[GLOW][YELLOW] = ImageIO.read(new File("res/img/bullet/glowY.png"));
			W[GLOW] = icon[GLOW][0].getWidth();
			H[GLOW] = icon[GLOW][0].getHeight();
			R[GLOW] = 260/2;
			ER[GLOW] = -1;
			for(int i=0;i<COLOR_KINDS;i++)ResizableBullet.init(i,0,100);

			for (int i = 1; i < B_KIND; i++) {
				for (int j = 0; j < COLOR_KINDS; j++) {
					icon[i][j] = ImageIO.read(new File("res/img/bullet/bullet" + i + "" + j + ".png"));
				}
				W[i] = icon[i][0].getWidth();
				H[i] = icon[i][0].getHeight();
				R[i] = (W[i] < H[i] ? H[i] : W[i])*2 / 5;
				if (H[i] != W[i]) {
					ER[i] = (W[i] < H[i] ? W[i] : H[i])/2;
				} else {
					ER[i] = -1;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Boolean move(int px, int py, int pr, List<Bullet> remove) {
		double xx, yy;
		count++;

		xx = Math.cos(base_angle) * speed;
		yy = Math.sin(base_angle) * speed;

		x += xx;
		y += yy;

		if (till < count && !inside()) {
			remove.add(this);
			return false;
		}
		return isHit(px,py,pr,remove);
	}

	boolean isHit(int px, int py, int pr, List<Bullet> remove){
		int x = px - getMX(), y = py - getMY(), r = R[kind] + pr;
		if ((r * r) > (x * x + y * y)) {
			if (ER[kind] == -1 || pr != sakura.r) {
				if (!(pr == sakura.r && sakura.isFast())) {
					if(remove!=null)remove.add(this);
					return true;
				}
			} else {
				r = ER[kind] + pr;
				if ((r * r) > (x * x + y * y)) {
					if (!(pr == sakura.r && sakura.isFast())) {
						if(remove!=null)remove.add(this);
						return true;
					}
				}
				int ex = (int) (R[kind] * Math.cos(base_angle) / 2), ey = (int) (R[kind] * Math.sin(base_angle) / 2);
				x = px - (int) (getMX() + ex);
				y = py - (int) (getMY() + ey);
				r = R[kind] / 2 + pr - 2;
				if ((r * r) > (x * x + y * y)) {
					if (!(pr == sakura.r && sakura.isFast())) {
						if(remove!=null)remove.add(this);
						return true;
					}
				}
				x = px - (int) (getMX() - ex);
				y = py - (int) (getMY() - ey);
				if ((r * r) > (x * x + y * y)) {
					if (!(pr == sakura.r && sakura.isFast())) {
						if(remove!=null)remove.add(this);
						return true;
					}
				}
			}
		}
		return false;
	}

	public int getMX() {
		return (int) x + W[kind] / 2;
	}

	public int getMY() {
		return (int) y + H[kind] / 2;
	}

	public Image getImage() {
		return icon[kind][col];
	}

	protected boolean inside() {
		int r = W[kind] > H[kind] ? W[kind] : H[kind];
		if (x - r > MyFrame.WIDTH || (x + r) <= 0 || y - r > MyFrame.AHEIGHT || (y + r) <= 0)
			return false;
		else
			return true;
	}

	public final double getRotate() {
		return base_angle - Math.PI * 3 / 2 + angle;
	}

	public static final BufferedImage getSakuraShot() {
		return icon[STAR][SPE];
	}

}
