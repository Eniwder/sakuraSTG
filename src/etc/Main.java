package etc;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import menu.AfterGame;
import menu.Card;
import menu.CardList;
import menu.Config;
import menu.Game;
import menu.ReplayList;
import menu.Title;
import window.CardListPanel;
import window.ConfigPanel;
import window.GamePanel;
import window.LoadPanel;
import window.MyFrame;
import window.ReplayListPanel;
import window.TitlePanel;
import barrage.Bullet;
import character.Player;

// メイン
public class Main extends JFrame {
	static MyFrame mf;
	public static Replay rp = new Replay();
	private static final String[] kinds = { "ok", "cancel", "shot", "blow", "fire", "kane", "bobo", "tararan", "sword",
			"swordf", "hit", "atack", "fast" };
	private static final int[] needsOfSE = { 8, 5, 10, 2, 2, 2, 2, 2, 3, 6, 10, 2, 1 };
	static Clip battleBGM=null,titleBGM=null,replayBGM = null;
	static FloatControl bc = null,tc=null,rc=null;

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {

		mf = new MyFrame();
		Scene scene = Scene.LOAD;
		Card card = null;
		Replay rep = null;
		boolean replaying = false;
		int repn = 0;
		init();
		JPanel bp = new JPanel();
		mf.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				MyFrame.fin();
				System.exit(0);
			}
		});


		while (true) {
			switch (scene) {
			case LOAD:
				if (!SE.init(kinds, needsOfSE)) {
					System.err.println("曲とテーブルの数が合わないんで終わります");
					System.exit(ERROR);
				}
				Bullet.init();
				CardList.clear();
				LoadPanel lp = new LoadPanel();
				mf.add(lp, BorderLayout.CENTER);
				mf.setVisible(true);
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						mf.repaint();
					}
				});

				bp = lp;
				scene = Scene.TITLE;

			case TITLE:
				tc.setValue((float) Math.log10(Config.BGMVol * 0.2 * 0.01) * 20);
				titleBGM.setFramePosition(0);
				titleBGM.loop(Clip.LOOP_CONTINUOUSLY);

				repn = 0;
				Title title = new Title(mf);
				TitlePanel tp = new TitlePanel(title);
				mf.remove(bp);
				mf.add(tp, BorderLayout.CENTER);
				mf.setVisible(true);
				try {
					scene = title.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(scene==Scene.REPLAY){
					titleBGM.stop();
				}
				bp = tp;
				break;
			case CONFIG:
				Config config = new Config(mf);
				config.init();
				ConfigPanel cp = new ConfigPanel(config);
				mf.add(cp, BorderLayout.CENTER);
				mf.remove(bp);
				mf.setVisible(true);
				try {
					scene = config.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
				bp = cp;
				break;
			case MENU:
				if(!titleBGM.isRunning()){
					titleBGM.setFramePosition(0);
					titleBGM.loop(Clip.LOOP_CONTINUOUSLY);
				}
				replaying = false;
				CardList cl = new CardList(mf);
				cl.init();
				CardListPanel clp = new CardListPanel(cl);
				mf.add(clp, BorderLayout.CENTER);
				mf.remove(bp);
				mf.setVisible(true);
				try {
					card = cl.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (card == null){
					scene = Scene.TITLE;
				}else{
					scene = Scene.GAME;
					titleBGM.stop();
				}
				bp = clp;
				break;
			case REPLAY:
				if(!replayBGM.isRunning()){
					rc.setValue((float) Math.log10(Config.BGMVol * 0.2 * 0.01) * 20);
					replayBGM.setFramePosition(0);
					replayBGM.loop(Clip.LOOP_CONTINUOUSLY);
				}
				ReplayList rl = new ReplayList(mf, repn);
				ReplayListPanel rlp = new ReplayListPanel(rl);
				mf.add(rlp, BorderLayout.CENTER);
				mf.remove(bp);
				mf.setVisible(true);
				bp = rlp;
				// リプレイを選択
				try {
					rep = rl.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
				// リプレイを選択していたらゲームへ、そうでなければタイトルへ
				if (rep != null) {
					scene = Scene.GAME;
					repn = rep.getNum();
					replaying = true;
				} else {
					scene = Scene.TITLE;
				}
				replayBGM.stop();
				break;
			case GAME:
				rp.init();
				GamePanel gp = null;
				try {
					if(!battleBGM.isRunning()){
						bc.setValue((float) Math.log10(Config.BGMVol * 0.2 * 0.01) * 20);
						battleBGM.setFramePosition(0);
						battleBGM.loop(Clip.LOOP_CONTINUOUSLY);
					}
					Game game;

					AfterGame ag = new AfterGame(mf, replaying);
					if (replaying)
						game = new Game(mf, CardList.getCard(rep.getCardNum()), ag, bc, new Player(rep));
					else
						game = new Game(mf, card, ag, bc, new Player());

					gp = new GamePanel(game, ag);

					mf.add(gp, BorderLayout.CENTER);
					mf.remove(bp);
					mf.setVisible(true);

					// 音量を徐々に上げていく(効果不明)
					for (int i = (int) (Config.BGMVol * 0.2); i < Config.BGMVol; i++)
						bc.setValue((float) Math.log10(Config.BGMVol * 0.2 * 0.01) * 20);
					if (replaying) {
						scene = game.start(rep.getSeed());
					} else {
						card.incChallenge();
						scene = game.start(System.currentTimeMillis());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(scene != Scene.GAME)battleBGM.stop();
				bp = gp;

				break;
			}
		}
	}

	private static void init() {

		// ウィンドウ関係
		mf.setBounds(100, 100, MyFrame.WIDTH, MyFrame.HEIGHT);
		mf.setResizable(false);

		// 設定を読み込む
		try (FileReader fr = new FileReader("config.ccs"); BufferedReader b = new BufferedReader(fr)) {
			try (Scanner sc = new Scanner(b.readLine())) {
				sc.useDelimiter(",");
				Config.setBGMVol(sc.nextInt());
				Config.setSEVol(sc.nextInt());
			}
		} catch (Exception e) {
			Config.setBGMVol(100);
			Config.setSEVol(80);
		}

		// BGMを読み込む
		try {
			battleBGM = AudioSystem.getClip();
			titleBGM = AudioSystem.getClip();
			replayBGM = AudioSystem.getClip();
			battleBGM.open(AudioSystem.getAudioInputStream(new File("res/sound/BGM/battle.wav")));
			titleBGM.open(AudioSystem.getAudioInputStream(new File("res/sound/BGM/title.wav")));
			replayBGM.open(AudioSystem.getAudioInputStream(new File("res/sound/BGM/replay.wav")));
			bc = (FloatControl) battleBGM.getControl(FloatControl.Type.MASTER_GAIN);
			tc = (FloatControl) titleBGM.getControl(FloatControl.Type.MASTER_GAIN);
			rc = (FloatControl) replayBGM.getControl(FloatControl.Type.MASTER_GAIN);
			bc.setValue((float) Math.log10(Config.BGMVol * 0.2 * 0.01) * 20);
		}catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	public static void fin(){
		mf.setVisible(false);
		battleBGM.close();
		titleBGM.close();
		replayBGM.close();
	}




}
