package window;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import menu.PageList;
import menu.ReplayList;
import etc.Replay;


public class ReplayListPanel extends JPanel{

	ReplayList rpl;
	private static float FADE_COUNT = 60;
	private static int FONT_SIZE = 15;
	private static int LINEHEIGHT = 30;
	private static int DATE=30,FNAME=230,NAME=430,FLAG=630;
	float count=FADE_COUNT;
	public ReplayListPanel(ReplayList rpl){
		this.rpl=rpl;
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
		g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, FONT_SIZE));
		g2.setColor(Color.WHITE);
		PageList<Replay> rps = rpl.getList();
		int page = rps.getPage();
		Replay temp;
		g2.drawString("撮影日",DATE,LINEHEIGHT);
		g2.drawString("ビデオの名前",FNAME,LINEHEIGHT);
		g2.drawString("挑戦したカード",NAME,LINEHEIGHT);
		g2.drawString("結果",FLAG,LINEHEIGHT);
		for(int i=0;i<ReplayList.LISTS;i++){
			float alpha = rps.getSelect()==i?1F:0.5F;
			AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha);
			g2.setComposite(composite);
			if( (i+ReplayList.LISTS*page)<rps.size() ){
				temp =rps.get(i+page*ReplayList.LISTS);
				String mark = temp.getClear()?"クリア":"失敗";
				g2.drawString(temp.getDate(),DATE,LINEHEIGHT*(i+2)+10);
				g2.drawString(temp.getFileName(),FNAME,LINEHEIGHT*(i+2)+10);
				g2.drawString(temp.getName(),NAME,LINEHEIGHT*(i+2)+10);
				g2.drawString(mark,FLAG,LINEHEIGHT*(i+2)+10);
			}else{
				g2.drawString("--/--/--",DATE,LINEHEIGHT*(i+2)+10);
				g2.drawString("----------------",FNAME,LINEHEIGHT*(i+2)+10);
				g2.drawString("----------------",NAME,LINEHEIGHT*(i+2)+10);
				g2.drawString("-----",FLAG,LINEHEIGHT*(i+2)+10);
			}
			g2.drawString("< "+(page+1)+" / "+ ((rps.size()-1)/ReplayList.LISTS+1) +" >",MyFrame.WIDTH/2-50,MyFrame.AHEIGHT-30);
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
