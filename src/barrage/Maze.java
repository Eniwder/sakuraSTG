package barrage;

import character.Boss;
import character.Player;
import etc.SE;

public class Maze extends AbstractBarrage {

	private static final int LR = 310, MR = 230, SR = 150;

	int ballPerTime = 12;
	int tx, ty;
	int hole, beHole;
	int step;
	int wait;
	int tf;

	// 種類、色、最低時間、初期座標x、y、初期角度、速度、目印
	@Override
	public void move() {

		if (sakura.isFast() && (frame - tf) > 60) {
			tf = frame;
			ballPerTime--;
			SE.start("tararan");
		}

		switch (step) {
		case 0:
			if (--wait == 0)
				step = 1;
			break;
		case 1:
			SE.start("sword");
			hole = rand.nextInt(180 / 5) * 5;
			for (int i = 0; i < 360; i += 5) {
				if (i != hole) {
					MovingBullet mb = new MovingBullet(Bullet.MIDDLE, Bullet.GREEN, 0, tx, ty, i * DEG, 0, 0);
					mb.calcMove(tx + (int) (Math.cos(i * DEG) * LR), ty + (int) (Math.sin(i * DEG) * LR));
					ls.add(mb);
				}
			}
			wait += Math.abs(90 - hole) / 6 + 10;
			beHole = hole;
			step = 2;
			break;

		case 2:
			hole = rand.nextInt(180 / 6) * 6;
			for (double i = 0; i < 360; i += 6) {
				if (i != hole) {
					MovingBullet mb = new MovingBullet(Bullet.MIDDLE, Bullet.GREEN, 0, tx, ty, i * DEG, 0, 0);
					mb.calcMove(tx + (int) (Math.cos(i * DEG) * MR), ty + (int) (Math.sin(i * DEG) * MR));
					ls.add(mb);
				}
			}
			wait += Math.abs(beHole - hole) / 6;
			beHole = hole;
			step = 3;
			break;

		case 3:
			hole = rand.nextInt(180 / 10) * 10;
			for (int i = 0; i < 360; i += 10) {
				if (i != hole) {
					MovingBullet mb = new MovingBullet(Bullet.MIDDLE, Bullet.GREEN, 0, tx, ty, i * DEG, 0, 0);
					mb.calcMove(tx + (int) (Math.cos(i * DEG) * SR), ty + (int) (Math.sin(i * DEG) * SR));
					ls.add(mb);
				}
			}
			wait += Math.abs(beHole - hole) / 10;
			wait *= ballPerTime;
			step = 4;
			break;

		case 4:
			if (--wait == 0) {
				double rr = (-100 + rand.nextInt(10)) * DEG;
				for (Bullet b : ls) {
					if (b.state == 0) {
						b.speed = 8;
						b.base_angle += -rr;
					}
				}
			} else if (wait < 0) {
				step = 0;
				wait = 120;
			}
		}

		frame++;

	}

	@Override
	public void init(Boss boss, Player sakura) {
		super.init(boss, sakura);
		frame = 0;
		ty = boss.getMY();
		tx = boss.getMX();
		step = 0;
		wait = 1;
		ballPerTime = 12;
		tf = -60;
	}
}
