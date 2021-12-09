package barrage;

import window.MyFrame;

public class Glow extends AbstractBarrage{

	private static final int UP=0,DOWN=1;
	private static final int CHANGE=1,MAX=45,MIN=20;

	ResizableBullet rbl;



	@Override
	public void move() {
		// 種類、色、最低時間、初期座標x、y、初期角度、速度、目印

		// 弾生成
		if(frame%8==0){
			ls.add(new ResizableBullet(Bullet.GLOW,Bullet.GREEN, 60, rand.nextInt(MyFrame.WIDTH), -90, 90*DEG,4,UP,rand.nextInt(MAX-MIN)+MIN));
		}

		// 弾サイズ変化
		if(frame%4==0){
			for(Bullet bl : ls){
				if(bl instanceof ResizableBullet)	rbl = (ResizableBullet)bl;
				if(rbl.state==UP){
					if((rbl.rate+=CHANGE)>=MAX)rbl.state=DOWN;
					rbl.setRate(rbl.rate);
				}else{
					if((rbl.rate-=CHANGE)<=MIN)rbl.state=UP;
					rbl.setRate(rbl.rate);
				}
			}
		}

		if(frame%90==30)boss.randomMove(100);


		frame++;

	}




}
