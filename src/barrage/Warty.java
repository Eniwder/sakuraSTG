package barrage;

import character.Boss;
import character.Player;
import etc.SE;

public class Warty extends AbstractBarrage {

	int frame = 0, hp;
	private static double RAD = Math.PI / 180;

	@Override
	public void move() {

		if (frame % (12 - ((hp - boss.getHp()) / 100)) == 0 && frame > 70) {
			// 種類、色、最低時間、初期座標x、y、初期角度、速度、目印
			SE.start("shot");
			ls.add(new Bullet(Bullet.SOVAL, Bullet.BLUE, 2000, boss.getMX(), boss.getMY() - 35, 0, 6, 0));
		}

		double r;
		for (Bullet bl : ls) {
			if (bl.state == 0) {
				r = RAD * 10;
				for (int deg = 45; deg <= 360; deg += 45) {
					for (double i = 1; i <= 10; i++) {
						if (bl.base_angle >= RAD * deg * i)
							r -= RAD / 8;
					}
				}
				if (bl.base_angle < RAD * 360 * 5)
					bl.base_angle += r;
				else
					bl.base_angle += r + rand.nextDouble() * RAD * 1.5;
			}
		}
		frame++;
	}

	@Override
	public void init(Boss boss, Player sakura) {
		super.init(boss, sakura);
		frame = 0;
		boss.calcMove(boss.getMX(), boss.getMY() - 50);
		hp = boss.getHp();
	}

}
