package barrage;

import java.util.LinkedList;
import java.util.Random;

import character.Boss;
import character.Player;
abstract public class AbstractBarrage{
	protected int frame,bx,by;
	protected Boss boss;
	protected Player sakura;
	protected LinkedList<Bullet> ls;
	protected Random rand;
	protected static final double DEG = Math.PI/180;


	public void init(Boss boss,Player sakura){
		ls=(LinkedList<Bullet>) boss.getBullets();
		this.boss=boss;
		this.sakura=sakura;
		frame=0;
		bx=boss.getMX();
		by=boss.getMY();
	}

	public void setRandom(long seed){
		rand = new Random(seed);
	}

	abstract public void move();

	protected double getBtoP(){
		return Math.atan2(sakura.getMY()-boss.getMY(),sakura.getMX()-boss.getMX());
	}

}