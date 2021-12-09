package barrage;

import java.util.LinkedList;
import java.util.List;

import window.MyFrame;
import character.Boss;
import character.Player;
import etc.SE;

public class Shadow extends AbstractBarrage{


	private static final int U=0,R=1,D=2,L=3;
	int bsize;
	int x0,y0,x1,y1;
	boolean atack=false;
	int color;
	int count;
	List<ResizableMovingBullet> lights = new LinkedList<>();
	int sx,sy;
	Bullet shadow;

	//種類、色、最低時間、初期座標x、y、初期角度、速度、目印
	@Override
	public void move() {

		if(frame%300==100){
			SE.start("tararan");
			atack=true;
			count=0;
			color = rand.nextInt(4);
			Bullet b = lights.get(color);
			double rad=Math.atan2(sakura.getMY()-b.getMY(),sakura.getMX()-b.getMX());
			sx=(int)(sakura.getMX()+100*Math.cos(rad));
			sy=(int)(sakura.getMY()+100*Math.sin(rad));
			shadow = new Bullet(Bullet.YUE,Bullet.SHADOW,0,sx,sy,-90*DEG,0,0);
			ls.add(shadow);
			// 光源を隠す;
			for(ResizableMovingBullet mb:lights){
				mb.speed=0;
				if(mb.col!=color){
					switch(mb.state){
					case U:
						mb.x-=bsize;
						break;
					case R:
						mb.y-=bsize;
						break;
					case D:
						mb.x+=bsize;
						break;
					case L:
						mb.y+=bsize;
					}
				}
			}
		}else if(frame%300==0){
			if(ls.contains(shadow))ls.remove(shadow);
			for(ResizableMovingBullet mb:lights){
				mb.speed=20;
				if(mb.col!=color){
					switch(mb.state){
					case U:
						mb.x+=bsize;
						break;
					case R:
						mb.y+=bsize;
						break;
					case D:
						mb.x-=bsize;
						break;
					case L:
						mb.y-=bsize;
					}
				}
			}
			atack=false;
		}

		if(atack){
			if(++count>=18){
				switch(color){
				case Bullet.BLUE:
					wave(sx,sy,count);
					break;
				case Bullet.RED:
					target(sx,sy,count);
					break;
				case Bullet.YELLOW:
					spread(sx,sy,count);
					break;
				case Bullet.GREEN:
					cross(sx,sy,count);
					break;
				}
			}

		}
		if(!atack){
			for(ResizableMovingBullet mb:lights){
				if(mb.state==R && mb.getMX()>MyFrame.WIDTH){
					mb.x=MyFrame.WIDTH-bsize/2;
					mb.base_angle=90*DEG;
					mb.state=D;
				}else if(mb.state==D && mb.getMY()>MyFrame.AHEIGHT){
					mb.y=MyFrame.AHEIGHT-bsize/2;
					mb.base_angle=180*DEG;
					mb.state=L;
				}else if(mb.state==L && mb.getMX()<0){
					mb.x=-bsize/2;
					mb.base_angle=270*DEG;
					mb.state=U;
				}else if(mb.state==U && mb.getMY()<0){
					mb.y=-bsize/2;
					mb.base_angle=0;
					mb.state=R;
				}
			}
		}
		frame++;
	}

	// 青
	private void wave(int x, int y, int count) {
		if(count<100){
			if(count%4==0){
				SE.start("shot");
				for(int i=0;i<12;i++){
					ls.add(new Bullet(Bullet.SOVAL,Bullet.BLUE,100,x,y,(i*30+count)*DEG,3.5,5));
				}
			}
		}
	}

	// 緑
	private void cross(int x, int y,int count) {
		if(count<100){
			if(count%8==0){
				SE.start("shot");
				for(int i=0;i<15;i++){
					ls.add(new Bullet(Bullet.SOVAL,Bullet.GREEN,0,x,y,i*24*DEG,3,3));
				}
			}else if(count%8==4){
				SE.start("shot");
				for(int i=0;i<15;i++){
					ls.add(new Bullet(Bullet.SOVAL,Bullet.GREEN,0,x,y,i*24*DEG,3,4));
				}
			}
		}
		for(Bullet b:ls){
			if(b.state==3 && b.count<100)b.base_angle+=1*DEG;
			else if(b.state==4 && b.count<100)b.base_angle-=1*DEG;
		}
	}

	// 赤
	private void target(int x, int y, int count) {
		if(count<150){
			if(count%2==0){
				double rad=Math.atan2(sakura.getMY()-sy,sakura.getMX()-sx);
				SE.start("shot");
				for(int i=0;i<6;i++){
					ls.add(new Bullet(Bullet.SMALL,Bullet.RED,0,x,y,rad+i*60*DEG,5,0));
				}
			}
		}
	}	// 黄
	private void spread(int x,int y,int count) {
		if(count<120){
			if(count%4==0){
				SE.start("shot");
				for(int i=0;i<3;i++){
					ls.add(new Bullet(Bullet.SMALL,Bullet.YELLOW,0,x,y,rand.nextInt(360)*DEG,rand.nextInt(3)+2,0));
					ls.add(new Bullet(Bullet.SOVAL,Bullet.YELLOW,0,x,y,rand.nextInt(360)*DEG,rand.nextInt(3)+2,0));
				}
			}
		}
	}






	@Override
	public void init(Boss boss, Player sakura) {
		super.init(boss, sakura);
		lights.clear();
		lights.add(new ResizableMovingBullet(Bullet.GLOW, Bullet.BLUE,Integer.MAX_VALUE,boss.getMX()-100,boss.getMY()-100,0,0,R,50));
		lights.add(new ResizableMovingBullet(Bullet.GLOW, Bullet.RED,Integer.MAX_VALUE,boss.getMX()+100,boss.getMY()-100,90*DEG,0,D,50));
		lights.add(new ResizableMovingBullet(Bullet.GLOW, Bullet.YELLOW,Integer.MAX_VALUE,boss.getMX()-100,boss.getMY()+100,270*DEG,0,U,50));
		lights.add(new ResizableMovingBullet(Bullet.GLOW, Bullet.GREEN,Integer.MAX_VALUE,boss.getMX()+100,boss.getMY()+100,180*DEG,0,L,50));
		for(ResizableMovingBullet mb:lights)ls.add(mb);
		bsize=(int)(Bullet.W[Bullet.GLOW]*0.5);
		x0=y0=0;
		x1=MyFrame.WIDTH;
		y1=MyFrame.AHEIGHT;
		atack=false;
		count=0;
		lights.get(0).calcMove(x0,y0);
		lights.get(1).calcMove(x1,y0);
		lights.get(2).calcMove(x0,y1);
		lights.get(3).calcMove(x1,y1);
		for(ResizableMovingBullet mb:lights)mb.speed=20;
		boss.calcMove(boss.getMX(),boss.getMY()-80);
	}
}
