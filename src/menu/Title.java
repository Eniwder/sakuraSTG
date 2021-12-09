package menu;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import window.MyFrame;
import etc.Key;
import etc.SE;
import etc.Scene;

public class Title {
	private MyFrame mf;
	private MenuList<Menu> ml = new MenuList<>(Arrays.asList(
			new Menu("はじめる", 400, 380,Scene.MENU),
			new Menu("設定", 400, 430,Scene.CONFIG),
			new Menu("知世ちゃんの部屋", 400, 480,Scene.REPLAY),
			new Menu("終わる", 400, 530,null){
				@Override public Scene retScene(){
					SE.singleStart("cancel");
					MyFrame.fin();
					System.exit(0);
					return null;
				}
			}));

	int select = 0;
	int load = 15;
	boolean inputf=false;

	public Title(MyFrame mf) {
		this.mf = mf;
	}

	public Scene start() throws InterruptedException, InvocationTargetException {

		while (true) {
			final EnumSet<Key.KeyCode> keys = Key.getKey();
			if(!keys.contains(Key.KeyCode.SHOT) && !keys.contains(Key.KeyCode.FAST) && load<0)inputf=true;
			if (--load < 0 && inputf) {
				if (keys.contains(Key.KeyCode.SHOT)) {
					return ml.get().retScene();
				}else if(keys.contains(Key.KeyCode.FAST)){
					SE.start("cancel");
					inputf=false;
					ml.setSeclet(-3);
				}

				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						if (keys.contains(Key.KeyCode.UP) && !keys.contains(Key.KeyCode.DOWN)) {
							ml.incSelect(5);
						} else if (!keys.contains(Key.KeyCode.UP) && keys.contains(Key.KeyCode.DOWN)) {
							ml.decSelect(5);
						}
						if(keys.isEmpty())ml.resetCool();
						ml.cooldown();
					}
				});
			}
				try {
					TimeUnit.MICROSECONDS.sleep(32222);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mf.repaint();
		}
	}

	public MenuList<Menu> getMenuList() {
		return ml;
	}

}
