package menu;

import java.io.Serializable;

import barrage.AbstractBarrage;

public class Card{
	public String name;
	public int hp;
	public AbstractBarrage barrage;
	public boolean isSelected=false;
	private int cardNum;

	private int level;

	private Data data=null;

	public Card(String name,AbstractBarrage barrage,int hp,int level){
		this.hp=hp;
		this.barrage=barrage;
		this.name=name;
		this.level=level;
	}

	public void setNum(int n){
		cardNum=n;
	}

	public int getNum(){
		return cardNum;
	}

	private static class Data implements Serializable{
		private static final long serialVersionUID = 1L;
		int challengeCount;
		int getCount;
		int firstGet;
	}

	public void makedata(){
		data=new Data();
	}

	public void setData(Object object) throws Exception{
		if(object==null)throw new Exception();
		this.data=(Data) object;
	}

	public void incChallenge(){
		data.challengeCount++;
	}

	public void incGet(){
		if(data.firstGet==0)data.firstGet=data.challengeCount;
		data.getCount++;
	}

	public void setFirstGet(int num){
		data.firstGet=num;
	}

	public int getLevel(){
		return level;
	}

	public int getfirstGet(){
		return data.firstGet;
	}
	public int getChallengeCount(){
		return data.challengeCount;
	}

	public int getGetCount(){
		return data.getCount;
	}
	public Data getData(){
		return data;
	}

}
