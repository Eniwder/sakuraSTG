package barrage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import window.MyFrame;
import character.Boss;
import character.Player;
import etc.SE;

public class Wood extends AbstractBarrage {
	int ts, tc;
	List<Integer> len;
	int count;

	@Override
	public void move() {

		switch (ts) {
		case 0:
			ls.add(new Bullet(Bullet.SOVAL, Bullet.BLUE, 0, bx, by, Math.PI / 2, 2, 0));
			count = 0;
			ts = 1;
			break;
		case 1:
			if (++count > 190) {
				ts = 2;
				SE.start("tararan");
			}
			break;
		case 2:
			maketree(bx, MyFrame.AHEIGHT, 0, Math.PI / 2);
			ts = 3;
		case 3:
			for (Bullet bl : ls) {
				if (bl.state == 0)
					bl.base_angle += 0.5 - rand.nextDouble();
			}
			tc = 0;
			ts = 4;
			break;
		case 4:
			if (++tc > 60) {
				for (Bullet bl : ls) {
					bl.speed += 0.02;
					if (bl.speed >= 3) {
						ts = 5;
						count = 0;
					}
				}
			}
			break;
		case 5:
			if (++count > 150) {
				len.add(len.get(len.size() - 1) * 2 / 3);
				ts = 0;
			}
		}
		frame++;
	}

	// 種類、色、最低時間、初期座標x、y、初期角度、速度、目印
	private void maketree(double bx, double by, int dep, double rad) {
		double r = 0.5 - rand.nextDouble();
		if (dep >= len.size()) {

			return;
		}
		for (int i = 0, end = len.get(dep); i < end; i++) {
			if (dep != 0)
				rad += r / end;
			ls.add(new Bullet(Bullet.SOVAL, Bullet.GREEN, 300, bx + Math.cos(rad) * i * 8, by - Math.sin(rad) * i * 8,
					rad, 0, 0));
		}
		if (dep == 0)
			maketree(bx + Math.cos(rad) * len.get(dep) * 8, by + Math.sin(rad) * -len.get(dep) * 8, dep + 1, rad);
		maketree(bx + Math.cos(rad) * len.get(dep) * 8, by + Math.sin(rad) * -len.get(dep) * 8, dep + 1, rad + Math.PI
				/ 4);
		maketree(bx + Math.cos(rad) * len.get(dep) * 8, by + Math.sin(rad) * -len.get(dep) * 8, dep + 1, rad - Math.PI
				/ 4);
	}

	@Override
	public void init(Boss boss, Player sakura) {
		super.init(boss, sakura);
		ts = tc = 0;
		len = new ArrayList<>(Arrays.asList(30, 20));
	}

}
