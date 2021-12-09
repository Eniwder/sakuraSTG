package barrage;

import window.MyFrame;
import character.Boss;
import character.Player;
import etc.SE;

public class Earthy extends AbstractBarrage {

	private static int L=3,U=0,R=1,D=2;
	private static int TATE_W=30,TATE_H=600,YOKO_W=15,YOKO_H=800;

	int d;
	int span,hp;

	@Override
	public void move() {

		if(frame%span==60){
			boss.calcMove(sakura.getMX(), boss.getMY());
			SE.start("fire");
			makeCrag(d++%4);
			if(hp-boss.getHp()>100){
				hp=boss.getHp();
				span-=35;
				frame=90;
			}
		}

		frame++;
	}

	@Override
	public void init(Boss boss, Player sakura) {
		super.init(boss, sakura);
		d=U;
		span=300;
		hp=boss.getHp();
		boss.calcMove(boss.getMX(), boss.getMY()-150);
	}


	public void makeCrag(int direct){
		// 種類、色、最低時間、初期座標x、y、初期角度、速度、目印
		int mid;
		if(direct==L){
			mid = sakura.getMY();
			for(double i=0;i<YOKO_H;i+=15){
				for(int j=0;j<YOKO_W+i/5;j+=15){
					ls.add(new Bullet(Bullet.SOVAL,Bullet.RED,300,MyFrame.WIDTH+i, mid+j,(180+rand.nextInt(j/4+5))*DEG,2-i/1000,0));
					ls.add(new Bullet(Bullet.SOVAL,Bullet.RED,300,MyFrame.WIDTH+i, mid-j,(180-rand.nextInt(j/4+5))*DEG,2-i/1000,0));
				}
			}
		}else if(direct==U){
			mid = sakura.getMX();
			for(double i=0;i<TATE_H;i+=15){
				for(int j=0;j<TATE_W+i/5;j+=15){
					ls.add(new Bullet(Bullet.SOVAL,Bullet.RED,300, mid+j,MyFrame.AHEIGHT+i,(-90-rand.nextInt(j/4+5))*DEG,2-i/1000,0));
					ls.add(new Bullet(Bullet.SOVAL,Bullet.RED,300, mid-j,MyFrame.AHEIGHT+i,(-90+rand.nextInt(j/4+5))*DEG,2-i/1000,0));
				}
			}
		}else if(direct==R){
			mid = sakura.getMY();
			for(double i=0;i<YOKO_H;i+=15){
				for(int j=0;j<YOKO_W+i/5;j+=15){
					ls.add(new Bullet(Bullet.SOVAL,Bullet.RED,300,-i, mid+j,-rand.nextInt(j/4+5)*DEG,2-i/1000,0));
					ls.add(new Bullet(Bullet.SOVAL,Bullet.RED,300,-i, mid-j,rand.nextInt(j/4+5)*DEG,2-i/1000,0));
				}
			}
		}else if(direct==D){
			mid = sakura.getMX();
			for(double i=0;i<TATE_H;i+=15){
				for(int j=0;j<TATE_W+i/5;j+=15){
					ls.add(new Bullet(Bullet.SOVAL,Bullet.RED,300, mid+j,-i,(90+rand.nextInt(j/4+5))*DEG,2-i/1000,0));
					ls.add(new Bullet(Bullet.SOVAL,Bullet.RED,300, mid-j,-i,(90-rand.nextInt(j/4+5))*DEG,2-i/1000,0));
				}
			}
		}

	}

}
