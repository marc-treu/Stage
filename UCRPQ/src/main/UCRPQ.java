package main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UCRPQ {

	List<CRPQ> children;
	
	public UCRPQ(List<CRPQ> children) {
		this.children = children;
	}
	

	public String toCypher() {
		StringBuilder sb = new StringBuilder();
		
		if(this.isCypherable()) {
			
			Iterator<CRPQ> it = this.children.iterator();
			
			sb.append(it.next().toCypher());
			
			while(it.hasNext()) {
				sb.append("\nUNION\n");
				sb.append(it.next().toCypher());
			}
		
		}else {
			List<CRPQ> tmp = new ArrayList<>();
			for (CRPQ e : this.children) {
				for( CRPQ c : e.getCypherable()) {
					if (c == null) {return null;}
					tmp.add(c);
				}
			}
			 sb.append(new UCRPQ(tmp).toCypher());
		}
		
		return sb.toString();
	}
	
	public boolean isCypherable() {
		for (CRPQ e : this.children) {
			if (!e.isCypherable())
				return false;
		}
		return true;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(this.children.get(0).toString());
		
		for(int i=1 ;i<this.children.size();++i) {
			sb.append(") || (");
			sb.append(this.children.get(i).toString());
		}
		sb.append(")");
		return sb.toString();
		
	}
	
}
