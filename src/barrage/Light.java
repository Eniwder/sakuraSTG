package barrage;

import window.MyFrame;
import character.Boss;
import character.Player;
import etc.SE;

public class Light extends AbstractBarrage{
	// 種類、色、最低時間、初期座標x、y、初期角度、速度、目印
	ResizableBullet sun;
	int sx,sy;
	boolean hpSeted;

	@Override
	public void move() {

		if(frame==60){
			SE.start("fire");
			sx=boss.getMX();
			sy=boss.getMY();
			sun  = new ResizableBullet(Bullet.GLOW,Bullet.RED, 0, sx,sy ,0,0,0,1);
			ls.add(sun);
		}

		if(frame>60){
			if(frame%60==0)evenSpread(sun.rate);
			if(frame%5==0 && sun.rate>15)ls.add(new Bullet(Bullet.SOVAL,Bullet.RED, 0, sx,sy,(rand.nextInt(220)-20)*DEG,3,0));
			if(frame%30==0 && sun.rate>80)ls.add(new ResizableBullet(Bullet.GLOW,Bullet.RED, 0,rand.nextInt(MyFrame.WIDTH),MyFrame.AHEIGHT,-90*DEG,1,0,10));
			else if(frame%60==0 && sun.rate>50)ls.add(new ResizableBullet(Bullet.GLOW,Bullet.RED, 0,rand.nextInt(MyFrame.WIDTH),MyFrame.AHEIGHT,-90*DEG,1,0,8));
			else if(frame%90==0 && sun.rate>30)ls.add(new ResizableBullet(Bullet.GLOW,Bullet.RED, 0,rand.nextInt(MyFrame.WIDTH),MyFrame.AHEIGHT,-90*DEG,1,0,6));
			if(frame%60==30 && (sun.rate==95 || sun.rate==65 || sun.rate==35)){
				SE.start("fire");
				int sp = MyFrame.WIDTH/40;
				int size = sun.rate==95?10:sun.rate==65?8:6;
				for(int i=0;i<=40;i++)ls.add(new ResizableBullet(Bullet.GLOW,Bullet.RED, 0,sp*i,MyFrame.AHEIGHT,-90*DEG,1,0,size));
			}
			else if(sun.rate==99 && !hpSeted){
				SE.start("bobo");
				boss.setHP(50);
				hpSeted=true;
			}
		}

		if(frame%30==0 && sun.rate<30){
			sun.setRate(sun.rate+1);
			if(sun.rate==15 || sun.rate==30)SE.start("fire");
		}else if(frame%60==0 && sun.rate<99){
			sun.setRate(sun.rate+1);
			if(sun.rate==50 || sun.rate==80)SE.start("fire");
		}
		frame++;
	}


	private void evenSpread(int r) {
		double space = 360/(double)r;
		double toSakura = getBtoP();
		for(int i=0;i<r;i++){
			ls.add(new Bullet(Bullet.SOVAL,Bullet.RED, 0, sx,sy,toSakura+(i*space)*DEG,3,0));
		}

	}



	@Override
	public void init(Boss boss, Player sakura) {
		super.init(boss, sakura);
		boss.clearMahojin();
		sun = new ResizableBullet(Bullet.GLOW,Bullet.RED, 0, boss.getMX(), boss.getMY(),0,0,0,5);
		hpSeted=false;
		boss.calcMove(boss.getMX(), boss.getMY()-120);
	}

}
