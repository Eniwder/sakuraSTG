package menu;

import etc.SE;
import etc.Scene;


public class Menu{
	public String name;
	public int x,y;
	public int val;
	private Scene scene;

	public Menu(String name,int x,int y,Scene scene){
		this(name,x,y,-1,scene);
	}
	public Menu(String name,int x,int y,int val){
		this(name,x,y,val,null);
	}
	public Menu(String name,int x,int y,int val,Scene scene){
		this.name = name;
		this.x=x;
		this.y=y;
		this.val=val;
		this.scene=scene;
	}

	public Scene retScene(){
		if(scene!=null)SE.start("ok");
		return scene;
	}

	public void add(int n,int max,int min){
		SE.start("hit");
		this.val+=n;
		if(val>max)val=max;
		else if(val<min)val=min;
	}

	public void add(int n){
		this.add(n,Integer.MAX_VALUE,Integer.MIN_VALUE);
	}

	public void addmod(int n,int mod){
		this.add(n,Integer.MAX_VALUE,Integer.MIN_VALUE);
		if(val<0)val=mod+val;
		this.val%=mod;
	}



}