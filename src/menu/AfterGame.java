package menu;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.EnumSet;

import javax.swing.SwingUtilities;

import window.MyFrame;
import etc.Key;
import etc.Main;
import etc.Scene;

public class AfterGame {
	private MyFrame mf;



	private MenuList<Menu> ml;

	int load = 30;
	boolean inputf=false;
	boolean isReplay;

	public AfterGame(MyFrame mf,boolean isReplay) {
		this.mf = mf;
		this.isReplay=isReplay;

		if(!isReplay){
			ml = new MenuList<>(Arrays.asList(
					new Menu("もういちど挑戦", MyFrame.WIDTH/2-120,MyFrame.HEIGHT/2-50,Scene.GAME),
					new Menu("メニューに戻る", MyFrame.WIDTH/2-120, MyFrame.HEIGHT/2+50,Scene.MENU),
					new Menu("リプレイを保存", MyFrame.WIDTH/2-120,MyFrame.HEIGHT/2+150,Scene.MENU){
						@Override public Scene retScene(){
							Main.rp.save();
							return super.retScene();
						}
					}));
		}else{
			ml = new MenuList<>(Arrays.asList(
					new Menu("もっと鑑賞する", MyFrame.WIDTH/2-120,MyFrame.HEIGHT/2-50,Scene.GAME),
					new Menu("他のビデオを見る", MyFrame.WIDTH/2-135, MyFrame.HEIGHT/2+50,Scene.REPLAY)));
		}

	}

	public Scene start(int s) throws InterruptedException, InvocationTargetException {
		ml.setSeclet(s);
		if(s!=0)Main.rp.setClear();
		while (true) {
			final EnumSet<Key.KeyCode> keys = Key.getKey();
			if(!keys.contains(Key.KeyCode.SHOT) && load<0)inputf=true;
			if (--load < 0 && inputf) {
				if (keys.contains(Key.KeyCode.SHOT)) {
					return ml.get().retScene();
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
					Thread.sleep(32);
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
