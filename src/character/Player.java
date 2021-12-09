package character;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.EnumSet;
import java.util.Map;

import javax.imageio.ImageIO;

import menu.Game;
import window.MyFrame;
import barrage.Bullet;
import etc.Key;
import etc.Key.KeyCode;
import etc.Main;
import etc.Replay;
import etc.SE;

public class Player extends AbstractCharacter {
	private static final double S2 = 0.707056;
	private final int moving = 8;
	public int fc = -1000;
	public BufferedImage hantei;
	private boolean fast = false;
	EnumSet<Key.KeyCode> tkeys;
	private static int N = 0, L = 1, R = 2;
	private int istate = N;
	private BufferedImage[][] icons = new BufferedImage[2][3];
	private int frame;
	private boolean isReplay = false;// リプレイかどうかのフラグ
	private Map<Integer, EnumSet<Key.KeyCode>> keymap;
	EnumSet<Key.KeyCode> keys = EnumSet.noneOf(KeyCode.class);

	public Player() {
		// 自機画像読み込み
		try {
			icon = ImageIO.read(new File("res/img/character/sakura.png"));

			icons[0][N] = ImageIO.read(new File("res/img/character/sakura.png"));
			icons[0][L] = ImageIO.read(new File("res/img/character/sakuraL.png"));
			icons[0][R] = ImageIO.read(new File("res/img/character/sakuraR.png"));

			icons[1][N] = ImageIO.read(new File("res/img/character/sakuraFast.png"));
			icons[1][L] = ImageIO.read(new File("res/img/character/sakuraFL.png"));
			icons[1][R] = ImageIO.read(new File("res/img/character/sakuraFR.png"));

			hantei = ImageIO.read(new File("res/img/character/hantei.png"));
		} catch (Exception e) {
			e.printStackTrace();
			icon = null;
		}

		iconW = icon.getWidth();
		iconH = icon.getHeight();

		// 初期座標セット
		x = MyFrame.WIDTH / 2 - iconW / 2;
		y = MyFrame.HEIGHT * 0.7;

		// 当たり判定
		r = 1;

		// リプレイ保存用のフレームカウント
		frame = 0;
	}

	public void setDummyToHantei() {
		temp = hantei;
		hantei = dummy;
	}

	public void resetDummyFromHantei() {
		if(temp!=null)hantei=temp;
	}

	public Player(Replay rep) {
		this();
		keymap = rep.getMap();
		isReplay = true;
	}

	public void move() {
		if (isReplay) {
			if (keymap.containsKey(frame))
				keys = keymap.get(frame);
			if (Key.getKey().contains(Key.KeyCode.FAST))
				Game.replayCancel();
		} else {
			keys = fast ? tkeys : Key.getKey();
		}
		int xx = 0, yy = 0;
		if (keys.contains(Key.KeyCode.UP))
			yy -= moving;
		if (keys.contains(Key.KeyCode.LEFT))
			xx -= moving;
		if (keys.contains(Key.KeyCode.RIGHT))
			xx += moving;
		if (keys.contains(Key.KeyCode.DOWN))
			yy += moving;
		if (keys.contains(Key.KeyCode.SLOW)) {
			xx /= 2;
			yy /= 2;
		}
		if (!fast && keys.contains(Key.KeyCode.FAST) && fc <= -180 && (yy != 0 || xx != 0)) {
			SE.start("fast");
			xx *= 3;
			yy *= 3;
			fc = 6;
			tkeys = keys;
			fast = true;
		}
		if (xx != 0 && yy != 0) {
			xx *= S2;
			yy *= S2;
		}

		// iconの向きを設定
		if (xx > 0)
			istate = R;
		else if (xx < 0)
			istate = L;
		else
			istate = 0;

		x += xx;
		y += yy;
		if (fc-- < 0)
			fast = false;
		if ((getX() + iconW - 10) >= MyFrame.WIDTH)
			x = MyFrame.WIDTH - iconW + 10;
		else if (getX() + 10 <= 0)
			x = -10;
		if ((getY() + iconH - 10) >= MyFrame.AHEIGHT)
			y = MyFrame.AHEIGHT - iconH + 10;
		else if (getY() + 10 <= 0)
			y = -10;

		// リプレイ保存用
		if (!isReplay)
			Main.rp.setKey(frame, keys);
		frame++;
	}

	public boolean isSlow() {
		if (!isReplay)
			keys = Key.getKey();
		return keys.contains(Key.KeyCode.SLOW);
	}

	public void shot() {
		if (!isReplay)
			keys = Key.getKey();
		if (keys.contains(Key.KeyCode.SHOT)) {
			// 種類、色、最低時間、初期座標x、y、初期角度、速度、角度変化量、目印
			bl.add(new Bullet(Bullet.STAR, Bullet.SPE, 0, getMX(), getMY() - 40, -Math.PI / 2, 8, 0));
		}
	}

	@Override
	public void hit() {
		isLive = false;
		SE.singleStart("kane");
	}

	@Override
	public BufferedImage getImage() {
		if (fc <= -180) {
			if (istate == N)
				return icons[1][N];
			else if (istate == L)
				return icons[1][L];
			else
				return icons[1][R];
		} else {
			if (istate == N)
				return icons[0][N];
			else if (istate == L)
				return icons[0][L];
			else
				return icons[0][R];
		}
	}

	public boolean isReplay() {
		return isReplay;
	}

	public boolean isFast() {
		return fast;
	}

}
