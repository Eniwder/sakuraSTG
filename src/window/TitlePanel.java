package window;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import menu.Menu;
import menu.MenuList;
import menu.Title;


public class TitlePanel extends JPanel{

	Title title;
	private static float FADE_COUNT = 60;
	private static int FONT_SIZE = 32;
	float count=FADE_COUNT;
	private BufferedImage back;
	public TitlePanel(Title title){
		this.title=title;
		try{
			back = ImageIO.read(new File("res/img/back/title.png"));
		}catch(Exception e){

		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		// 背景
		g2.drawImage(back, 0,0,this);

		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		// メニューを表示
		MenuList<Menu> ml = title.getMenuList();
		g2.setFont(new Font(Font.DIALOG, Font.BOLD, FONT_SIZE));
		g2.setColor(Color.WHITE);
		for(Menu menu:ml.getList()){
			float alpha = menu.equals(ml.get())?1F:0.5F;
			AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha);
			g2.setComposite(composite);
			g2.drawString(menu.name, menu.x, menu.y);
		}
		// 開幕フェード
		if(--count>=0){
			AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, count / FADE_COUNT);
			g2.setComposite(composite);
			g2.setColor(Color.BLACK);
			g2.fillRect(0, 0, 1000, 1000);
		}



	}

}
