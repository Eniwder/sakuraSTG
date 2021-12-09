package barrage;

import java.util.LinkedList;
import java.util.List;

import character.Boss;
import character.Player;
import etc.SE;

public class Jump extends AbstractBarrage {

	private static final int DOLL = 0, BULLET = 1;
	private List<RefrectBullet> mokonas = new LinkedList<>();

	@Override
	public void move() {
		// 種類、色、最低時間、初期座標x、y、初期角度、速度、目印
		if (frame % 600 == 0) {
			SE.singleStart("tararan");
			RefrectBullet mokona = new RefrectBullet(Bullet.MOKONA, Bullet.SPE, 0, boss.getMX(), boss.getMY(), getBtoP(), 3, DOLL,RefrectBullet.ALL,Integer.MAX_VALUE);
			mokonas.add(mokona);
			ls.add(mokona);
		}
		if (frame%120 == 0)boss.randomMove(30);
		for (RefrectBullet rbl : mokonas) {
			rbl.angle+=DEG*3;
			if (rbl.getRefed()) {
				SE.start("ok");
				int col = (int) (rand.nextInt(Bullet.COLOR_KINDS));
				for (double i = Math.PI / 6, rad = Math.PI / 6; i < Math.PI * 2 + rad; i += rad) {
					ls.add(new Bullet(Bullet.MIDDLE, col, 0, rbl.getMX(), rbl.getMY(), i, 3.2, BULLET));
				}
				rbl.clearRefed();
			}
		}
		frame++;
	}

	@Override
	public void init(Boss boss, Player sakura) {
		super.init(boss, sakura);
		frame = -60;
	}

}
