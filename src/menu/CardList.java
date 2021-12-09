package menu;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import javax.swing.SwingUtilities;

import window.MyFrame;
import barrage.Dark;
import barrage.Earthy;
import barrage.Erase;
import barrage.Firey;
import barrage.Flower;
import barrage.Glow;
import barrage.Illusion;
import barrage.Jump;
import barrage.Light;
import barrage.Maze;
import barrage.Mirror;
import barrage.Shadow;
import barrage.Shield;
import barrage.Sword;
import barrage.Thunder;
import barrage.Warty;
import barrage.Windy;
import barrage.Wood;
import etc.Key;
import etc.SE;

public class CardList{
	private MyFrame mf;
	private static List<Card> cl = new ArrayList<>(Arrays.asList(
			(new Card("「風」ウィンディ", (new Windy()), 800,2)),
			(new Card("「樹」ウッド", (new Wood()), 820,1)),
			(new Card("「跳」ジャンプ", (new Jump()), 820,2)),
			(new Card("「水」ウォーティ", (new Warty()), 800,3)),
			(new Card("「幻」イリュージョン", (new Illusion()), 860,3)),
			(new Card("「花」フラワー", (new Flower()), 900,2)),
			(new Card("「剣」ソード", (new Sword()), 800,3)),
			(new Card("「雷」サンダー", (new Thunder()), 700,3)),
			(new Card("「影」シャドウ", (new Shadow()), 800,3)),
			(new Card("「鏡」ミラー", (new Mirror()), 600,4)),
			(new Card("「盾」シールド", (new Shield()), 800,3)),
			(new Card("「迷」メイズ", (new Maze()), 800,3)),
			(new Card("「消」イレイズ", (new Erase()), 450,3)),
			(new Card("「灯」グロウ", (new Glow()), 700,1)),
			(new Card("「光」ライト", (new Light()), 100000,4)),
			(new Card("「闇」ダーク", (new Dark()), 250,4)),
			(new Card("「火」ファイアリー", (new Firey()), 4000,3)),
			(new Card("「地」アーシー", (new Earthy()), 800,2))
			));

	private static int select;
	int count = 0;
	int load = 30;
	boolean inputf = false;
	static boolean init = false;

	public CardList(MyFrame mf) {
		this.mf = mf;
		if(!init){
			try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("cardlist.ccs"))){
				for(Card c:cl){
					c.setData(ois.readObject());
				}
			}catch(Exception e){
				System.out.println("カードの情報を再生成");
				for(Card c:cl){
					c.makedata();
				}
			}finally{
				init=true;
			}
		}

		for(int i=0;i<cl.size();i++){
			cl.get(i).setNum(i);
		}

	}

	public static void clear() {
		select=0;
	}

	public static Card getCard(int num){
		return cl.get(num);
	}

	public Card start() throws InterruptedException, InvocationTargetException {

		while (true) {
			final EnumSet<Key.KeyCode> keys = Key.getKey();
			if (!keys.contains(Key.KeyCode.SHOT) && load < 0)
				inputf = true;
			if (--load < 0 && inputf) {
				if (keys.contains(Key.KeyCode.SHOT)) {
					SE.start("ok");
					return cl.get(getNum());
				} else if (keys.contains(Key.KeyCode.FAST)) {
					SE.start("cancel");
					return null;
				}

				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						--count;
						if (keys.contains(Key.KeyCode.UP) && !keys.contains(Key.KeyCode.DOWN) && count < 0) {
							cl.get(getNum()).isSelected = false;
							SE.start("shot");
							select++;
							count = 6;
						} else if (!keys.contains(Key.KeyCode.UP) && keys.contains(Key.KeyCode.DOWN) && count < 0) {
							cl.get(getNum()).isSelected = false;
							SE.start("shot");
							select--;
							count = 6;
						}
						cl.get(getNum()).isSelected = true;
					}
				});
			}
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			mf.repaint();
		}
	}

	private int getNum() {
		if (select > 0) {
			return Math.abs(cl.size() - select % cl.size()) % cl.size();
		} else {
			return Math.abs((select) % cl.size());
		}
	}

	public List<Card> getCard() {
		return cl;
	}

	public void init() {
		cl.get(getNum()).isSelected = true;
	}

	public static void fin(){
		if(init){
			try( ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("cardList.ccs"))){
				for(Card c:cl){
					oos.writeObject(c.getData());
				}
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("シリアライズ失敗");
			}
		}
	}

}
