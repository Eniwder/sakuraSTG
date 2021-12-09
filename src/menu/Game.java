package menu;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.FloatControl;
import javax.swing.SwingUtilities;

import window.MyFrame;
import barrage.Bullet;
import barrage.EffectBullet;
import character.Boss;
import character.Player;
import etc.Main;
import etc.SE;
import etc.Scene;

public class Game {

	private MyFrame mf;
	public Player sakura;
	public Boss boss;

	private LinkedList<Bullet> bbl;
	private List<Bullet> brem;
	private List<Bullet> pbl;
	private List<Bullet> prem;


	long s;

	static int fps = 60,time=99;
	private static boolean cancel;
	private static double DEG = Math.PI/180;
	private int frame = 0;
	private Card card;
	private AfterGame ag;
	private Random rand;

	FloatControl fc;

	public Game(MyFrame mf,Card card,AfterGame ag,FloatControl fc,Player sakura) {
		this.mf = mf;
		this.ag=ag;
		this.fc=fc;
		this.card=card;
		this.sakura=sakura;
		boss = new Boss(card);
		Main.rp.setCard(card);
		bbl=(LinkedList<Bullet>) boss.getBullets();
		brem = boss.getRemoveBullets();
		pbl=sakura.getBullets();
		prem = sakura.getRemoveBullets();
		cancel=false;
		rand = new Random(System.currentTimeMillis());
	}

	public Scene start(long seed) throws InterruptedException, InvocationTargetException {
		frame=0;
		time=99;
		System.gc();
		Bullet.setPlayer(sakura);
		boss.init(sakura,seed);

		long error = 0;
		final long idealSleep = (1000 << 16) / 60;
		long oldTime,newTime;
		newTime = System.currentTimeMillis() << 16;
		s = System.currentTimeMillis();
		while(!cancel){
			oldTime = newTime;

			if(!sakura.isLive || !boss.isLive){
				// 音量を徐々に下げていく(効果不明)
				for(int i=Config.BGMVol;i>Config.BGMVol*0.2;i--)fc.setValue((float)Math.log10(i*0.2*0.01) * 20);
				if(!sakura.isLive)return ag.start(0);
				else{
					if(!sakura.isReplay()){
						card.incGet();
					}
					return ag.start(1);
				}
			}


			// イベントスレッドに処理を任せて待つ
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					// プレイヤー移動
					sakura.move();
					if(frame%3==0)sakura.shot();

					// ボス移動
					boss.move();
					boss.shot();

					// ボスの弾の移動
					prem.clear();
					int px=sakura.getMX(),py=sakura.getMY(),pr=sakura.getR();
					for(Bullet bl : bbl)if(bl.move(px, py,pr,brem) && !sakura.isFast())sakura.hit();
					for(Bullet rem:brem)bbl.remove(rem);

					// プレイヤーの弾の移動
					brem.clear();
					int bx=boss.getMX(),by=boss.getMY(),br=boss.getR();
					for(Bullet bl : pbl)if(bl.move(bx, by,br,prem)){
						boss.hit();
						bbl.addFirst(new EffectBullet(Bullet.STAR,Bullet.EF,8,bl.getMX(),bl.getMY(),rand.nextInt(360)*DEG,1));
					}
					for(Bullet rem:prem)pbl.remove(rem);

					// ボスとプレイヤーの接触
					int x=px-bx,y=py-by,r=pr+br;
					if((r*r) > ( x*x + y*y ))sakura.hit();

					// 再描画
					mf.repaint();
				}
			});

			if(++frame%60==0){
				final long e = System.currentTimeMillis();
				fps=(int)(60*1000/(e-s));
				if(--time<0)sakura.isLive=false;;
				s = System.currentTimeMillis();
			}

			// 60FPS用の処理
			newTime = System.currentTimeMillis() << 16;
			long sleepTime = idealSleep - (newTime - oldTime) - error; // 休止できる時間
			if (sleepTime < 0x20000) sleepTime = 0x20000; // 最低でも2msは休止
			oldTime = newTime;
			Thread.sleep(sleepTime >> 16); // 休止
			newTime = System.currentTimeMillis() << 16;
			error = newTime - oldTime - sleepTime; // 休止時間の誤差

		}
		SE.start("cancel");
		return Scene.REPLAY;
	}


	public static void replayCancel(){
		cancel=true;
	}

	public double getPlayerMX(){
		return sakura.getMX();
	}
	public double getPlayerMY(){
		return sakura.getMY();
	}

	public final List<Bullet> getPRem(){
		return prem;
	}

	public static String getTime() {
		return String.valueOf(time);
	}

	public static String getFPS() {
		return String.valueOf(fps);
	}
}
