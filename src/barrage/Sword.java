package barrage;

import java.util.ArrayList;
import java.util.List;

import window.MyFrame;
import character.Boss;
import character.Player;
import etc.SE;

//種類、色、最低時間、初期座標x、y、初期角度、速度、目印
public class Sword extends AbstractBarrage {

	private static final int SW = 0, BIG = 1, MID = 2, SM = 3;

	double tpr;
	Bullet sword;
	boolean isRotation = false, big = false, isStuck = false;
	int stc = 10;

	List<Bullet> cutted = new ArrayList<>();

	@Override
	public void move() {

		if (frame % 180 == 0) {
			boss.randomMove(30);
					ls.add(new Bullet(Bullet.BIG, Bullet.RED,60,sakura.getMX(),-50, DEG * 90,4,BIG));
		}

		if (isStuck || frame == 90) {
			tpr = Math.atan2(sakura.getMY() - sword.getMY(), sakura.getMX() - sword.getMX());
			big = tpr < sword.base_angle ? true : false;
			isRotation = true;
			isStuck = false;
			sword.speed = 0;
			stc = 10;
		}

		if (isRotation) {
			if (big && tpr <= sword.base_angle) {
				sword.base_angle -= DEG * 3;
			} else if (!big && tpr >= sword.base_angle) {
				sword.base_angle += DEG * 3;
			} else {
				SE.start("sword");
				sword.base_angle=tpr;
				isRotation = false;
				sword.speed = 8;
			}
		}

		// 大玉が落ちてくる
		if (frame % (22 + (boss.getHp() / 100)) == 0) {
				ls.add(new Bullet(Bullet.BIG, rand.nextInt(Bullet.COLOR_KINDS),60,rand.nextInt(MyFrame.WIDTH-60)+30,-50, DEG * 90,4,BIG));
		}

		// 剣と弾がぶつかったら
		cutted.clear();
		for (Bullet bl : ls) {
			double x = (bl.getMX() - sword.getMX()), y = (bl.getMY() - sword.getMY()), r = (Bullet.R[bl.kind]);
			if (!bl.equals(sword))if ((r * r) > (x * x + y * y) && bl.count > 30){
				SE.singleStart("swordf");
				cutted.add(bl);
			}
			if (bl.state == SM)bl.angle += DEG * 3;
		}

		cut();

		// 壁に刺さったら
		if (!isRotation && (sword.getMX() + Bullet.H[Bullet.SWORD] / 2 * Math.cos(sword.base_angle) <= -3
				|| sword.getMY() + Bullet.H[Bullet.SWORD] / 2 * Math.sin(sword.base_angle) <= 0
				|| MyFrame.WIDTH <= (sword.getMX() + Bullet.H[Bullet.SWORD]/2 * Math.cos(sword.base_angle))
				|| MyFrame.AHEIGHT <= (sword.getMY() + Bullet.H[Bullet.SWORD]/2 * Math.sin(sword.base_angle)))) {
			SE.start("swordf");
			sword.speed = 0;
			if (--stc < 0) {
				sword.speed = -10;
				isStuck = true;
			}
		}

		frame++;

	}

	private void cut() {
		for (Bullet bl : cutted) {
			if (!(bl.state == SM)) {
				if (bl.state == BIG) {
					for (double i = Math.PI / 6, rad = Math.PI / 6; i < Math.PI * 2 + rad; i += rad) {
						ls.add(new Bullet(Bullet.MIDDLE, bl.col, 30, bl.getMX(), bl.getMY(), i, 2, MID));
					}
				} else {
					for (double i = Math.PI / 6, rad = Math.PI / 6; i < Math.PI * 2 + rad; i += rad) {
						ls.add(new Bullet(Bullet.SMALL, bl.col, 30, bl.getMX(), bl.getMY(), i, 1, SM));
					}
				}
			}
			ls.remove(bl);
		}
	}

	@Override
	public void init(Boss boss, Player sakura) {
		super.init(boss, sakura);
		stc = 10;
		isRotation = false;
		big = false;
		isStuck = false;
		sword = new Bullet(Bullet.SWORD, Bullet.SPE, Integer.MAX_VALUE, boss.getMX(), boss.getMY(), DEG * 90, 0, SW);
		ls.add(sword);
	}

}
