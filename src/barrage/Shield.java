package barrage;

import java.util.LinkedList;

import character.Boss;
import character.Player;

public class Shield extends AbstractBarrage {

	double rr = 0, yr = 0, br = 0, gr = 0;
	static final int R = 100, RR = 1, YR = 2, BR = 3, GR = 4;
	int ty;
	LinkedList<Bullet> rl = new LinkedList<>();
	LinkedList<Bullet> yl = new LinkedList<>();
	LinkedList<Bullet> bl = new LinkedList<>();
	LinkedList<Bullet> gl = new LinkedList<>();

	@Override
	public void move() {

		// 種類、色、最低時間、初期座標x、y、初期角度、速度、目印
		if (frame % 2 == 0) {
			Bullet ba = new Bullet(Bullet.SOVAL, Bullet.RED, 0, (int) (boss.getMX() + R * Math.cos(rr)), (int) (ty + R
					* Math.sin(rr)), rr * DEG, 0, RR);
			ls.add(ba);
			rl.add(ba);
			rr += 1;
			if (rr > 30)
				rl.poll().speed = 2;

			Bullet bc = new Bullet(Bullet.SOVAL, Bullet.BLUE, 0, (int) (boss.getMX() + R * Math.cos(br)), (int) (ty + R
					* Math.sin(br)), br * DEG, 0, BR);
			bl.add(bc);
			ls.add(bc);
			br += 2.5;
			if (rr > 30)
				bl.poll().speed = 2.5;
		} else {
			Bullet bb = new Bullet(Bullet.SOVAL, Bullet.YELLOW, 0, (int) (boss.getMX() + R * Math.cos(yr)),
					(int) (ty + R * Math.sin(yr)), yr * DEG, 0, YR);
			yl.add(bb);
			ls.add(bb);
			yr += 1.5;
			if (rr > 30)
				yl.poll().speed = 3;

			Bullet bd = new Bullet(Bullet.SOVAL, Bullet.GREEN, 0, (int) (boss.getMX() + R * Math.cos(gr)),
					(int) (ty + R * Math.sin(gr)), gr * DEG, 0, GR);
			gl.add(bd);
			ls.add(bd);
			gr += 3.5;
			if (rr > 30)
				gl.poll().speed = 3.5;
		}
		frame++;

	}

	@Override
	public void init(Boss boss, Player sakura) {
		super.init(boss, sakura);
		frame = 0;
		rr = 0;
		yr = 90;
		br = 180;
		gr = 270;
		rl.clear();
		yl.clear();
		bl.clear();
		gl.clear();
		ty = boss.getMY();
	}
}
