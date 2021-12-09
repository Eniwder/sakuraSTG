package window;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import menu.Config;
import menu.Menu;
import menu.MenuList;


public class ConfigPanel extends JPanel{

	private static float FADE_COUNT = 60;
	private static int FONT_SIZE = 36;
	float count=FADE_COUNT;

	Config conf;

	public ConfigPanel(Config conf){
		this.conf=conf;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// 黒背景
		g2.setBackground(Color.BLACK);
		g2.clearRect(0, 0, MyFrame.WIDTH, MyFrame.HEIGHT);

		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		// メニューを表示
		MenuList<Menu> ml = conf.getMenuList();
		g2.setFont(new Font(Font.DIALOG, Font.BOLD, FONT_SIZE));
		g2.setColor(Color.PINK);
		for(Menu menu:ml.getList()){
			float alpha = menu.equals(ml.get())?1F:0.5F;
			AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha);
			g2.setComposite(composite);
			g2.drawString(menu.name, menu.x, menu.y);
			if(!menu.name.equals("確定")){
				if(menu.name.equals("描画品質")){
					String temp=(menu.val%2==0)?"高":"低";
					g2.drawString(temp, menu.x+300, menu.y);
				}else{
					g2.drawString(menu.val+"％", menu.x+300, menu.y);
				}
			}
		}

	if(--count>=0){
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, count / FADE_COUNT));
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, 1000, 1000);
	}
	}
}
