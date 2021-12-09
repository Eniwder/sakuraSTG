package etc;

import java.awt.event.KeyEvent;
import java.util.EnumSet;

public class Key {
	public static enum KeyCode {
		UP, LEFT, RIGHT, DOWN, SHOT, SLOW, FAST;
	}

	static EnumSet<KeyCode> keyset = EnumSet.noneOf(KeyCode.class);
	static KeyCode kc;

	public static void inputKey(KeyEvent e) {
		switch (e.getKeyCode()) {
		// 上キー
		case KeyEvent.VK_UP:
			keyset.add(KeyCode.UP);
			break;
		// 下キー
		case KeyEvent.VK_DOWN:
			keyset.add(KeyCode.DOWN);
			break;
		// 左キー
		case KeyEvent.VK_LEFT:
			keyset.add(KeyCode.LEFT);
			break;
		// 右キー
		case KeyEvent.VK_RIGHT:
			keyset.add(KeyCode.RIGHT);
			break;
		// Zキー
		case KeyEvent.VK_Z:
			keyset.add(KeyCode.SHOT);
			break;
		// SHIFTキー
		case KeyEvent.VK_SHIFT:
			keyset.add(KeyCode.SLOW);
			break;
		// Xキー
		case KeyEvent.VK_X:
			keyset.add(KeyCode.FAST);
			break;
		}
	}

	public static void releaseKey(KeyEvent e) {
		switch (e.getKeyCode()) {
		// 上キー
		case KeyEvent.VK_UP:
			keyset.remove(KeyCode.UP);
			break;
		// 下キー
		case KeyEvent.VK_DOWN:
			keyset.remove(KeyCode.DOWN);
			break;
		// 左キー
		case KeyEvent.VK_LEFT:
			keyset.remove(KeyCode.LEFT);
			break;
		// 右キー
		case KeyEvent.VK_RIGHT:
			keyset.remove(KeyCode.RIGHT);
			break;
		// Zキー
		case KeyEvent.VK_Z:
			keyset.remove(KeyCode.SHOT);
			break;
		// SHIFTキー
		case KeyEvent.VK_SHIFT:
			keyset.remove(KeyCode.SLOW);
			break;
			// Xキー
		case KeyEvent.VK_X:
			keyset.remove(KeyCode.FAST);
			break;
		}
	}

	public static EnumSet<KeyCode> getKey() {
		return keyset.clone();
	}

}
