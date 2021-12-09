package barrage;

import java.util.List;

import window.MyFrame;

public class RefrectBullet extends Bullet {

	public static int R = 1, U = 2, L = 4, D = 8,ALL=(R|U|L|D);
	private boolean isRefed = false;
	int mode, refcount;

	public RefrectBullet(int kind, int col, int till, double x, double y, double base_angle, double speed, int state,
			int mode, int refcount) {
		super(kind, col, till, x, y, base_angle, speed, state);
		this.mode = mode;
		this.refcount = refcount;
	}

	@Override
	public Boolean move(int px, int py, int pr, List<Bullet> remove) {
		double xx, yy;
		count++;

		xx = Math.cos(base_angle) * speed;
		yy = Math.sin(base_angle) * speed;

		x += xx;
		y += yy;
		double ta;
		if (refcount > 0) {
			if (x <= 0 && (mode&L)!=0) {
				x += 1;
				ta = base_angle;
				base_angle -= Math.PI / 2;
				base_angle *= -1;
				base_angle += Math.PI / 2;
				angle += (ta - base_angle);
				refcount--;
				isRefed=true;
			}
			else if ((x + Bullet.W[kind]) >= MyFrame.WIDTH && (mode&R)!=0) {
				x -= 1;
				ta = base_angle;
				base_angle += Math.PI / 2;
				base_angle *= -1;
				base_angle -= Math.PI / 2;
				angle += (ta - base_angle);
				refcount--;
				isRefed=true;
			}
			if (y <= 0 && (mode&U)!=0) {
				y += 1;
				ta = base_angle;
				base_angle *= -1;
				angle += (ta - base_angle);
				refcount--;
				isRefed=true;
			} else if ((y + Bullet.H[kind]) >= MyFrame.AHEIGHT && (mode&D)!=0) {
				y -= 1;
				ta = base_angle;
				base_angle += Math.PI;
				base_angle *= -1;
				base_angle += Math.PI;
				angle += (ta - base_angle);
				refcount--;
				isRefed=true;
			}
		}

		if (till < count && !inside()) {
			remove.add(this);
			return false;
		}
		return isHit(px, py, pr, remove);
	}

	public void clearRefed(){
		isRefed=false;
	}
	public boolean getRefed(){
		return isRefed;
	}

}
