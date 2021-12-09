package menu;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.EnumSet;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import window.MyFrame;
import etc.Key;
import etc.Replay;
import etc.SE;

public class ReplayList {
	private MyFrame mf;

	public static final int LISTS = 15;

	int load = 30;
	boolean inputf=false;

	int page=0;
	File[] files;
	PageList<Replay> ml = new PageList<>(LISTS);

	public ReplayList(MyFrame mf,int num) {
		this.mf = mf;
		page=num/LISTS*(-1);
		ml.setSeclet(num%LISTS);
		File dir = new File("./rep");
		if(!dir.exists()){
			dir.mkdir();
		}
		files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if(files[i].getName().substring(files[i].getName().length()-7).contains(".tomoyo")){
				File file = files[i];
				try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
					Replay temp = (Replay)ois.readObject();
					temp.setFileName(file.getName());
					ml.add(temp);
				}catch(Exception e){
					JOptionPane.showMessageDialog(mf,"リプレイファイルが壊れています\n"+file.getName());
				}
			}
		}
	}

	public Replay start() throws InterruptedException, InvocationTargetException {
		while (true) {
			final EnumSet<Key.KeyCode> keys = Key.getKey();
			if(!keys.contains(Key.KeyCode.SHOT) && !keys.contains(Key.KeyCode.FAST) &&  load<0)inputf=true;
			if (--load < 0 && inputf) {
				if (keys.contains(Key.KeyCode.SHOT)) {
					SE.start("ok");
					return ml.get();
				}else if (keys.contains(Key.KeyCode.FAST)) {
					SE.start("cancel");
					return null;
				}
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						if (keys.contains(Key.KeyCode.UP) && !keys.contains(Key.KeyCode.DOWN)) {
							ml.incSelect(5);
						} else if (!keys.contains(Key.KeyCode.UP) && keys.contains(Key.KeyCode.DOWN)) {
							ml.decSelect(5);
						}
						if((keys.contains(Key.KeyCode.RIGHT) || keys.contains(Key.KeyCode.LEFT))){
							page=keys.contains(Key.KeyCode.RIGHT)?(keys.contains(Key.KeyCode.LEFT))?0:-1:1;
							ml.addPage(page,5);
						}
						if(keys.isEmpty())ml.resetCool();
						ml.cooldown();
						mf.repaint();
					}
				});
				try {
					Thread.sleep(32);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public PageList<Replay> getList() {
		return ml;
	}



}
