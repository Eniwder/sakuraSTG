package etc;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import menu.Config;

public class SE {
	private static final Map<String, List<Clip>> seMap = new HashMap<>();
	private static final int MAX_SIMULTANEOUS_PLAYBACK = 10;
	private static String[] kinds;
	private static int[] needsOfSE;
	private static AudioInputStream[][] audioIn;
	private static int vol;
	private static Clip cl;

	private SE() {
		// 作られない
	}

	public static boolean init(String[] k, int[] n) {
		kinds = k;
		needsOfSE = n;
		audioIn = new AudioInputStream[kinds.length][MAX_SIMULTANEOUS_PLAYBACK];
		if (kinds.length != needsOfSE.length)
			return false;
		vol = Config.SEVol;
		int count = 0;
		try {
			for (String name : kinds) {
				List<Clip> cls = new LinkedList<>();
				for (int i = 0; i < needsOfSE[count]; i++) {
					audioIn[count][i] = AudioSystem.getAudioInputStream(new File("res/sound/SE/" + name + ".wav"));
					cl = AudioSystem.getClip();
					cl.open(audioIn[count][i]);
					FloatControl control = (FloatControl) cl.getControl(FloatControl.Type.MASTER_GAIN);
					control.setValue((float) Math.log10(vol * 0.2 * 0.01) * 20);
					cls.add(cl);
				}
				seMap.put(name, cls);
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void volumeReset() {
		if (vol != Config.SEVol) {
			vol = Config.SEVol;
			for (String name : kinds) {
				for (Clip cl : seMap.get(name)) {
					FloatControl control = (FloatControl) cl.getControl(FloatControl.Type.MASTER_GAIN);
					control.setValue((float) Math.log10(vol * 0.2 * 0.01) * 20);
				}
			}
		}
	}

	public static void start(String name) {
		for (Clip cl : seMap.get(name)) {
			if (cl.isRunning())
				continue;
			cl.setFramePosition(0);
			cl.start();
			return;
		}
	}

	public static void singleStart(String name) {
		cl = seMap.get(name).get(0);
		if (cl.isRunning())
			return;
		cl.setFramePosition(0);
		cl.start();
	}

	public static void waitEnd(String name) {
		cl = seMap.get(name).get(0);
		while (cl.isRunning()) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return;
	}

	public static void fin() {

		for (List<Clip> cls : seMap.values()) {
			for (Clip cl : cls) {
				cl.close();
			}
		}
		try {
			for (AudioInputStream[] aiss : audioIn) {
				for (AudioInputStream ais : aiss) {
					if(ais!=null)ais.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
