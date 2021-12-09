package menu;

import java.util.ArrayList;
import java.util.List;

import etc.SE;

public class MenuList<E> {

	protected final List<E> list;
	protected int select=0,cool=0;


	public MenuList(List<E> list){
		this.list = new ArrayList<>(list);
	}

	public MenuList(){
		this.list = new ArrayList<>();
	}

	public int incSelect(int cool){
		if(isCooloff()){
			SE.start("shot");
			select++;
			this.cool=cool;
		}
		return getSelect();
	}

	public int decSelect(int cool){
		if(isCooloff()){
			SE.start("shot");
			select--;
			this.cool=cool;
		}
		return getSelect();
	}

	public int getSelect(){
		if(select>0){
			return Math.abs(list.size()-select%list.size())%list.size();
		}else{
			return Math.abs((select)%list.size());
		}
	}

	public void setSeclet(int select){
		this.select=select;
	}

	public E get(){
		return list.get(getSelect());
	}

	public E get(int n){
		return list.get(n);
	}

	public void cooldown(){
		cool--;
	}

	public int size(){
		return list.size();
	}

	public List<E> getList(){
		return list;
	}

	public void resetCool(){
		cool=-1;
	}

	public void setCool(int n){
		cool=n;
	}

	public boolean isCooloff(){
		return cool<0;
	}

	public void add(E obj){
		list.add(obj);
	}

}
