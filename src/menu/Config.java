package menu;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.EnumSet;

import javax.swing.SwingUtilities;

import window.MyFrame;
import etc.Key;
import etc.Scene;

public class Config {
	MyFrame mf;
	public static int BGMVol,SEVol;
	private static final int BGM=0,SE=1;
	private MenuList<Menu> ml = new  MenuList<>(Arrays.asList(
			new Menu("BGM音量", 100, 100,100),
			new Menu("SE音量", 100, 200,80),
			new Menu("確定", 100, 400,Scene.TITLE)));

	int load = 15;
	boolean inputf=false;
	int beBGMVol,beSEVol;
	public Config(MyFrame mf) {
		this.mf = mf;
	}

	public Scene start() throws InterruptedException, InvocationTargetException {
		beBGMVol=BGMVol;
		beSEVol=SEVol;
		while (true) {
			final EnumSet<Key.KeyCode> keys = Key.getKey();
			if(!keys.contains(Key.KeyCode.SHOT) && load<0)inputf=true;
			if (--load < 0 && inputf) {
				if (keys.contains(Key.KeyCode.SHOT)) {
					etc.SE.volumeReset();
					Scene s = ml.get().retScene();
					if(s!=null)return s;
				}else if(keys.contains(Key.KeyCode.FAST)){
					BGMVol = beBGMVol;
					SEVol = beSEVol;
					etc.SE.start("cancel");
					return Scene.TITLE;
				}
			}
			try {
				Thread.sleep(32);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					if (keys.contains(Key.KeyCode.UP) && !keys.contains(Key.KeyCode.DOWN)) {
						ml.incSelect(5);
					} else if (!keys.contains(Key.KeyCode.UP) && keys.contains(Key.KeyCode.DOWN)) {
						ml.decSelect(5);
					}else if ((keys.contains(Key.KeyCode.LEFT) || keys.contains(Key.KeyCode.RIGHT))) {
						int n = keys.contains(Key.KeyCode.RIGHT)?(keys.contains(Key.KeyCode.LEFT)?0:5):-5;
						if(ml.isCooloff()){
							ml.get().addmod(n,105);
							ml.setCool(5);
						}
						BGMVol=ml.get(BGM).val;
						SEVol=ml.get(SE).val;
					}

					if(keys.isEmpty())ml.resetCool();
					ml.cooldown();
					// 再描画
					mf.repaint();
				}
			});

		}
	}

	public MenuList<Menu> getMenuList(){
		return ml;
	}

	public void init(){
		ml.get(0).val=BGMVol;
		ml.get(1).val=SEVol;
	}


	public static void setBGMVol(int vol){
		if(vol<0)vol=0;
		else if(vol>100)vol=100;
		BGMVol=vol;
	}
	public static void setSEVol(int vol){
		if(vol<0)vol=0;
		else if(vol>100)vol=100;
		SEVol=vol;
	}
	public static int getBGMVol(){
		return BGMVol;
	}
	public static int getSEVol(){
		return SEVol;
	}



}
