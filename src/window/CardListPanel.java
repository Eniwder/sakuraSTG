package window;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.JPanel;

import menu.Card;
import menu.CardList;


public class CardListPanel extends JPanel{

	CardList cl;
	int index;
	int colorCount=0;
	private static final float FADE_COUNT = 60;
	private static final int NAME_SIZE = 36;
	private static final int DISC_SIZE = 24;
	private static final float[] ALPHA={1F,0.8F,0.6F,0.4F,0.2F,0F};
	private static final int SEP = 450;
	private static final int LX = 20;
	public static boolean moving = true;
	float count=FADE_COUNT;
	private int bef;
	private float mc;
	private float disca=0;
	public CardListPanel(CardList cl){
		this.cl=cl;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
	    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    g2.setBackground(Color.BLACK);
		g2.clearRect(0, 0, MyFrame.WIDTH, MyFrame.HEIGHT);

		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		g2.setStroke(new BasicStroke(3.0f));
		g2.drawLine(SEP, 0, SEP, MyFrame.AHEIGHT);

		List<Card> clist = cl.getCard();
		// 現在示しているカードの位置を取得
		for(index=0;!clist.get(index).isSelected&&index<clist.size();index++);

		// 選択してるカードが変わったかどうか
		if(index!=bef){
			mc+=index<bef?10:-10;
			if(moving){
				for(int i=bef-5;i<bef+6;i++){
					if(i>=0 && i<clist.size()){
						float alp = ALPHA[Math.abs(i-bef)];
						alp+=index>bef?bef>i?mc*0.008/4F:-mc*0.03/4F:bef<i?-mc*0.008/4F:mc*0.03/4F;
						if(alp>1F)alp=1F;
						else if(alp<0)alp=0;
						g2.setFont(new Font(Font.DIALOG, Font.BOLD, NAME_SIZE));
						AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alp);
						g2.setComposite(composite);
						if(i==index)g2.setColor(new Color(255,255,255));
						else g2.setColor(Color.GRAY);
						g2.drawString(clist.get(i).name, LX, (i-bef)*60+MyFrame.HEIGHT/2+mc);
					}
				}
				if(Math.abs(mc)>=60)moving=false;
			}else{
				colorCount=200;
				for(int i=index-4;i<index+5;i++){
					if(i>=0 && i<clist.size()){
						g2.setFont(new Font(Font.DIALOG, Font.BOLD, NAME_SIZE));
						AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,ALPHA[Math.abs(index-i)]);
						g2.setComposite(composite);
						if(i==index)g2.setColor(new Color(255,255,255));
						else g2.setColor(Color.GRAY);
						g2.drawString(clist.get(i).name, LX, (i-index)*60+MyFrame.HEIGHT/2);
					}
				}
				bef=index;
				mc=disca=0;
				moving=true;
			}
		}else{
			// 止まっている時の処理
			for(int i=index-4;i<index+5;i++){
					if(i>=0 && i<clist.size()){
						g2.setFont(new Font(Font.DIALOG, Font.BOLD, NAME_SIZE));
						AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,ALPHA[Math.abs(index-i)]);
						g2.setComposite(composite);
						int col=255-Math.abs(120-(++colorCount/2%241));
						if(i==index)g2.setColor(new Color(col,col,col));
						else g2.setColor(Color.GRAY);
						g2.drawString(clist.get(i).name, LX, (i-index)*60+MyFrame.HEIGHT/2);
					}
			}

			// カードの情報(フェードで表示)
			if(++disca>=60)disca=60;

			g2.setFont(new Font(Font.DIALOG, Font.BOLD, DISC_SIZE));
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, disca/60));
			g2.setColor(Color.PINK);
			String str="";

			for(int i=0;i<clist.get(index).getLevel();i++)str+="★";
			g2.drawString(str,SEP+30,80+45);

			str=(clist.get(index).getfirstGet()==0)?"未撃破":clist.get(index).getfirstGet()+"回目";
			g2.drawString(str,SEP+30,180+45);

			g2.drawString(clist.get(index).getGetCount()+" / "+clist.get(index).getChallengeCount(),SEP+30,280+45);

		}

		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		g2.setFont(new Font(Font.DIALOG, Font.BOLD, DISC_SIZE));
		g2.setColor(Color.GRAY);
		g2.drawString("難しさ",SEP+20,80);
		g2.drawString("初撃破",SEP+20,180);
		g2.drawString("撃破回数 / 挑戦回数",SEP+20,280);

		g2.drawString("コンプ率",SEP+20,480);
		double getCount=0,rate;
		for(int i=0;i<clist.size();i++)if(clist.get(i).getfirstGet()!=0)getCount++;
		rate=Math.ceil( (getCount/clist.size())*10000 );
		rate/=100;
		if(rate==100)g2.setColor(Color.YELLOW);
		else g2.setColor(Color.PINK);
		g2.drawString(rate+"％",SEP+30,480+50);


		if(--count>=0){
			AlphaComposite composite1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, count / FADE_COUNT);
			g2.setComposite(composite1);
			g2.setColor(Color.BLACK);
			g2.fillRect(0, 0, 1000, 1000);
		}

	}
}
