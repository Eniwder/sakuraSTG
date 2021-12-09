package barrage;

import window.MyFrame;
import character.Boss;
import character.Player;
import etc.SE;

public class Mirror extends AbstractBarrage {

	private static int MIRROR = -1;
	private static int MIDDLE = MyFrame.WIDTH / 2;
	boolean isMirrored;

	Bullet mirror;

	@Override
	public void move() {
		if (frame == 20) {
			ls.add(new Bullet(Bullet.BIG, Bullet.YELLOW, 0, boss.getMX(), boss.getMY(), 90 * DEG, 2, MIRROR));
		} else if (frame % 120 == 0) {
			boss.randomMove(30);
		} else if (frame % 120 == 60) {
			// 種類、色、最低時間、初期座標x、y、初期角度、速度、目印、反射モード、反射回数
			for (int i = 30; i <= 150; i += 12) {
				ls.add(new RefrectBullet(Bullet.MIDDLE, Bullet.BLUE, 0, boss.getMX(), boss.getMY(), (i + 180) * DEG, 3,
						MIRROR, RefrectBullet.L | RefrectBullet.U | RefrectBullet.R, Integer.MAX_VALUE));
			}
		}

		if (!isMirrored) {
			if (Math.abs(MIDDLE - sakura.getMX()) > 10) {
				mirror.x = MIDDLE + (MIDDLE - sakura.getMX() - sakura.iconW / 2);
				mirror.y = sakura.y;
				ls.addFirst(mirror);
				isMirrored = true;
				SE.start("tararan");
			}
		} else {
			mirror.x = MIDDLE + (MIDDLE - sakura.getMX() - sakura.iconW / 2);
			mirror.y = sakura.y;
			int mx = mirror.getMX(), my = mirror.getMY(), mr = sakura.r;
			for (Bullet bl : ls) {
				if (bl.isHit(mx, my, mr, null) && !bl.equals(mirror))
					sakura.hit();
			}
		}

		frame++;

	}

	@Override
	public void init(Boss boss, Player sakura) {
		super.init(boss, sakura);
		mirror = new Bullet(Bullet.SAKURA, Bullet.SPE, Integer.MAX_VALUE, 0, 0, -90 * DEG, 0, MIRROR);
		isMirrored = false;
		frame = 1;
	}

}
