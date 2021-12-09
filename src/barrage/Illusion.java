package barrage;

import java.util.LinkedList;
import java.util.List;

import character.Boss;
import character.Player;
import etc.SE;

public class Illusion extends AbstractBarrage{


	private static final int MOVE_RANGE=100;


	int homeX,homeY;
	List<MovingBullet> yue = new LinkedList<>();
	int thp,gc,cc;
	boolean isGathering=false;



	@Override
	public void move() {


		if(frame%120==80 && !isGathering){
			boss.randomMove(MOVE_RANGE);
			rand.nextInt();
			for(MovingBullet mb:yue)mb.randomMove(rand, MOVE_RANGE);
		}

		if(frame%60==0 && frame>=80 && !isGathering){
			for(MovingBullet mb:yue){
				for(double i=Math.PI/12,rad=Math.PI/12;i<Math.PI*2+rad;i+=rad){
					ls.add(new Bullet(Bullet.SOVAL,cc,0,mb.getMX(),mb.getMY(),i,3,0));
				}
			}

		}

		if(thp>boss.getHp()+250){
			isGathering=true;
			SE.start("tararan");
			boss.calcMove(homeX,homeY);
			for(MovingBullet mb:yue)mb.calcMove(homeX,homeY);
			gc=60;
			cc++;
			thp=boss.getHp();
			frame=0;
		}

		if(isGathering){
			if(--gc<0){
				SE.start("sword");
				MovingBullet temp = new MovingBullet(Bullet.YUE, Bullet.SPE, Integer.MAX_VALUE, boss.getMX(), boss.getMY(),DEG*270, 0,0);
				yue.add(temp);
				ls.add(temp);
				isGathering=false;
			}
		}

		frame++;
	}


	@Override
	public void init(Boss boss, Player sakura) {
		super.init(boss, sakura);

		boss.setFloat(false);
		boss.clearMahojin();
		thp=boss.getHp();
		cc=0;
		homeX=boss.getMX();
		homeY=boss.getY()-50;

		yue.clear();
		isGathering=false;

		// 幻を生成
		yue.add(new MovingBullet(Bullet.YUE, Bullet.SPE, Integer.MAX_VALUE, boss.getMX(), boss.getMY(),DEG*270, 0,0));
		yue.add(new MovingBullet(Bullet.YUE, Bullet.SPE, Integer.MAX_VALUE, boss.getMX(), boss.getMY(),DEG*270, 0,0));
		for(MovingBullet mb : yue)ls.add(mb);

		boss.calcMove(homeX,homeY);
		for(MovingBullet mb:yue)mb.calcMove(homeX,homeY);

		}


	}



