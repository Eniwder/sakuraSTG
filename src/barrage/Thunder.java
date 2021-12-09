package barrage;

import window.MyFrame;
import character.Boss;
import character.Player;
import etc.SE;

public class Thunder extends AbstractBarrage{

	private static final int LOCAS= 0;
	private static final int CHIP = 1;
	private static final int ELEC = 2;


	boolean making;
	int count;
	double a;
	int next;
	Bullet th;

	@Override
	public void move() {
		int moveTime=60;
		if(frame==next){
			if(next<80)moveTime=(next-20);
			boss.calcMove(sakura.getMX(),100,moveTime);
		}else if(frame==next+moveTime+5){
			frame=0;
			making=true;
			next=(int)(60+a*boss.getHp());
			SE.start("sword");
			// 雷の元
			th=new ResizableBullet(Bullet.GLOW,Bullet.YELLOW,600,boss.getMX(),boss.getMY(),(30-rand.nextInt(60)+90)*DEG,7,LOCAS,10);
			ls.add(th);
			count=0;
		}

		if(frame%10==1){
			int x=boss.getMX()-sakura.getMX();
			int y=boss.getMY()-sakura.getMY();
			if(x*x+y*y<150*150){
				SE.start("shot");
				for(int i=0;i<12;i++){
					ls.add(new Bullet(Bullet.SOVAL,Bullet.YELLOW,0,boss.getMX()+Math.cos(i*30*DEG)*100,boss.getMY()+Math.sin(i*30*DEG)*100,rand.nextInt(360)*DEG,0.7,ELEC));
				}
			}
		}

		if(th!=null && th.getMY()>=MyFrame.AHEIGHT){
			SE.start("bobo");
			making=false;
			crash();
			th.till=0;
			th.y=-500;
		}else if(making && frame%2==0){
			ls.add(new Bullet(Bullet.SOVAL,Bullet.YELLOW,0,th.getMX(),th.getMY(),th.base_angle,0,LOCAS));
			if((++count)%7==0)th.base_angle=(30-rand.nextInt(60)+90)*DEG;
		}else{
			for(Bullet b:ls){
				if(b.count>60*10 && b.state==LOCAS){
					SE.start("shot");
					b.base_angle=rand.nextInt(360)*DEG;
					b.speed=0.5;
					b.state=ELEC;
					b.count=0;
				}else if(b.state==ELEC && b.count>80){
					b.x=-20;
				}
			}
		}

		frame++;
	}

	private void crash() {
//		SE.start("bobo");
		int x=th.getMX();
		for(int j=0;j<3;j++){
			for(int i=1;i<19;i++){
				double rad=(i*9-15+rand.nextInt(30)+180)*DEG;
				ls.add(new Bullet(Bullet.SOVAL,Bullet.RED,60,x,MyFrame.AHEIGHT,rad,3+i%2+rand.nextDouble(),CHIP));
			}
		}
	}

	@Override
	public void init(Boss boss, Player sakura) {
		super.init(boss, sakura);
		making=false;
		count=0;
		a=60.0/boss.getHp();
		next=80;
		th=null;
	}

}
