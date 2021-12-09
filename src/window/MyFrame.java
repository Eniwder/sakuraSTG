package window;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileWriter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import menu.CardList;
import menu.Config;
import etc.Key;
import etc.Main;
import etc.SE;

// フレームの設定はここで行う
public class MyFrame extends JFrame implements KeyListener{

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final int AHEIGHT = 580;

	public static int key;


	public MyFrame() {
		setTitle("さくらともうひとつの審判");
		setSize(WIDTH+16,HEIGHT+37);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(new ImageIcon("./res/img/back/icon.png").getImage());
		addKeyListener(this);
	}

	public static int getKey(){
		return key;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Key.inputKey(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Key.releaseKey(e);
	}

	public static void fin(){
		Main.fin();
		closing();
		SE.waitEnd("cancel");
		SE.fin();
	}

	public static void closing() {

		// 設定のデータをファイルに保存
		 try ( FileWriter fw = new FileWriter("config.ccs") ) {
		        fw.write(Config.getBGMVol()+",");
		        fw.write(Config.getSEVol()+",");
		 } catch (Exception e) {
		    	e.printStackTrace();
			}
		 // カードの情報をシリアライズ化
		 CardList.fin();
	}

}
