package barrage;

import java.util.List;
import java.util.Random;

import window.MyFrame;

public class MovingBullet extends Bullet{


	private static final double MAX_Y=MyFrame.HEIGHT/3;
	private static final double MIN_X=30;
	private static final double MIN_Y=15;
	private static final double SPACE=60;
	private static final int DEFAULT_TIME = 60;

	private double dx,dy,v0x,v0y,ax,ay;
	private int tt,dt;
	private double tx,ty;
	private boolean isMoving=false;

	public MovingBullet(int kind, int col, int till, double x, double y, double base_angle, double speed, int state) {
		super(kind, col, till, x, y, base_angle, speed, state);
		tx=x;
		ty=y;
	}

	@Override
	public Boolean move(int px, int py, int pr, List<Bullet> remove){
		move();
		return super.move(px, py, pr, remove);
}

	public void move() {
		// 移動中でなければフワフワ動く
		if(isMoving){
			if(--tt>=0){
				int t=(dt-tt);
				x=tx-((v0x*t)-0.5*ax*t*t);
				y=ty-((v0y*t)-0.5*ay*t*t);
			}else{
				if(tt<=-5){
					ty=y;
					tx=x;
					isMoving=false;
				}
			}
		}
	}

	public void calcMove(int ddx,int ddy,int time){
		isMoving=true;
		dx=getMX()-ddx;
		dy=getMY()-ddy;
		v0x=2*dx/time;
		v0y=2*dy/time;
		ax=2*dx/(time*time);
		ay=2*dy/(time*time);
		tx=x;
		ty=y;
		tt=dt=time;
	}

	public void calcMove(int dx,int dy){
		calcMove(dx,dy,DEFAULT_TIME);
	}

	public void randomMove(Random rand,int range){
		int dx,dy;

		do{
			dx=(int)(rand.nextDouble()*range*2)-range;
			dx+=(dx>0?MIN_X:-MIN_X)+x;
		}while((dx-SPACE)<=0 || (dx+W[kind]+SPACE)>=(MyFrame.WIDTH));

		do{
			dy=(int)(rand.nextDouble()*range)-range/2;
			dy+=(dy>0?MIN_Y:-MIN_Y)+ty;
		}while((dy-SPACE)<=0 || dy>=MAX_Y);

		calcMove(dx,dy);
	}



}
