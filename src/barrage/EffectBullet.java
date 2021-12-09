package barrage;

import java.util.List;

public class EffectBullet extends Bullet {

	public EffectBullet(int kind, int col, int till, double x, double y, double base_angle, double speed) {
		super(kind, col, till, x, y, base_angle, speed,-1);
		}

	@Override
	public Boolean move(int px, int py, int pr, List<Bullet> remove) {
		double xx, yy;
		count++;
		xx = Math.cos(base_angle) * speed;
		yy = Math.sin(base_angle) * speed;
		x += xx;
		y += yy;
		if (till < count) {
			remove.add(this);
			return false;
		}
		return false;
	}


}
