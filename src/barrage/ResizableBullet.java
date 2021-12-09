package barrage;

import java.awt.Image;
import java.util.List;

import window.MyFrame;

public class ResizableBullet extends Bullet {

	double beW = W[kind], beH = H[kind];
	public double rw, rh;
	public int rate;
	protected static Image[][] resizedImage = new Image[Bullet.COLOR_KINDS][100];


	public ResizableBullet(int kind, int col, int till, double x, double y, double base_angle, double speed, int state,
			int rate) {
		super(kind, col, till, x, y, base_angle, speed, state);
		setRate(rate);
	}

	public ResizableBullet(int kind, int col, int till, double x, double y, double base_angle, double speed, int state) {
		super(kind, col, till, x, y, base_angle, speed, state);
	}

	// 0.0~1.0まで!
	public void setRate(int rate) {
		this.rate = rate;
		rw = W[kind] * (double)rate/100;
		rh = H[kind] * (double)rate/100;
		x += (beW - rw) / 2;
		y += (beH - rh) / 2;
		beW = rw;
		beH = rh;
	}

	@Override
	public final Image getImage() {
		return resizedImage[col][rate];
	}

	@Override
	protected final boolean inside() {
		int r = (int)rw;
		if (x - r > MyFrame.WIDTH || (x + r) <= 0 || y - r > MyFrame.AHEIGHT || (y + r) <= 0)
			return false;
		else
			return true;
	}

	@Override
	boolean isHit(int px, int py, int pr, List<Bullet> remove) {
		int x = px - getMX(), y = py - getMY(), r = (int) (R[kind] *(double)rate/100) + pr;
		if ((r * r) > (x * x + y * y)) {
			if (!(pr == sakura.r && sakura.isFast())) {
				if (remove != null)
					remove.add(this);
				return true;
			}
		}
		return false;
	}

	@Override
	public final int getMX() {
		return (int) (x + rw / 2);
	}

	@Override
	public final int getMY() {
		return (int) (y + rh / 2);
	}

	public static void init(int col,int min,int max){
		for(int i=min;i<max;i++){
			resizedImage[col][i]=icon[Bullet.GLOW][col].getScaledInstance((int)((double)(i+1)/100*W[Bullet.GLOW]), (int)((double)(i+1)/100*H[Bullet.GLOW]), Image.SCALE_SMOOTH);
		}
	}

}
