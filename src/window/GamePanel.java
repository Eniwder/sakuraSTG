package window;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JPanel;

import menu.AfterGame;
import menu.Game;
import menu.Menu;
import menu.MenuList;
import barrage.Bullet;
import character.Boss;
import character.Player;

// パネルの設定
public class GamePanel extends JPanel {
	private static final float FADE_COUNT = 60;
	private static final int FONT_SIZE = 32;

	Player sakura;
	Boss boss;
	List<Bullet> bbl;
	List<Bullet> pbl;
	List<Bullet> prem;
	int frame = 0;
	int hw, hh, mh, mw, bw;
	int bossHp;
	BufferedImage ef;

	float count = 0;
	private AfterGame ag;
	private BufferedImage back;

	public GamePanel(Game game, AfterGame ag) {
		sakura = game.sakura;
		boss = game.boss;
		bbl = boss.getBullets();
		pbl = sakura.getBullets();
		prem = sakura.getRemoveBullets();
		hw = sakura.hantei.getWidth() / 2;
		hh = sakura.hantei.getHeight() / 2;
		mh = boss.getMahojin().getHeight() / 2;
		mw = boss.getMahojin().getWidth() / 2;
		bossHp = boss.getHp();
		this.ag = ag;
		ef = Bullet.getSakuraShot();
		back = LoadPanel.back;
		bw = back.getWidth();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		frame++;
		AffineTransform af = new AffineTransform();
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		// 背景
		g2.drawImage(back, -(frame * 2 % bw), 0, this);
		g2.drawImage(back, -(frame * 2 % bw) + bw, 0, this);

		// FPSを描画
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		g2.drawString(Game.getFPS(), MyFrame.WIDTH - 25, MyFrame.AHEIGHT - 10);

		// 残り時間を描画
		g2.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
		g2.drawString(Game.getTime(), MyFrame.WIDTH - 50, 50);

		if (frame % 2 == 0 && sakura.isLive) {
			if (frame % 180 < 90) {
				mw += 3;
				mh += 3;
			} else {
				mw -= 3;
				mh -= 3;
			}
		}

		// ボスと魔法陣
		if (!boss.isLive && sakura.isLive) {
			mw += 15;
			mh += 15;
			af.setToRotation((((double) frame * 5 % 360) * Math.PI / 180), boss.getMX(), boss.getMY());
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1 - (count / (FADE_COUNT * 2 / 3))));
			g2.setTransform(af);
			g2.drawImage(boss.getMahojin(), boss.getMX() - mw / 2, boss.getMY() - mh / 2, mw, mh, this);

			g2.setTransform(new AffineTransform());
			g2.drawImage(boss.getImage(), boss.getX(), boss.getY(), this);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
			// ボス死亡後処理
		} else {
			if (sakura.isLive)
				af.setToRotation((((double) frame * 2 % 360) * Math.PI / 180), boss.getMX(), boss.getMY());
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3F));

			g2.setTransform(af);
			g2.drawImage(boss.getMahojin(), boss.getMX() - mw / 2, boss.getMY() - mh / 2, mw, mh, this);
			g2.setTransform(new AffineTransform());

			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
			g2.drawImage(boss.getImage(), boss.getX(), boss.getY(), this);
		}

		// 自機
		if (sakura.isFast()) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.3));
			g2.drawImage(sakura.getImage(), sakura.getX(), sakura.getY(), this);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		} else {
			g2.drawImage(sakura.getImage(), sakura.getX(), sakura.getY(), this);
		}

		// 自機ショット
		g2.setTransform(new AffineTransform());
		for (Bullet bl : pbl)
			g2.drawImage(bl.getImage(), (int) bl.x, (int) bl.y, this);
		// 削除される弾ももう一度描画
		for (Bullet bl : prem)
			g2.drawImage(ef, (int) bl.x, (int) bl.y, this);

		// 自機判定
		if (sakura.isSlow() && sakura.isLive && boss.isLive) {
			af.setToRotation((((double) frame % 360) * Math.PI / 180), sakura.getMX(), sakura.getMY());
			g2.setTransform(af);
			g2.drawImage(sakura.hantei, sakura.getMX() - hw, sakura.getMY() - hh, this);
		}

		// ボスHP
		g2.setTransform(new AffineTransform());
		GradientPaint gp = new GradientPaint(10, 10, Color.RED, (MyFrame.WIDTH - 100), 22, Color.PINK);
		g2.setPaint(gp);
		g.fillRect(10, 10, (int) ((MyFrame.WIDTH - 20) * ((double) boss.getHp() / bossHp)), 12);

		// ボス弾幕
		for (Bullet bl : bbl) {
			af.setToRotation(bl.getRotate(), bl.getMX(), bl.getMY());
			g2.setTransform(af);
			g2.drawImage(bl.getImage(), (int) bl.x, (int) bl.y, this);
		}

		// 死亡後処理
		if (!sakura.isLive || !boss.isLive) {
			g2.setTransform(new AffineTransform());
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

			// 黒フェード
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (count / FADE_COUNT)));
			g2.setColor(Color.BLACK);
			g2.fillRect(0, 0, 1000, 1000);

			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

			// プレイ後のメニューを表示
			MenuList<Menu> ml = ag.getMenuList();
			g2.setFont(new Font(Font.DIALOG, Font.BOLD, FONT_SIZE));
			g2.setColor(Color.WHITE);
			for (Menu menu : ml.getList()) {
				float alpha = menu.equals(ml.get()) ? 1F : 0.5F;
				AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
				g2.setComposite(composite);
				g2.drawString(menu.name, menu.x, menu.y);
			}
			if (count < FADE_COUNT * 2 / 3)
				count++;
		}
	}
}