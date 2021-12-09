package barrage;

import character.Boss;
import character.Player;
import etc.SE;

public class Flower extends AbstractBarrage{


	private static final double EN = Math.PI*2;
	private static final int X_RANGE = (int)((800-80)/(2*Math.PI));
	private static final int Y_RANGE = 40;
	private static final int S=0,E=50,ST=E+1;
	private static final int SP=4;
	private static final int H=12;
	private static final float W=800,BLOCK=100,ONE_BLOCK=W/BLOCK;


	double x;
	int[] block=new int[(int)BLOCK];


	@Override
	public void move() {
		if(frame==10){
			boss.setFloat(false);
			boss.calcMove((int)(EN*X_RANGE-(x%EN)*X_RANGE)+boss.iconW/2,(int)Math.sin(x)*Y_RANGE+80+boss.iconH/2);
		}else if(frame>10+60){
		// ボスの動き
		if(x%(Math.PI*4)<=EN){
			boss.x=(x%EN)*X_RANGE;
			boss.y=Math.sin(x)*Y_RANGE+80;
		}else{
			boss.x=EN*X_RANGE-(x%EN)*X_RANGE;
			boss.y=Math.sin(x)*Y_RANGE+80;
		}
		x+=DEG;
		}



		// 段幕部分
		if(frame>60 && frame%30==0){
			SE.start("shot");
			// 種類、色、最低時間、初期座標x、y、初期角度、速度、目印
			final int r = rand.nextInt(10);
			// 上
			ls.add(new Bullet(Bullet.SOVAL,Bullet.RED,100,boss.getMX()-10-r,boss.getMY()-50,DEG*180+rand.nextInt(90)*DEG,SP,S));
			ls.add(new Bullet(Bullet.SOVAL,Bullet.RED,100,boss.getMX()+10+r,boss.getMY()-50,-DEG*180-rand.nextInt(90)*DEG,SP,S));

			ls.add(new Bullet(Bullet.SOVAL,Bullet.RED,100,boss.getMX()-20-r,boss.getMY()-100,DEG*180+rand.nextInt(90)*DEG,SP,S));
			ls.add(new Bullet(Bullet.SOVAL,Bullet.RED,100,boss.getMX()+20+r,boss.getMY()+-100,-DEG*180-rand.nextInt(90)*DEG,SP,S));

			// 真ん中
			ls.add(new Bullet(Bullet.SOVAL,Bullet.RED,100,boss.getMX()-80-r,boss.getMY(),DEG*180+rand.nextInt(90)*DEG,SP,S));
			ls.add(new Bullet(Bullet.SOVAL,Bullet.RED,100,boss.getMX()+80+r,boss.getMY(),-DEG*180-rand.nextInt(90)*DEG,SP,S));

			ls.add(new Bullet(Bullet.SOVAL,Bullet.RED,100,boss.getMX()-160-r,boss.getMY(),DEG*180+rand.nextInt(90)*DEG,SP,S));
			ls.add(new Bullet(Bullet.SOVAL,Bullet.RED,100,boss.getMX()+160+r,boss.getMY(),-DEG*180-rand.nextInt(90)*DEG,SP,S));
			// 下
			ls.add(new Bullet(Bullet.SOVAL,Bullet.RED,100,boss.getMX()-10-r,boss.getMY()+50,DEG*180+rand.nextInt(90)*DEG,SP,S));
			ls.add(new Bullet(Bullet.SOVAL,Bullet.RED,100,boss.getMX()+10+r,boss.getMY()+50,-DEG*180-rand.nextInt(90)*DEG,SP,S));

			ls.add(new Bullet(Bullet.SOVAL,Bullet.RED,100,boss.getMX()-20-r,boss.getMY()+100,DEG*180+rand.nextInt(90)*DEG,SP,S));
			ls.add(new Bullet(Bullet.SOVAL,Bullet.RED,100,boss.getMX()+20+r,boss.getMY()+100,-DEG*180-rand.nextInt(90)*DEG,SP,S));

		}

		for(Bullet bl:ls){
			if(bl.state<E && bl.state>=0){
				double t = DEG*90-bl.base_angle;
				bl.base_angle+=t/10;
				bl.speed-=0.05;
				bl.state++;
			}


			if(bl.x>0 && bl.x<W && bl.state!=ST && bl.y>=block[(int)(bl.x/ONE_BLOCK)]){
				block[(int)(bl.x/ONE_BLOCK)]-=H;
				bl.speed=0;
				bl.state=ST;
			}

		}


		frame++;
	}


	@Override
	public void init(Boss boss,Player sakura){
		super.init(boss,sakura);
		x=DEG*180;
		for(int i=0;i<BLOCK;i++)block[i]=560;
	}

}
