package barrage;

import window.MyFrame;
import character.Boss;
import character.Player;
import etc.SE;

public class Erase extends AbstractBarrage {

	// 種類、色、最低時間、初期座標x、y、初期角度、速度、目印
	@Override
	public void move() {
		if(frame%360==0){
			SE.singleStart("tararan");
			sakura.resetDummyFromHantei();
			boss.warp(0,-1000);
		}else if(frame%360==180){
			sakura.setDummyToHantei();
			SE.singleStart("tararan");
			boss.warp(rand.nextInt(MyFrame.WIDTH-200)+100,rand.nextInt(100)+50);
		}
		if(frame%9==0){
			ls.add(new Bullet(Bullet.MIDDLE, Bullet.BLUE, 0,rand.nextInt(MyFrame.WIDTH),MyFrame.AHEIGHT,-90*DEG,6+rand.nextDouble(),0));
		}
		if(frame%5==0)for(Bullet bl : ls)bl.speed-=0.2;
		frame++;
	}

	@Override
	public void init(Boss boss, Player sakura) {
		super.init(boss, sakura);
		frame = 0;
		boss.warp(0,-1000);
	}

}
