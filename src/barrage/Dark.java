package barrage;

import java.util.List;

import window.MyFrame;
import character.Boss;
import character.Player;
import etc.SE;

public class Dark extends AbstractBarrage {

	private static int DARK = 1,ARROW=2,STAR=3,ATK=4,ARROW_SPEED=5;
	Bullet dark;

	int atksrcX,atksrcY;

	@Override
	public void move() {
		// 種類、色、最低時間、初期座標x、y、初期角度、速度、目印
		switch (frame) {
		case 60:
			SE.singleStart("tararan");
			dark.x = -550;
			dark.y = -300;
			break;
		case 60 * 10:
			dark.speed = 0.8;
			break;
		case 60 * 15:
			dark.speed = 0;
			break;
		case 60 * 18:
			dark.speed = 0.8;
			changeDirect(225);
			break;
		case 60 * 21:
			changeDirect(135);
			break;
		case 60 * 26:
			changeDirect(225);
			break;
		case 60 * 31:
			changeDirect(180);
			break;
		case 60 * 34:
			changeDirect(315);
			break;
		case 60 * 41:
			dark.speed = 0;
			break;
		case 60 * 46:
			dark.speed = 1.2;
			changeDirect(90);
			break;
		case 60 * 51:
			changeDirect(0);
			break;
		case 60 * 52:
			changeDirect(90);
			break;
		case 60 * 53:
			changeDirect(270);
			break;
		case 60 * 54:
			changeDirect(0);
			break;
		case 60 * 58:
			changeDirect(270);
			break;
		case 60 * 64:
			changeDirect(135);
			break;
		case 60 * 69:
			dark.speed = 0;
			break;
		}

		if (frame<60*67 && frame % 10 == 0) {
			ls.addFirst(new Bullet(Bullet.SMALL,Bullet.YELLOW,60,rand.nextInt(MyFrame.WIDTH),0,90*DEG,1.5,STAR));
		}

		if (frame>=60*70 && frame<=60*80 && frame %60 == 0) {
			SE.start("sword");
			makeArrow();
		}

		if(frame==60*80){
			SE.start("tararan");
			boss.warp(MyFrame.WIDTH/2-boss.iconW/2, 80);
			atksrcX=boss.getMX();
			atksrcY=(int)(boss.y+boss.iconH+5);
			ls.addFirst(new ResizableBullet(Bullet.GLOW,Bullet.BLUE,0,atksrcX,atksrcY,0,0,0,20));
			dark.x=1000;
			dark.y=1000;
		}

		if(frame>60*82){
			SE.start("shot");
			ls.addFirst(new Bullet(Bullet.SOVAL,Bullet.BLUE,0,atksrcX,atksrcY,(30+rand.nextInt(121))*DEG,4+rand.nextDouble(),ATK));
		}

		for (Bullet bl : ls) {
			if (bl.state == STAR) {
				double r = (-1 + rand.nextInt(3)) * DEG;
				bl.base_angle += r;
				bl.angle += DEG - r;
			}
		}

		frame++;

	}

	private void makeArrow() {
		double r = rand.nextInt(360) * DEG;
		int x = (int)(sakura.getMX() + 300 * Math.cos(r));
		int y = (int)(sakura.getMY() +300 * Math.sin(r));
		double toP = Math.atan2(sakura.getMY()-y,sakura.getMX()-x);
		ls.addFirst(new Bullet(Bullet.SOVAL,Bullet.BLUE,120,(int)(x+16 * Math.cos(r)),(int)(y+15 * Math.sin(r)),toP,ARROW_SPEED,ARROW));
		ls.addFirst(new Bullet(Bullet.SOVAL,Bullet.BLUE,120,(int)(x+25 * Math.cos(r)),(int)(y+25 * Math.sin(r)),toP,ARROW_SPEED,ARROW));
		ls.addFirst(new Bullet(Bullet.SOVAL,Bullet.BLUE,120,(int)(x+35 * Math.cos(r)),(int)(y+35 * Math.sin(r)),toP,ARROW_SPEED,ARROW));
		ls.addFirst(new Bullet(Bullet.SOVAL,Bullet.BLUE,120,(int)(x+45 * Math.cos(r)),(int)(y+45 * Math.sin(r)),toP,ARROW_SPEED,ARROW));
		ls.addFirst(new Bullet(Bullet.SOVAL,Bullet.BLUE,120,x,y,toP,ARROW_SPEED,ARROW));
		Bullet kari;
		int x0 = (int)(sakura.getMX() + 350 * Math.cos(r));
		int y0 = (int)(sakura.getMY() +350 * Math.sin(r));
		int x2 = (int)(sakura.getMX() + 355 * Math.cos(r+0.5*DEG));
		int y2 = (int)(sakura.getMY() +355 * Math.sin(r+0.5*DEG));
		kari = new Bullet(Bullet.SOVAL,Bullet.BLUE,120,x2,y2,toP,ARROW_SPEED,ARROW);
		kari.angle = Math.atan2(y0-y2,x0-x2)-kari.base_angle;
		ls.addFirst(kari);
		x2 = (int)(sakura.getMX() + 355 * Math.cos(r-0.5*DEG));
		y2 = (int)(sakura.getMY() +355 * Math.sin(r-0.5*DEG));
		kari = new Bullet(Bullet.SOVAL,Bullet.BLUE,120,x2,y2,toP,ARROW_SPEED,ARROW);
		kari.angle = Math.atan2(y0-y2,x0-x2)-kari.base_angle;
		ls.addFirst(kari);
	}

	private void changeDirect(int deg) {
		dark.base_angle = deg * DEG;
		dark.angle = -deg * DEG;
	}

	@Override
	public void init(Boss boss, Player sakura) {
		super.init(boss, sakura);
		dark = new Bullet(Bullet.DARK, Bullet.SPE, -1, 5000, 5000, 0, 0, DARK) {
			@Override
			public Boolean move(int px, int py, int pr, List<Bullet> remove) {
				double xx, yy;
				count++;
				xx = Math.cos(base_angle) * speed;
				yy = Math.sin(base_angle) * speed;
				x += xx;
				y += yy;
				return false;
			}
		};
		ls.add(dark);
		boss.warp(-1000, -1000);
	}
}
