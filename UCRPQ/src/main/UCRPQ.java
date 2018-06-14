package main;

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
				sb.append("La UCRPQ doit etre modifier");
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
	
}
