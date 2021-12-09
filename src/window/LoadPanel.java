package window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import menu.Card;
import barrage.Bullet;
import barrage.EffectBullet;
import barrage.ResizableBullet;
import barrage.Windy;
import character.Boss;
import character.Player;

public class LoadPanel extends JPanel {

	List<Bullet> ls = new LinkedList<>();
	int frame = 0;

	public static BufferedImage back;

	public LoadPanel() {
		try {
			back = ImageIO.read(new File("res/img/back/back.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 種類、色、最低時間、初期座標x、y、初期角度、速度、目印
		for (int i = 0; i < Bullet.B_KIND; i++) {
			for (int j = 0; j < Bullet.COLOR_KINDS; j++) {
				Bullet bl = new Bullet(i, j, 0, 0, 0, 0, 0, 0);
				ls.add(bl);
			}
		}
		for (int i = Bullet.B_KIND; i < Bullet.B_KIND + Bullet.B_SPE; i++) {
			Bullet bl = new Bullet(i, Bullet.SPE, 0, 0, 0, 0, 0, 0);
			ls.add(bl);
		}
		Bullet bl = new Bullet(Bullet.SAKURA, Bullet.SPE, 0, 0, 0, 0, 0, 0);
		ls.add(bl);
		bl = new EffectBullet(Bullet.STAR, Bullet.EF, 0, 0, 0, 0, 0);
		ls.add(bl);
		for (int j = 0; j < 100; j++) {
			ResizableBullet rblR = new ResizableBullet(Bullet.GLOW, Bullet.RED, 0, 0, 0, 0, 0, 0, j);
			ls.add(rblR);
		}
		for (int j = 20; j < 50; j++) {
			ResizableBullet rblG = new ResizableBullet(Bullet.GLOW, Bullet.GREEN, 0, 0, 0, 0, 0, 0, j);
			ls.add(rblG);
		}
		ResizableBullet rblB = new ResizableBullet(Bullet.GLOW, Bullet.BLUE, 0, 0, 0, 0, 0, 0, 50);
		ls.add(rblB);
		ResizableBullet rblY = new ResizableBullet(Bullet.GLOW, Bullet.YELLOW, 0, 0, 0, 0, 0, 0, 50);
		ls.add(rblY);

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		if (frame == 1) {
			for (Bullet bl : ls) {
				g2.drawImage(bl.getImage(),0,0, this);
			}
			Boss boss = new Boss(new Card("「風」ウィンディ", (new Windy()), 800,2));
			g2.drawImage(boss.getImage(),0, 0, this);
			g2.drawImage(boss.getMahojin(),0,0,0,0, this);
			Player player = new Player();
			g2.drawImage(player.getImage(),0, 0, this);
			g2.drawImage(player.hantei, 0, 0, this);
			g2.drawImage(back,0, 0, this);

		}

		// 黒背景
		g2.setBackground(Color.BLACK);
		g2.clearRect(0, 0, MyFrame.WIDTH, MyFrame.HEIGHT);

		g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 32));
		g2.setColor(Color.WHITE);
		g2.drawString("Now Loading...", 280, 300);
		frame++;
	}

}
