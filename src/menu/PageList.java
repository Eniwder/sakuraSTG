package menu;

import etc.SE;

public class PageList<E> extends MenuList<E>{
	int page;
	private final int LISTS;

	public PageList(int LISTS){
		this.LISTS=LISTS;
	}


	public int getPage(){
		if(page>0){
			return Math.abs(((list.size()-1)/LISTS+1)-page%((list.size()-1)/LISTS+1))%((list.size()-1)/LISTS+1);
		}else{
			return Math.abs((page)%((list.size()-1)/LISTS+1));
		}
	}

	public int getLists(){
		return LISTS;
	}

	@Override
	public int incSelect(int cool){
		if(isCooloff()){
			SE.start("shot");
			select++;
			while( (getSelect()+getPage()*LISTS)>=list.size() )select++;
			this.cool=cool;
		}
		return getSelect();
	}

	@Override
	public int decSelect(int cool){
		if(isCooloff()){
			SE.start("shot");
			select--;
			while( (getSelect()+getPage()*LISTS)>=list.size() )select--;
			this.cool=cool;
		}
		return getSelect();
	}

	public void addPage(int n,int cool) {
		if(this.cool<0){
			SE.start("shot");
			if(list.size()>LISTS)page+=n;
			this.cool=cool;
		}
		select=0;
	}

	@Override
	public E get(){
		return list.get(getSelect()+getPage()*LISTS);
	}

	@Override
	public int getSelect(){
		if(select>0){
			return (Math.abs(LISTS-select%LISTS)%LISTS)%LISTS;
		}else{
			return (Math.abs((select)%LISTS))%LISTS;
		}
	}


}
