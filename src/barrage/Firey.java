package barrage;

import java.util.LinkedList;
import java.util.List;

import window.MyFrame;
import character.Boss;
import character.Player;
import etc.SE;

public class Firey extends AbstractBarrage {

	private static int R = 350,NR=200;

	// 種類、色、最低時間、初期座標x、y、初期角度、速度、目印
	int tx, ty,ntx,nty;
	int rw, rh;
	List<Bullet> circles = new LinkedList<>();
	List<Bullet> remCircles = new LinkedList<>();
	int span;
	@Override
	public void move() {
		// 輪作成
		if (frame == span) {
			if (frame == 0) {
				tx = sakura.getMX();
				ty = sakura.getMY();
			} else {
				do {
					double rad = rand.nextInt(360) * DEG;
					ntx = tx + (int) (NR * Math.cos(rad));
					nty = ty + (int) (NR * Math.sin(rad));
				} while (!( (50<ntx) && (ntx<MyFrame.WIDTH-50) && (50<nty) && (nty<MyFrame.AHEIGHT-50) ) );
				tx=ntx;
				ty=nty;
			}
			span+=250-frame/100;
			SE.start("fire");
			for (int i = 0; i < 360; i += 6) {
				Bullet circleBullet = new ResizableBullet(Bullet.GLOW, Bullet.RED, 600, tx+(R-frame/100)*Math.cos(i*DEG), ty+(R-frame/100)*Math.sin(i*DEG), tx*1000+ty, 0, i, 8);
				ls.add(circleBullet);
				circles.add(circleBullet);
			}
		}else{
			// 収束
			for (Bullet bl : circles) {
				int count = bl.count;
				if (count >= R - 20) {
					SE.singleStart("bobo");
					ls.add(new ResizableBullet(Bullet.GLOW, Bullet.RED, 0, bl.getMX(), bl.getMY(), rand.nextInt(360)*DEG, 1+rand.nextDouble(), 0, 6));
					bl.x=-1000;
					bl.till=0;
					remCircles.add(bl);
				} else {
					int tx = ((int) (bl.base_angle)) / 1000;
					int ty = ((int) (bl.base_angle)) % 1000;
					bl.x = tx + (R - (count+(frame/100))) * Math.cos((bl.state + count * 2) * DEG) - rw;
					bl.y = ty + (R - (count+(frame/100))) * Math.sin((bl.state + count * 2) * DEG) - rh;
					if(frame%5==0 && count<60)ls.add(new Bullet(Bullet.SMALL, Bullet.RED, 180, bl.getMX(), bl.getMY(), bl.state*DEG,3, 0));
				}
			}
			for (Bullet bl : remCircles)circles.remove(bl);
		}
		boss.autoHit();

		frame++;
	}

	@Override
	public void init(Boss boss, Player sakura) {
		super.init(boss, sakura);
		ResizableBullet rbl = new ResizableBullet(Bullet.GLOW, Bullet.RED, 600, 0, 0, 0, 0, 0, 10);
		this.rw = (int) rbl.rw / 2;
		this.rh = (int) rbl.rh / 2;
		circles.clear();
		span=0;
		boss.warp(-1000, -1000);
	}

}
