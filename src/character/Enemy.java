package character;

import java.io.File;

import javax.imageio.ImageIO;

import window.MyFrame;

public class Enemy extends AbstractCharacter{

	// 属性
	int hp=5;
	int count=0;
	boolean isVisible;

	public Enemy(){
		x=MyFrame.WIDTH/2;
		y=0;
		try {
			icon = ImageIO.read(new File("res/enemy1.png"));
		} catch (Exception e) {
			e.printStackTrace();
			icon = null;
		}
		iconW=icon.getWidth();
		iconH=icon.getHeight();

	}

	public void move(){
		count++;
		if(count/10%2==0)x-=20;
		else x+=20;
		if(count<60)y+=5;
		else y-=2;
	}


	public boolean isVisible(){
		return isVisible;
	}

	public void setVisible(boolean f){
		isVisible=f;
	}

	public boolean dead(){
		return hp<=0;
	}

	public boolean inside() {
		if( x>MyFrame.WIDTH || (x+iconW)<=0 || y>MyFrame.HEIGHT || (y+iconH)<=0)return false;
		else return true;
	}

	@Override
	public void hit(){
		hp--;
	}

}
