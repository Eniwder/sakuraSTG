package barrage;

import window.MyFrame;
import character.Boss;
import character.Player;
import etc.SE;

public class Windy extends AbstractBarrage {
	int state = 2;// 2:最初 1:威力プラス
	private static final int E = 1, W = 2, S = 4, N = 8;// 東西南北をビットで表現
	private int[] hori = { E, W };// ランダムで選択用(横)
	private int[] verti = { S, N };// ランダムで選択用(縦)
	private int MINH = 250;
	private static final int SPAN = 6 * 60, EXTEND_SPAN = 4 * 60;// 風の吹く時間と最大延長時間
	int direct = 0;// 方向を表すビット
	int blowTime = -100;// 風の吹く時間

	@Override
	public void move() {
		blowTime--;
		if (blowTime == 0 || blowTime == -101)
			SE.start("blow");
		if (state != 1 && boss.getHp() < 400) {
			state = 1;
			SE.start("tararan");
		}
		else if (blowTime < -60) {
			int bd = direct;
			do {
				direct = hori[rand.nextInt(hori.length)];
				direct += verti[rand.nextInt(verti.length)];
			} while (bd == direct);
			blowTime = rand.nextInt(EXTEND_SPAN) + SPAN;
		} else if (blowTime < 0) {
			if (blowTime % 3 == 0) {
				for (Bullet bl : ls) {
					bl.speed -= 1.0 / 60.0;
				}
			}
		} else {
			if (blowTime % 2 == 0) {
				int x = ((direct & W) != 0) ? -20 : MyFrame.WIDTH + 20;
				int y = rand.nextInt(MyFrame.AHEIGHT); // -100～(画面の高さ-100)の位置
				y += ((direct & N) != 0) ? -MINH : MINH;
				double rad;
				if ((direct & E) != 0 && (direct & S) != 0)
					rad = 5 * Math.PI / 4;
				else if ((direct & E) != 0 && (direct & N) != 0)
					rad = 3 * Math.PI / 4;
				else if ((direct & W) != 0 && (direct & S) != 0)
					rad = 7 * Math.PI / 4;
				else
					rad = Math.PI / 4;
				rad += Math.PI * (2 * rand.nextDouble() - 1) / 6;
				// 種類、色、最低時間、初期座標x、y、初期角度、速度、目印
				ls.add(new Bullet(Bullet.SOVAL, Bullet.GREEN, 1000, x, y, rad, 2 + (state % 2) * 0.5, 0));
			}
			if (blowTime % state == 0) {
				sakura.x += ((direct & W) != 0) ? 1 : -1;
				sakura.y += ((direct & N) != 0) ? 1 : -1;
			}
		}
	}

	@Override
	public void init(Boss boss, Player sakura) {
		super.init(boss, sakura);
		state = 2;
		direct = 0;
		blowTime = -100;
	}

}
