package etc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import menu.Card;
import etc.Key.KeyCode;

public class Replay implements Serializable{
	private static final long serialVersionUID = 2L;


	private int cardNum;
	private String name;
	transient private String fileName;
	transient private int num;
	private Date date;
	private boolean clear=false;
	private long seed;	// 弾幕とボスの乱数
	private Map<Integer,EnumSet<Key.KeyCode>> keys;
	private EnumSet<Key.KeyCode> bef;

	private static class Date implements Serializable{
		private static final long serialVersionUID = 3L;
		public int year,month,day;
		@Override public String toString(){
			StringBuilder sb = new StringBuilder();
			sb.append(year);
			sb.append("/");
			if(month<10)sb.append("0"+month);
			else sb.append(month);
			sb.append("/");
			if(day<10)sb.append("0"+day);
			else sb.append(day);
			return sb.toString();
		}
	}

	public Map<Integer,EnumSet<Key.KeyCode>> getMap(){
		return keys;
	}

	public void setDate(){
		Calendar cal = Calendar.getInstance();
		date.year=cal.get(Calendar.YEAR);
		date.month=cal.get(Calendar.MONTH)+1;
		date.day=cal.get(Calendar.DATE);
	}

	public void setClear(){
		clear=true;
	}

	public boolean getClear(){
		return clear;
	}

	public void setFileName(String fn){
		fn=fn.split(".tomoyo")[0];
		fileName=fn;
	}

	public String getFileName(){
		return fileName;
	}


	public void setCard(Card card){
		this.name=card.name;
		this.cardNum=card.getNum();
	}

	public int getCardNum(){
		return cardNum;
	}

	public String getName(){
		return name;
	}

	public String getDate(){
		return date.toString();
	}


	public void setSeed(long seed){
		this.seed=seed;
	}

	public long getSeed(){
		return seed;
	}


	public void setKey(int frame,EnumSet<KeyCode> k){
		if(!bef.equals(k)){
			keys.put(frame, k);
			bef=k;
		}
	}

	public void init(){
		seed=0;
		keys = new HashMap<>();
		bef = EnumSet.noneOf(KeyCode.class);
		date=new Date();
		clear=false;
	}

	public void save() {
		setDate();
		File dir = new File("./rep");
		if(!dir.exists())dir.mkdir();
		File file;
		int i=0;
		do{
			file = new File("./rep/"+date.year+date.month+date.day+i+".tomoyo");
			i++;
		}while(file.exists());
		try( ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))){
			oos.writeObject(this);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("シリアライズ失敗");
		}
	}

	public void setNum(int num){
		this.num=num;
	}

	public int getNum(){
		return num;
	}


}
